<html>
    <head>
        <title>104 photos - a year of photos!</title>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
        <script type='text/javascript' src='fullcalendar/fullcalendar.min.js'></script>
        <script type='text/javascript' src='fancybox/jquery.fancybox-1.3.4.pack.js'></script>
        <script type="text/javascript" src="js/jquery.form.js"></script>

        <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css" type="text/css" media="all" />
        <link rel='stylesheet' type='text/css' href='fullcalendar/ui-lightness/jquery-ui-1.8.16.custom.css' media="screen" />
        <link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
        <link rel='stylesheet' type='text/css' href='css/calendar.css' />
        <link rel='stylesheet' type='text/css' href='fancybox/jquery.fancybox-1.3.4.css' media="screen"/>

        <script type="text/javascript">
            $(document).ready(function() {
                var host = location.hostname + (location.port?":"+location.port:"");
                var apiPath = "/REST/";
                if ( host.indexOf('localhost') != -1 ) { //If local system the prepend the webapplication path
                    apiPath = '/Picture-Calendar/REST/';
                }
                var BASE_URL = 'http://' + host + apiPath;
                var USER_LOGGED_IN = false;
                var calendarId = 1;

                $('#calendar').fullCalendar({
                    theme: true,
                    header: {
                        left: 'prev,next, today',
                        center: 'title',
                        right: ''
                    },
                    buttonText: {
                        today: 'Today'
                    },
                    firstDay: 1,
                    weekMode: 'variable',
                    aspectRatio: 1.6,
                    loading: function(bool) {
                        if (bool) $('#loading').show();
                        else $('#loading').hide();
                    },
                    events: fetchEvents,
                    eventRender: customEventRender,
                    eventAfterRender:  registerFancyBox,
                    dayClick: dayClicked
                });

                function fetchEvents(start, end, callback) {
                    var months = monthsBetween(start, end);
                    var events = [];

                    for (var i=1; i<months.length-1; i++) { //TODO: Remove -1
                        var ajaxUrl = BASE_URL + "calendar/" + calendarId + '/' + months[i].getFullYear() + "/" + (months[i].getMonth()+1);
                        //Perform AJAX call
                        function doIt() {
                            var last = i===(months.length-1-1); //TODO: Reset to .length-1 only
                            $.ajax({
                                url: ajaxUrl,
                                dataType: 'json',
                                success: function(result) {
                                    $.each(result, function( key, value) {
                                        events.push( {
                                            id:        value.id,
                                            allDay:     true,
                                            title:      value.description,
                                            start:      value.targetDate,
                                            end:        value.targetDate,
                                            content:    value.content,
                                            url:        value.originalUrl
                                        });
                                    });
                                    if (last) { //If this is the last ajax call then use callback
                                        callback(events);
                                    }
                                }
                            });
                        }
                        doIt();
                    }
                }

                function monthsBetween(start, end) {
                    var pointer = new Date(start.getTime());
                    pointer.setDate(28);
                    var months = [];
                    months.push(start);
                    while( pointer < end ) {
                        months.push( new Date(rollMonth(pointer).getTime()) );
                    }
                    return months;
                }

                function rollMonth(date) {
                    if (date.getMonth() == 11) {
                        date.setFullYear(date.getFullYear() + 1);
                        date.setMonth(0);
                    } else {
                        date.setMonth(date.getMonth() + 1);
                    }
                    return date;
                }

                function customEventRender(event, element) {
                    var imgUrl = BASE_URL + 'entry/' + event.id + '/content';
                    var thumbnailUrl = BASE_URL + 'entry/' + event.id + '/thumbnail';
                    var title = event.title;
                    if (event.url) {
                        title += ' <a target=\'_blank\' class=\'originalURL\' href=\'' + event.url + '\'>(original)</a>';
                    }
                    return $( '<div class="photoEntry" />' )
                    .html( '<a href="' + imgUrl + '" class="fancyImage" title="' + title + '"><img src="' + thumbnailUrl + '" /></a>' );
                }

                function registerFancyBox() {
                    $( 'a.fancyImage' ).fancybox({
                        type: 'image',
                        hideOnContentClick:     true,
                        transitionIn:           'elastic',
                        transitionOut:          'elastic'
                    });
                }

                function dayClicked( date, allDay, jsEvent, view ) {
                    if (!USER_LOGGED_IN) {
                        console.log('To add new pictures you need to be logged in');
                        return;
                    }
                    var actionUrl = BASE_URL + 'entry/' + calendarId + '/' + date.getFullYear() + '/' + (date.getMonth()+1) + '/' + date.getDate();
                    if (checkMobileSafari()) {
                        var iphoneWindow = window.open('iphone_upload.html?action=' + escape(actionUrl) + '&date=' + escape(date.toDateString()) + '&JSESSIONID=' + readCookie('JSESSIONID'));
                        return;
                    }
                    $( '#addEntry' ).resetForm();
                    $( '#dummy' ).attr('value', '...');
                    $( '#addEntry' ).attr('action', actionUrl);
                    $( '#showDate' ).attr('value',date.toDateString());
                    $( '#newPhotoEntryForm' ).dialog( "open" );
                }

                $( "#newPhotoEntryForm" ).dialog({
                    title: 'Add a new photo',
                    autoOpen: false,
                    show: 'fold',
                    hide: 'fold',
                    height: 500,
                    width: 550,
                    modal: true,
                    buttons: {
                        Add: function() {
                            $( '#addEntry' ).ajaxSubmit({
                                error: function( jqXHR ) {
                                    var message = "You aren't allowed to create photographs, please login as correct user and try again.";
                                    var title = "Not allowed";
                                    if (jqXHR.status != 401) {
                                        message = "Something went wrong (" + jqXHR.status + ") and the photograph wasn't saved";
                                        title = "Unknown error";
                                    }
                                    $( "<p>" + message + "</p>" ).dialog({
                                        title: title,
                                        modal: true,
                                        buttons: {
                                            Ok: function() {
                                                $( this ).dialog( "close" );
                                            }
                                        }
                                    });
                                    window.jq = jqXHR;
                                }
                             });
                            $( '#newPhotoEntryForm' ).dialog( 'close' );
                            setTimeout(function() {
                                $( '#calendar' ).fullCalendar( 'refetchEvents' );
                            }, 800);
                        },
                        Cancel: function() {
                            $( this ).dialog( 'close' );
                        }
                    }
                });
                var options = {
                  type: 'POST'
                };
                $( '#addEntry' ).ajaxForm(options);
                $( '#imageFile' ).change( function(e) {
                    $( '#dummy' ).attr('value', $(this).val());
                });

                //Check login status
                $.ajax({
                    url: BASE_URL + 'isLoggedIn',
                    dataType: 'json',
                    success: function( result ) {
                        if (result.loggedIn === 'true') {
                            $( '#login a')
                            .attr('href', BASE_URL + 'logout')
                            .html('Logout');
                            USER_LOGGED_IN = true;
                        } else {
                            $( '#login a')
                            .attr('href', BASE_URL + 'login')
                            .html('Login');
                            USER_LOGGED_IN = false;
                        }
                    }
                });
            });

            function checkMobileSafari() {
                var regCriteria = /(iPod|iPhone|iPad)/;
                if (regCriteria.test(navigator.userAgent)) {
                    return true;
                }
                return false;
            }

            function readCookie(name) {
                var nameEQ = name + "=";
                var ca = document.cookie.split(';');
                for (var i=0;i < ca.length;i++) {
                    var c = ca[i];
                    while (c.charAt(0)==' ') c = c.substring(1,c.length);
                    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
                }
                return null;
            }
        </script>
        <script type="text/javascript">
        
          var _gaq = _gaq || [];
          _gaq.push(['_setAccount', 'UA-7986127-3']);
          _gaq.push(['_trackPageview']);
        
          (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
          })();
        
        </script>
    </head>
    <body>
        <div id="login">
            <a href="">Login (undetected)</a>
        </div>
        <h1>104 photos - 2012</h1>
        <p>The goal of this calendar is to publish two photos per week throughout the year of 2012.
        One photo during the week and one from the weekend.
        <div id="calendar-content" >
            <span id="loading">Loading...</span>
            <div id="calendar" />
        </div>
        <div id="newPhotoEntryForm" title="Basic dialog">
          <p>Provide additional information and the picture you wish to upload below. External link can be used to link to an off-site resource like your own personal web site.</p>
          <form id="addEntry" action="" METHOD="POST">
              <fieldset>
                  <legend>Image Details</legend>
                  <label id="labelShowDate" for="showDate">Date:</label>
                  <input type="text" disabled="true" name="showDate" id="showDate" value="2011" />
                  <label for="description">Description:</label>
                  <input name="description" id="description" type="text" class="ui-widget-content ui-corner-al"/>
                  <label for="originalUrl" id="originalUrlLabel">External link:</label>
                  <input id="originalUrl" type="text" name="originalUrl" class="ui-widget-content ui-corner-al"/>
                  <div id="fileInput">
                    <label for="imageFile">Image:</label>
                    <input id="imageFile" type="file" name="content" class="ui-widget-content ui-corner-al"/>
                    <span id="dummyFile" >
                        <input type="text" name="dummy" id="dummy" value="..."/> <img src="images/filebrowse.png" />
                    </span>
                  </div>
              </fieldset>
          </form>
        </div>
    </body>
</html>
