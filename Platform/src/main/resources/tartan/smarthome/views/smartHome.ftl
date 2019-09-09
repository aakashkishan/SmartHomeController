<!-- This Apache Freemarker script is similar to a Java Server Page. It renders Dynamic server-side contnent.
See -->
<#-- @ftlvariable name="" type="tartan.smarthome.views.SmartHomeView" -->
<html lang="us">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <title>Tartan House Control Panel</title>
    <script type="text/javascript">
    
        $(document).ready(function() {

            $("#predbtn").click(function(){
                var dd = new Date();
                dd.setDate(dd.getDate() + 1);
                var year = dd.getFullYear();
                var month  = dd.getMonth();
                var day = dd.getDate();
                var user_id = ${tartanUser.getIDString()};
//            var user_id = 3440;

                var date = new Date(document.getElementById("predDate").value);
                var mode = document.getElementById("predMode").value;

                day = date.getDate()+1;
                month = date.getMonth()+1;
                year = date.getFullYear();

                var xml = new XMLHttpRequest();
                xml.onreadystatechange = function() {
                    if (this.status == 200) {
                        var result = this.responseText;
                        document.getElementById("prediction_time").innerHTML= result;
                    }
                };
                var apiurl = "http://17654-foxtrot.isri.cmu.edu:5000/pred?user_id=" + user_id + "&type=" + mode +"&year=" + year + "&month=" + month + "&day=" + day;
                console.log(apiurl);
                xml.open("GET", apiurl, true);
                xml.send();
            });

             $("#refresh_button").click(function() {
                window.location.reload();
             });

            function updateState() {
                var door = $('#door').val();
                var light = $('#light').val();
                var alarmDelay = $('#alarmDelay').val();
                var targetTemp = $('#targetTemp').val();
                var humidifier = $('#humidifier').val();
                var armAlarm = $('#armAlarm').val();
                var passcode = $('#alarmPasscode').val();
                var hvacMode = $('#hvacMode').val();
                var lockState = $('#lockState').val();
                var registerReceived = $('#registerReceived').val();
                var arriveState = $('#arriveState').val();
                var intruderState = $('#intruderState').val();
                var allClear = $('#allClear').val();

                return JSON.stringify({"door":door,"light":light,"targetTemp":targetTemp,"humidifier":humidifier,"alarmArmed":armAlarm,"alarmDelay":alarmDelay,"alarmPasscode":passcode});
            }

            $("#update_button").click(function(){
                  $.ajax({
                    type: 'POST',
                    contentType: 'application/json',
                    url:  '/smarthome/update/${tartanHome.name}',
                    data: updateState(),
                    success: function(data) {
                        location.reload(true);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not update ${tartanHome.name}");
                    },
                });

            });

            $("#alarm_button").click(function(){

                  $.ajax({
                    type: 'POST',
                    contentType: 'application/json',
                    url:  '/smarthome/update/${tartanHome.name}',
                    data: updateState(),
                    success: function(data) {
                        location.reload(true);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not deactivate alarm for ${tartanHome.name}");
                    },
                });
            });

            $("#report1_button").click(function(){

                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url:  '/smarthome/report1',
                    success: function() {
                        location.href="/smarthome/report1";
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not load report");
                    },
                });
            });

            $("#report2_button").click(function(){

                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url:  '/smarthome/report2',
                    success: function() {
                        location.href="/smarthome/report2";
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not load report");
                    },
                });
            });

            $("#subscribe_button").click(function(){
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url:  '/smarthome/subscribe',
                    success: function() {
                        location.href="/smarthome/subscribe";
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not load subscription form");
                    },
                });
            });
            $("#view_monthly_report").click(function(){
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url:  '/smarthome/monthly_report',
                    success: function() {
                        location.href="/smarthome/monthly_report";
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not load monthly report");
                    },
                });
            });
            $("#query_report").click(function(){
                $.ajax({
                    type: 'GET',
                    contentType: 'text/html',
                    url:  '/smarthome/report/query',
                    success: function() {
                        location.href="/smarthome/report/query";
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not save report");
                    },
                });
            });
            $("#search_report").click(function(){
                $.ajax({
                    type: 'GET',
                    contentType: 'text/html',
                    url:  '/smarthome/report/search',
                    success: function() {
                        location.href="/smarthome/report/search";
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not search report");
                    },
                });
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
<div id="${tartanHome.name}_div">
<fieldset id="${tartanHome.name}_control"><legend><h2>House: ${tartanHome.name}@${tartanHome.address}</h2></legend>
    <button id="subscribe_button">Subscribe to monthly reports</button>
    <button id="view_monthly_report">View monthly report</button>
    <button id="query_report">Query report</button>
    <button id="search_report">Search report</button>

    <h3> Input details for prediction </h3>
    <p>
    	<strong> Input mode:</strong> 
    	<select name="mode_predict" id="predMode">
            <option value="day">day</option>
            <option value="night">night</option>
        </select>
    </p>
    <p>
    	<strong> Input date:</strong>
    	<input id="predDate" type="date"/>
    </p>
    <p>
        <input type="button" id="predbtn" value = "predict" />
    </p>
  	<p>
    	<h3> Prediction Time </h3>
    	<div id="prediction_time"></div>
    </p>
    <h3>HVAC</h3>
    <p>
        <strong>Temperature: <font color="blue">${tartanHome.temperature} F </font></strong>
    </p>
    <strong>
        <label for="targetTemp">Set Temperature: </label>
        <input id="targetTemp" type="number" value="${tartanHome.targetTemp}" min="50" max="85" /> degrees F
    </strong>
    <p>
        <strong>Humidity: <font color="blue">${tartanHome.humidity}% </font></strong>
    </p>
    <p>
        <strong><label for="humidifier">Humidifier:</label></strong>
        <select name="slider-flip-m" id="humidifier" data-role="slider" data-mini="true">
            <option value="off">off</option>
            <option value="on" <#if tartanHome.humidifier=="on">selected="true"</#if> >on</option>
        </select>
    </p>
    <p>
        <strong>Mode:
        <#if tartanHome.hvacMode == "heat">
            <font color="red">Heat</font>
        <#else>
            <font color="blue">Cool</font>
        </#if>
        </strong>
    </p>
    <P>
        <strong>HVAC is ${tartanHome.hvacState}</strong>
    </P>
    <hr>

    <h3>Proximity</h3>
    <p>
        <strong>House is <font color="blue"> ${tartanHome.proximity}</font></strong>
    </p>
    <p>
        <strong>Door state:</strong>
        <select name="slider-flip-m" id="door" data-role="slider" data-mini="true">
        <#if tartanHome.door == "closed">
            <option value="closed" selected="true">closed</option>
            <option value="open">open</option>
        <#else>
            <option value="closed" >closed</option>
            <option value="open" selected="true">open</option>
        </#if>
        </select>
    </p>
    <p>
        <strong>Light state:</strong>
        <select name="slider-flip-m" id="light" data-role="slider" data-mini="true">
            <#if tartanHome.light == "on">
                <option value="on" selected="true">on</option>
                <option value="off">off</option>
            <#else>
                <option value="on">on</option>
                <option value="off" selected="true">off</option>
            </#if>
        </select>
    </p>

    <p>
        <strong>Lock state:</strong>
        <select name="slider-flip-m" id="lock" data-role="slider" data-mini="true">
            <#if tartanHome.lockState == "on">
                <option value="on" selected="true">on</option>
                <option value="off">off</option>
            <#else>
                <option value="on">on</option>
                <option value="off" selected="true">off</option>
            </#if>
        </select>
    </p>
    <p>
        <strong>Night lock:</strong>

    </p>
    <strong>
        <label for="startTime">Set Start Time: </label>
        <input id="startTime" type="time" value="${tartanHome.startTime}"/>
    </strong>
    <strong>
        <label for="endTime">Set End Time: </label>
        <input id="endTime" type="time" value="${tartanHome.endTime}"/>
    </strong>
    <hr>
    <h3>Alarm System</h3>
    <p>
        <strong>Arriving signal is <font color="blue"> ${tartanHome.arriveState}</font></strong>
    </p>
    <p>
        <strong>Registered signal is <font color="blue"> ${tartanHome.registerReceived}</font></strong>
    </p>
    <p>
        <strong>Intruder state is <font color="blue"> ${tartanHome.intruderState}</font></strong>
    </p>
 

    <p>
        <strong>All clear state is <font color="blue"> ${tartanHome.allClear}</font></strong>
    </p> 
    <p>
        <strong>Alarm status:</strong>
        <select name="slider-flip-m" id="armAlarm" data-role="slider" data-mini="true">
            <#if tartanHome.alarmArmed=="armed">
                <option value="armed" selected="true">armed</option>
                <option value="disarmed">disarmed</option>
            <#else>
                <option value="armed">armed</option>
                <option value="disarmed" selected="true">disarmed</option>
            </#if>
        </select>
    </p>
    <p>
        <#if tartanHome.alarmActive != "active">
        <strong><font color="green">Alarm Off</font></strong>
        <#else>
        <strong><font color="red">Alarm Active!</font></strong>
        <label for="alarmPasscode">Alarm passcode: </label><input id="alarmPasscode" type="text" />
        <button id="alarm_button">Stop alarm</button>
    </#if>
    </p>
    <p>
        <strong>
            Alarm delay: <input id="alarmDelay" type="number" value="${tartanHome.alarmDelay}" /> seconds
        </strong>
    </p>
    <hr>
    <h3> Event log</h3>
    <textarea id="log" rows="15" cols="150">
    <#list tartanHome.eventLog as i>
    ${i}
    </#list>
    </textarea>
    <p>
        <button id="update_button">Update house state</button> <button id="refresh_button">Refresh house state</button>
    </p>
</fieldset>
</div>
</body>
</html>
