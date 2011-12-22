<html>
    <head>
        <title>JQuery Calendar</title>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
        <script type='text/javascript' src='fullcalendar/fullcalendar.js'></script>
        <script type='text/javascript' src='fancybox/jquery.fancybox-1.3.4.pack.js'></script>

        <link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
        <link rel='stylesheet' type='text/css' href='fullcalendar/ui-lightness/jquery-ui-1.8.16.custom.css' media="screen"/>
        <link rel='stylesheet' type='text/css' href='css/calendar.css' />
        <link rel='stylesheet' type='text/css' href='fancybox/jquery.fancybox-1.3.4.css' media="screen"/>

        <script type="text/javascript">
            $(document).ready(function() {
                var BASE_URL = "http://localhost:8080/Picture-Calendar/REST/";

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
                    aspectRatio: 1.4,
                    loading: function(bool) {
                        if (bool) $('#loading').show();
                        else $('#loading').hide();
                    },
                    events: fetchEvents,
                    eventRender: customEventRender,
                    eventAfterRender:  registerFancyBox
                });

                function fetchEvents(start, end, callback) {
                    var calendarId = 1;
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
                    var title = event.title;
                    if (event.url) {
                        title += ' <a target=\'_blank\' class=\'originalURL\' href=\'' + event.url + '\'>(original)</a>';
                    }
                    return $( '<div class="photoEntry" />' )
                    .html( '<a href="' + imgUrl + '" class="fancyImage" title="' + title + '"><img src="' + imgUrl + '" /></a>' );
                }

                function registerFancyBox() {
                    $( 'a.fancyImage' ).fancybox({
                        type: 'image',
                        hideOnContentClick:     true,
                        transitionIn:           'elastic',
                        transitionOut:          'elastic'
                    });
                }
            });
        </script>
    </head>
    <body>
        <h1>Showing Calendar 1</h1>
        <div id="calendar-content" >
            <span id="loading">Loading...</span>
            <div id="calendar" />
        </div>
    </body>
</html>
