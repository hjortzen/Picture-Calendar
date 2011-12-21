<html>
    <head>
        <title>JQuery Calendar</title>
        <link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
        <link rel='stylesheet' type='text/css' href='css/calendar.css' />
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
        <script type='text/javascript' src='fullcalendar/fullcalendar.js'></script>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log('JQuery initialized!');
                $('#calendar').fullCalendar({
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
                    aspectRatio: 2.5,
                    loading: function(bool) {
                        if (bool) $('#loading').show();
                        else $('#loading').hide();
                    },
                    events: fetchEvents,
                    eventRender: customEventRender
                });

                function fetchEvents(start, end, callback) {
                    var baseUrl = "http://localhost:8080/Picture-Calendar/REST/";
                    var calendarId = 1;
                    var months = monthsBetween(start, end);
                    var events = [];

                    for (var i=0; i<months.length; i++) {
                        var ajaxUrl = baseUrl + "calendar/" + calendarId + '/' + months[i].getFullYear() + "/" + (months[i].getMonth()+1);
                        console.log('URL should be something like ' + ajaxUrl);
                        //Perform AJAX call
                        function doIt() {
                            var last = i===(months.length-1);
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
                                    console.log(events);
                                    if (last) { //If this is the last ajax call then use callback
                                        console.log('Calling callback!');
                                        callback(events);
                                    }
                                }
                            });
                        }
                        doIt();
                    }
                    return true;
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
                    return $( '<div class="photoEntry" />' ).html('<img src=\"' + event.url + '\"></img>');
                }
            });
        </script>
    </head>
    <body>
        <h1>This page is intended for Calendar POC</h1>
        <div id="calendar-content" >
            <div id="loading">Loading...</div>
            <div id="calendar" />
        </div>
    </body>
</html>
