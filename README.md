# tartan

The Tartan SmartHome Platform
---
This Dropwizard appllication is a RESTful service to control the Tartan SmartHome platform. 
This depends on the IoTController library.

How to start the tartan application
---

In order for this to run properly, MySQL and one or more House Simulator (Hubs) have to be running on the 
system.

1. Inside the Platform folder, run `./gradlew shadowJar` to build your application
1. Start application with `./gradlew run`
1. To check that your application is running enter url `http://localhost:8088/smarthome/state/mse`


How to Launch Containers
---

1. **Jenkins** automatically builds and launches the containers after a commit to the **master** branch
2. In order to maunally launch the containers from Jenkins, run the `SmartHomeDocker` job
3. Containers can also be launched using `sudo docker-compose up —build` command from the 
	project root directory(`/home/foxtrot/.jenkins/workspace/SmartHomeDocker`)

How to Revert Containers
---

1. Run the `Revert_platform` job from jenkins to revert back to the previous successful build

New features provided to the user
---

1. The user can query the application to get the number of hours per day, number of days per week
	and month that the home lighting system and HVAC were running. The user can also query 
	the energy usage per month. 
2. The user can request these queries using the 'Query report' button on the 
	TartanHome Control Panel home page.
3. All reports generated by the user will be saved in the database and can be searched by the
	user using the 'Search report' button on the TartanHome Control Panel home page. 
4. The user can also subscribe to monthly reports using the 'Subscribe to monthly reports'
	button on the TartanHome Control Panel home page. The user can view monthly reports 
	using the 'View Monthly Report' button.

Changes to the code structure
--

1. The new reporting can be found in the following locations:
	a. <Project dir>/Platform/src/main/java/tartan/report and 
	b. <Project dir>/Platform/src/main/resources/tartan/report.views
2. Two new tables were added to the database:
	a. Report
	b. Subscription
3. 4 checkers were used during the development of the system:
	a. Nullness Checker
	b. Tainting Checker 
	c. Initialization Checker
	d. Format String Checker