# SynapFlow
simple productivity timer and event planner app. To study via the Pomodoro Technique, studying for 25 minutes, taking a break for 5. While also creating events to keep track of, and to be alerted on to make sure that you are staying on track.

**Abstract** 
	The purpose of this project is to create a simple productivity timer and event planner app. To study via the Pomodoro Technique, studying for 25 minutes, taking a break for 5. While also creating events to keep track of and to be alerted on to make sure that you are staying on track. We believe there is a benefit for an app like this one, removing extra distractions and creating exactly what is needed to keep you motivated and on track for success. 

**Introduction**
	SynapFlow is an app that is built to be a simple way of keeping track of events like exams, graduation, or scheduling time to sit down and study a certain subject. Giving you an interactive calendar to view your tasks to make sure you can plan ahead. The app is meant to help you stay productive and on top of your schedule, while also giving you a tool to help you sit down and study for a period of time, while giving you breaks. 
The app also rewards you with trophies to help feed a sense of accomplishment for each completed study session. The app also gives you the capability of viewing some analytics for your last week and last month's amount of study sessions, in the form of a line chart for the last week, and a bar chart for the last month, binning each group as a week in the last four. 

**Description of App** 

SynapFlow is a time management app that allows you to track your study times and keep track of important events like exams, birthdays, or meetings. Features include: 

Timer: Primarily structured around the Pomodoro Method (25 minutes of focused work, 5 minute break intervals). Users have the option to pause, resume, or completely stop a timed session.
Analytics: Tracks users' study sessions progress by tracking completed number of study sessions over the last few days, and understanding your study sessions over the last month which are visually displayed through user friendly chart graphics.
Tasks: Creating event tasks with short descriptions and date/timeframes to be alerted when you need to be working on something.
Rewards: Users can acquire achievement badges that serve as reminders of progress and accomplishments which instill a sense of motivation to study consistently and log more sessions for greater milestones.
Calendar: Clean user friendly calendar which serves as a hub to display all previous and newly created tasks which can be marked as complete or deleted. Dates within the calendar containing any tasks are represented by a blue dot visual so that users can identify which days contain tasks to be completed.
Discussion 

**Challenges**
Notifications and AlarmManager: Getting notifications to work with the Android AlarmManager class proved to be a challenge as the way to do this has changed many times over the development of Android and its many different API versions. Getting notifications themselves to show up was not a problem or a challenge but I had difficulty getting the AlarmManager and BroadcastReceiver to work properly. It ended up being all due to a single line of code that was using the wrong function call. Originally I was trying to get it to work with the AlarmManger.SetExact() function, which was the function that I most commonly saw in the documentation to do this sort of thing. I then eventually stumbled across the setExactAndAllowWhileIdle() function which solved all of my problems after trying to get it to work for a couple of hours. It seems to always be the small things that cause all of the problems.

Firestore Implementation: Firestore’s implementation was a struggle to grasp. The concept of adding, deleting, updating properly was a bit complicated to work with at first. The first hurdle we encountered was properly getting the data out of Firestore and making sure each fragment properly had access to them. We eventually landed on a global fsdb variable that could store lists inside of the class implementation, so that as things processed it could request firestore to update the lists and maps. The next issue we encountered was properly updating the data, and storing it. We eventually found out that you could store new collections inside documents, and that was the game changer we needed to make our implementation work. 

**Innovation**
	The innovation of our app is the simplicity in how it brings together useful features that a user would need to effectively study, track progress through logged sessions, as well as store important tasks which can alert the user through notifications based on priority and chosen target date/times. All listed features of our comprehensive study tool play a crucial role in keeping the user on track through the promotion of productivity, motivation, and organization. Although this is a powerful tool to enhance a user's study experience or desired learning goals, the overall design was developed with simplicity in mind so that anyone can easily pick up and use our application without facing any technological barriers. 

**Merit**
Interactive: The app engages you in a variety of activities, such as studying, analytics, and tracking tasks. Analytics serve as an effective visual tool that user’s can understand by seeing the progression of logged study sessions over varying periods of time. Providing users the ability to create tasks which are automatically allocated to the calendar allows greater flexibility to easily set and revisit. To ensure users do not miss or forget to manage a task (complete/delete), our app provides automatic notifications to act as helpful reminders to the user. 
Simple: There isn’t a lot of interface to bog down the application, so that way it's lightweight in resource utilization as well as being straight to the point of the user’s experience. Our vision was to create a painless to use study app that still managed to be robust in providing the user with functional features that would improve their overall productivity and organization. 
Useful: The app makes studying enjoyable and motivates users to be consistent with the rewards system in place as well as review their progress through the analytics graphics. Provides users a timer that is designed to operate based on the most effective studying technique known (Pomodoro) to provide a structured study session. Task creation is simple to use and is automatically stored and organized through the calendar making it easy to manage tasks and deadlines effectively. 


**List of Implemented Concept**

1.Navigation 
The navigation bar exists inside our home activity, where it also displays the current fragment. The bar allows you to access the home timer, the analytics page, task create page, trophies, and the calendar. 

2.Fragments 
Near the entirety of our application is ran inside of fragments. The only page that is not, is the main login page. We utilized Fragments for the timer, analytics, task, trophy, and calendar pages. Both plots as well are fragments inside of the analytics fragment.

3.Firebase
Firebase was the core of our project's information. The first portion we implemented was the Google Authentication for the users to login with. This decision was so that we could easily deal with accounts and login systems and not have to worry about passwords and lost accounts. We then used the token UID generated as the users ID for the firestore database.

In Firestore we created a database, using the UID as keys for each user, we then saved some general information, like a count of how many completed study sessions the user has completed, a long with a map of dates, with a string of the time they completed a task, so that this information could be later accessed. We didn’t fully utilize the times finished, we could have instead used a map with an int counter, but this was a part of something we attempted with the analytics.  
	 
4.API 
AndroidChart API: We discovered Android chart as an alternative to AndroidPlot. The formatting, code setup, and design was significantly cleaner and we decided we wanted the format of AndroidChart for our design.

Material Progress Bar: The progress bar ‘wheel’ was something we found while researching how to do the timer, via the walkthrough I began with. The wheel for the timer counts down/gradually depletes as the time counts down and ultimately disappears on completion. Functional visual element that provides users a quick and easy way to monitor the time remaining during a study session.

Calendar API: The calendar API was a github repository we found that allowed us to integrate date information to display for the user. It also was very simple for us to add tasks to it due to it being simple enough to take in a date as a key, so that we could display the information in the recycler view.
	
 5. Notification
AddTaskFragment: After a task is created in the add task fragment we create a pending intent to the NotificationBroadcastReciever (see below) with the task details. We then used the Android Alarm Manager class to set an alarm at the date and time set in the fragment in the OS to trigger that broadcast to the NotificationBroadcastReciever.

NotificationBroadcastReciever: When this class receives a broadcast from the OS it builds a new notification with our channel and task details. Once that is done it will then send the notification to the user that they have a task to complete.

 6. Recycler view 
Recycler view was used as a way to display tasks inside the calendar. Inside the calendar at the bottom, we designed it so that it displays the name, description time and if the task is completed. That way the user can keep track of the information the had described the task with. Along with being able to complete the task and delete the task from that window as well.

 7. Material Design 
Material Design was a trickier implementation for us. We implemented our own image in the home screen of our app. We also utilized trophy images, along with icons for the navigation bar. We also changed the default color and appearance of the app, attempting to make it look more completed.

 8. Permission
Permissions were implemented to allow for both notifications and we also implemented it to access the users location.  The notifications need the permissions to be able to run properly. While implementing the extra logic for the location was to make sure we could do as much as we can with the functionality.

 9. LiveData
LiveData is used to store all of the variable information for the timer data. We utilized LiveData so that we can store the timer and state of the project without fear of losing the data while we access other portions of the app. Once you return the timer fragment, the app will reinitialize the timer and either continue it, continue to have it paused, or finish the timer and run the code related to the timer finishing. 

 10. Unit Testing
Plot Unit Testing: For the plots, we created some unit tests to make sure the lines and plots were created as expected based on the map that is stored inside the function.

Task Unit Testing: For the tasks, we created some tests to make sure that each of the variables within the Task object are updated and stored correctly.

Calendar Unit Testing: For fun dayBinderIsCalledOnDayChanged(), this test checks to make sure that the dayBinder is working properly to update any changes made to a selected day. For fun programmaticScrollWorksAsExpected(), this test checks to confirm that the scrollToMonth correctly scrolls to the specified month.

**Limitations**
We had a few complications throughout the project. During the final few weeks of the project, a member had some medical problems so that they could no longer fully contribute to the project. Leaving us to have to make up a quarter of the work in the last week. Brad and Sean focused on taking over this aspect of the code and managed to get it complete in a few days. In doing so we were unable to make the Work Manager work within our scope, and ultimately had to drop it. The Work Manager runs tasks in the background, but you cannot retrieve the tasks once they start. So you can’t run the timer in the background, and then retrieve it once you reopen the app. So we had to scrap this idea. We also had trouble implementing LiveData, we eventually realized we were creating a new LiveData object every time we accessed the fragment, rather than using a persistent one. We spent at least ten to twenty hours over two days researching both LiveData and Work Manager attempting to fully understand what they’re capable of before we were able to get our proper implementation.
Our team's schedule was not aligned at all which contributed to a complicated experience with time management and communication. A few of us work during the week, others were overloaded full time students that worked the weekend, and all of us had other projects outside of this one with similar issues to the one I discussed in the last paragraph. We managed to reconcile these complexities by employing the use of a group trello board as guidance for project checkpoints/deadlines. Additionally, multiple remote voice chat meetings were held after designating a timeframe for when we all had a half hour to sit down, look at the app and discuss in detail scope of work to be divided, progress over time, and potential blockers encountered. Sean tried to solve minor problems, since he had the least amount of classes this semester so that we didn’t have to wait for others to be free. And we all spoke regularly in our group discord server to share any issues we found, or let people know when we can hop in to solve a problem. So even though we had very different schedules, we managed to make it work and complete the project on time. 

**Conclusion**
	We believe our app is a great solution to those who need something that can keep them busy studying and keep track of their tasks in one place. Using the Pomodoro Method is proven to be a strong method for studying. Viewing your completed sessions per day is valuable to keep you motivated. Tasks help you stay organized, and you can plan ahead your tasks. Rewards help you stay motivated and give you that much more to work towards. Having your schedule mapped out on a calendar is a great way to visually know what is going on. 
	In conclusion this app allowed us to work as a team and implement an app to our expectations. We ran into issues and problems that we did not anticipate in the beginning of the project, and that caused us to scrap some portions entirely, like the work manager. We quickly adapted as a team to find a new solution and were able to bring our idea to life. We were able to avoid scope creep, and managed to keep every expectation under manageable deadlines. Our team dynamic was great throughout the entire project, and we learned a lot of lessons as a group that we plan on taking with us into our new careers as Software Engineers.
