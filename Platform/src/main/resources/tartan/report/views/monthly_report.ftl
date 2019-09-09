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
                var d = new Date();
                var n = d.getMonth()+1;
                var name = "MonthlyReport_"+n;
                var date = '2019-'+n+'-01';
                var light = ${light};
                var hvac = ${hvac};
                var energy = ${energy};
                return "filename="+name+"&light="+light+"&HVAC="+hvac+"&energy="+energy+"&date="+date;
            }


            $.ajax({
                type: 'GET',
                contentType: 'application/json',
                url:  '/smarthome/report/query/api?'+getReqParams(),
                success: function(a) {
                    if(a.success == true) {
                        report = a.data;
                        for(var i = 0;i < report.sections.length;i++) {
                            $('#report_date').html("report date:"+report.date);
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
                }
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
        <p>Your report</p>
        <p>
            <strong>report date</strong></>
        <li id="report_date"></li>
        </p>
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
        <div id="error"></div>
    </fieldset>
</div>
</body>
</html>