<!-- This Apache Freemarker script is similar to a Java Server Page. It renders Dynamic server-side contnent.
See -->
<#-- @ftlvariable name="" type="tartan.smarthome.views.SmartHomeView" -->
<html lang="us">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <title>Tartan House Control Panel</title>
    <script type="text/javascript">
        $(document).ready(function () {
            function getDate() {
                var date = $('#searchDate').val();

                return "date=" + date;
            }

            function getName() {
                var name = $('#searchName').val();

                return "filename=" + name;
            }

            $("#search_date_btn").click(function () {
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/smarthome/report/search/date_api?' + getDate(),
                    success: function (a) {
                        var reportHTML = "";
                        $('#report').html('');
                        if(a.success == true) {
                            for(d in a.data) {
                                report = a.data[d];
                                var keys = [];
                                reportHTML = reportHTML + '<table class="' + report.id +
                                    '"><tr><td>operation</td><td>id</td><td>name</td><td>date</td>';
                                for (var i = 0; i < report.sections.length; i++) {
                                    keys = keys.concat(Object.keys(report.sections[i]));
                                }
                                for (var j = 0; j < keys.length; j++) {

                                    reportHTML = reportHTML + "<td>" + keys[j] + "</td>";
                                }
                                reportHTML = reportHTML +
                                    '</tr><tr><td><button id="' + report.id +
                                    '" class="delete_btn">delete</button></td><td>' +
                                    report.id + '</td><td>' +
                                    report.name + '</td><td>' +
                                    report.date + '</td>';
                                for (var i = 0; i < report.sections.length; i++) {
                                    section_keys = Object.keys(report.sections[i]);
                                    for (var j = 0; j < section_keys.length; j++) {
                                        reportHTML = reportHTML + "<td>" + report.sections[i][section_keys[j]] + "</td>";
                                    }
                                }
                                reportHTML = reportHTML + "</tr></table><br>"
                            }
                            $('#report').append(reportHTML);
                            $('#searchName').val('');
                            $('#searchDate').val('');
                            $(".delete_btn").click(function () {
                                $.ajax({
                                    type: 'GET',
                                    contentType: 'application/json',
                                    url: '/smarthome/report/delete?report_id=' + this.id,
                                    context: this,
                                    success: function (data, json) {
                                        $('#error').html('<p>'+data.msg+'</p>');
                                        var table_id = this.id;
                                        $('.' + table_id).remove();
                                    },
                                    error: function (jqXHR, textStatus, errorThrown) {
                                        alert("Could not delete report");
                                    },
                                });
                            });
                        }
                        $('#error').html('<p>'+a.msg+'</p>');
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("Could not find report");
                    },
                });
            });

            $("#search_name_btn").click(function () {
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/smarthome/report/search/name_api?' + getName(),
                    success: function (a) {
                        var reportHTML = "";
                        $('#report').html('');
                        if(a.success == true) {
                            for(d in a.data) {
                                report = a.data[d];
                                var keys = [];
                                reportHTML = reportHTML + '<table class="' + report.id +
                                    '"><tr><td>operation</td><td>id</td><td>name</td><td>date</td>';
                                for (var i = 0; i < report.sections.length; i++) {
                                    keys = keys.concat(Object.keys(report.sections[i]));
                                }
                                for (var j = 0; j < keys.length; j++) {
                                    reportHTML = reportHTML + "<td>" + keys[j] + "</td>";
                                }
                                reportHTML = reportHTML +
                                    '</tr><tr><td><button id="' + report.id +
                                    '" class="delete_btn">delete</button></td><td>' +
                                    report.id + '</td><td>' +
                                    report.name + '</td><td>' +
                                    report.date + '</td>';
                                for (var i = 0; i < report.sections.length; i++) {
                                    section_keys = Object.keys(report.sections[i]);
                                    for (var j = 0; j < section_keys.length; j++) {
                                        reportHTML = reportHTML + "<td>" + report.sections[i][section_keys[j]] + "</td>";
                                    }
                                }
                                reportHTML = reportHTML + "</tr></table><br>"
                            }
                            $('#report').append(reportHTML);
                            $('#searchName').val('');
                            $('#searchDate').val('');
                            $(".delete_btn").click(function () {
                                $.ajax({
                                    type: 'GET',
                                    contentType: 'application/json',
                                    url: '/smarthome/report/delete?report_id=' + this.id,
                                    context: this,
                                    success: function (data, json) {
                                        $('#error').html('<p>'+data.msg+'</p>');
                                        var table_id = this.id;
                                        $('.' + table_id).remove();
                                    },
                                    error: function (jqXHR, textStatus, errorThrown) {
                                        alert("Could not delete report");
                                    },
                                });
                            });
                        };
                        $('#error').html('<p>'+a.msg+'</p>');
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("Could not find report");
                    },
                });
            });

// $("#see_btn").click(function(){
//     alert("test");
//     $.ajax({
//         type: 'GET',
//         contentType: 'application/json',
//         url:  '/report/select',
//         success: function(data) {
//             alert(data.sections[0].hours_of_day);
//             $('#light_hour_of_day').html(data.sections[0].hours_of_day);
//             $('#light_day_of_week').html(data.sections[0].day_of_week);
//             $('#light_day_of_month').html(data.sections[0].day_of_month);
//             $('#HVAC_hour_of_day').html(data.sections[1].hours_of_day);
//             $('#HVAC_day_of_week').html(data.sections[1].day_of_week);
//             $('#HVAC_day_of_month').html(data.sections[1].day_of_month);
//             $('#total_energy').html(data.sections[2].total);
//         },
//         error: function(jqXHR, textStatus, errorThrown) {
//             alert("Could not load report");
//         },
//     });
// });
        });
    </script>
</head>
<style>
    font-family:

    "Times New Roman"
    ,
    Times, serif

    ;
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

    td {
        width: 120px;
    }
</style>
<div id="report_content">
    <fieldset>
        <legend><h2>Reporting System</h2></legend>
        <div style="display: inline-block">
            <strong>Input search date:</strong>
            <input type="date" id="searchDate"/>
        </div>
        <button id="search_date_btn">search by date</button>
        <div style="display: inline-block">
            <strong>Input search name:</strong>
            <input type="text" id="searchName"/>
        </div>
        <button id="search_name_btn">search by name</button>
    </fieldset>
</div>
<div id="error"></div>
<p>Your report</p>
<div id="report"></div>
</body>
</html>
