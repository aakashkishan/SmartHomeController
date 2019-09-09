<!-- This Apache Freemarker script is similar to a Java Server Page. It renders Dynamic server-side contnent.
See -->
<#-- @ftlvariable name="" type="tartan.smarthome.views.SmartHomeView" -->
<html lang="us">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <title>Tartan House Control Panel</title>
    <script type="text/javascript">
        $(document).ready(function() {
            function getReqParams() {
                var name = $('#reportName').val();
                var date = $('#reportDate').val();
                if ($('#light').is(':checked'))
                    var light = true;
                else
                    var light = false;
                if ($('#HVAC').is(':checked'))
                    var HVAC = true;
                else
                    var HVAC = false;
                if ($('#energy').is(':checked'))
                    var energy = true;
                else
                    var energy = false;

                return "filename="+name+"&light="+light+"&HVAC="+HVAC+"&energy="+energy+"&date="+date;
            }

            $("#submit_btn").click(function(){
                var name = $('#reportName').val();
                var date = $('#reportDate').val();
                $('#light_hour_of_day').html("");
                $('#light_day_of_week').html("");
                $('#light_day_of_month').html("");
                $('#HVAC_hour_of_day').html("");
                $('#HVAC_day_of_week').html("");
                $('#HVAC_day_of_month').html("");
                $('#total_energy').html("");
                if(name === "" || date === "") {
                    $('#error').html("name or date can not be empty");
                }
                else {
                    $.ajax({
                        type: 'GET',
                        contentType: 'application/json',
                        url:  '/smarthome/report/query/api?'+getReqParams(),
                        success: function(a) {
                            if(a.success == true) {
                                report = a.data;
                                for(var i = 0;i < report.sections.length;i++) {
                                    if(report.sections[i].type == "light") {
                                        $('#light_hour_of_day').html("hours per day:"+report.sections[i].hours_of_day);
                                        $('#light_day_of_week').html("days per week:"+report.sections[i].day_of_week);
                                        $('#light_day_of_month').html("days per month:"+report.sections[i].day_of_month);
                                    }
                                    if(report.sections[i].type == "HVAC") {
                                        $('#HVAC_hour_of_day').html("hours of day:"+report.sections[i].hours_of_day);
                                        $('#HVAC_day_of_week').html("days per week:"+report.sections[i].day_of_week);
                                        $('#HVAC_day_of_month').html("days per month:"+report.sections[i].day_of_month);
                                    }
                                    if(report.sections[i].type == "energy") {
                                        $('#total_energy').html("total hours:"+report.sections[i].total);
                                    }
                                }
                            }
                            $('#error').html('<p>'+a.msg+'</p>');
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            alert("Could not save report");
                        },
                    });
                }
                $('#reportDate').val("");
                $('#reportName').val("");
                $('#light').prop('checked', false);
                $('#HVAC').prop('checked', false);
                $('#energy').prop('checked', false);
            });
        });
    </script>
</head>
<style>
    font-family: "Times New Roman", Times, serif;
    input[type=text], select {
        width: 100%;
        padding: 12px 20px;
        margin: 8px 0;
        display: inline-block;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }
    input[type=submit] {
        width: 100%;
        background-color: #4CAF50;
        color: white;
        padding: 14px 20px;
        margin: 8px 0;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    div {
        border-radius: 5px;
        background-color: #f2f2f2;
        padding: 20px;
    }
</style>
<div id="report_content">
    <fieldset><legend><h2>Reporting System</h2></legend>
        <p>
            <strong>select report date:</strong>
            <input type="date" id="reportDate" />
        </p>
        <p>
            <strong>select report type:</strong><br>
            Lighting<input type="checkbox" name="light" id="light"/><br>
            HVAC<input type="checkbox" name="HVAC" id="HVAC"/><br>
            Energy<input type="checkbox" name="energy" id="energy"/>
        </p>
        <p>
            <strong>Input report name:</strong>
            <input type="text" id="reportName" />
        </p>
        <button id="submit_btn" >submit</button>
        <br>
        <div id="error"></div>
        <p>Your report</p>
        <p>
            <strong>light</strong>
        <li id="light_hour_of_day"></li>
        <li id="light_day_of_week"></li>
        <li id="light_day_of_month"></li>
        </p>
        <p>
            <strong>HVAC</strong>
        <li id="HVAC_hour_of_day"></li>
        <li id="HVAC_day_of_week"></li>
        <li id="HVAC_day_of_month"></li>
        </p>
        <p>
            <strong>energy</strong>
        <li id="total_energy"></li>
        </p>
    </fieldset>
</div>
</body>
</html>