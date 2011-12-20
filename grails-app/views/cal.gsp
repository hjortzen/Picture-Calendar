<html>
    <head>
        <title>JQuery Calendar</title>
        <link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
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
                    events: function(start, end, callback) {
                        var baseUrl = "http://localhost:8080/Picture-Calendar/REST/";
                        var calendarId = 1;
                        console.log('Rendering from \t\t' + start + '\nto \t\t\t' + end + '\nand trying to call ajax');
                        //TODO: Figure out how many months to ask for (or if just for one specific day) and create URL accordingly
                        var ajaxUrl = baseUrl + "calendar/" + calendarId + "/2011/12";
                        console.log('URL should be something like ' + ajaxUrl);

                        //Perform AJAX call
                        $.ajax({
                            url: ajaxUrl,
                            dataType: 'json',
                            success: function(result) {
                                var events = [];
                                $.each(result, function( key, value) {
                                    events.push( {
                                        id:        value.id,
                                        title:      value.description,
                                        start:      value.targetDate,
                                        end:        value.targetDate,
                                        content:    value.content,
                                        url:        value.originalUrl
                                    });
                                });

                                callback(events);
                            }
                        });
                    }
                 });
                 console.log('Calendar initialized!');

            });
        </script>
    </head>
    <body>
        <h1>This page is intended for Calendar POC</h1>
        <div id="calendar" style="width: 800px" >
        </div>
    </body>
</html>
