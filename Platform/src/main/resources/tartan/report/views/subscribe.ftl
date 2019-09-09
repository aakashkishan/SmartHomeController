<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <title>Subscribe</title>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#subs_button").click(function(){
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url:  '/smarthome/subscribe/update',
                    data: updateState(),
                    success: function(response) {
                        if(response==true)
                            alert('User subscribed');
                        else
                            alert('Could not subscribe');
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Failure");
                    },
                });
            });
            function updateState() {

                if ($('#Light').is(':checked'))
                    var light = true;
                else
                    var light = false;
                if ($('#HVAC').is(':checked'))
                    var hvac = true;
                else
                    var hvac = false;
                if ($('#Energy').is(':checked'))
                    var energy = true;
                else
                    var energy = false;

                return "light="+light+"&hvac="+hvac+"&energy="+energy;
            }
        });
    </script>


</head>

<body>
<h1>Subscribe to monthly reports</h1>
<hr>
<div class="content">
    <table>
        <tr><td><input type="checkbox" id="HVAC" name="HVAC"> HVAC </td></tr>
        <tr><td><input type="checkbox" id="Light" name="Light"> Light</td></tr>
        <tr><td><input type="checkbox" id="Energy" name="Energy"> Energy</td></tr>
        <tr><td><input type="submit" id="subs_button"></td></tr>
    </table>
</div>
</body>
</html>