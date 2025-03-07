# Flitness

Design a backend system for a new enterprise application that Flipkart is launching, Flitness. Flipkart is partnering up with gyms across Bangalore to enter into the fitness space. For the Beta launch the requirements are as follows: 
There are only 2 centers for now - Koramangala and Bellandur. We plan to expand to multiple others if we get traction. 
Each center has n slots of an hour each. For eg the Bellandur center has only 6 slots - 3 in the morning of an hour each from 6am to 9am and similarly 3 in the evening from 6pm to 9pm. All the centers are open 7 days a week. 
Each slot at a center can have k possible workout variations - Weights, Cardio, Yoga, Swimming etc. There could be newer workouts added at center/slot level in the future. Adding new workout types should be easy and lead to minimal or no changes in application. 
The number of seats in each workout at each time-slot for a given center is fixed. 
For simplicity’s sake you can assume that the workout info will be entered by the Admin only once.


User should be able perform the following operations: 
Register onto the platform with [name, email & user-location(string)]
registerUser(name, email, location)


View the workouts availability/unavailability for


 a particular day
viewWorkoutSlotAvailability(“Weights”, “20-09-24”)


Book a workout for a user, if seats are available in that time slot at that center
bookSession(name, workoutLocation, workoutType, startTime, endTime, “20-09-24”)

View schedule for a day basis workout type/center as input. It should be easy to change the view pattern. If no workout type or center is provided, return full day’s schedule.
viewSchedule(user’s-name, “20-09-24”)


Admin should be able to perform following operation:
addWorkout(“Koramangala”, “Weights”, startTime:int, endTime:int, capacity:int, “01-09-24”, “30-09-24”)
Note: no need to register admin as a user, you can assume admin is created by default. No functional requirements apart from addWorkout is required for admin.

Bonus
Sort the viewWorkoutAvailability w.r.t  user’s location (closest first). Assume the distance matrix between different locations to (Bellandur, Koramangala) for now to be static.
Users should not be able to book more than W number of workout sessions in a week across all workoutTypes.
Guideline: 
● Time: 90mins 
● Write modular and clean code. 
● A driver program/main class/test case is needed to test out the code by the evaluator with multiple test cases. But do not spend too much time in the input parsing. Keep it as simple as possible. 
● Evaluation criteria: Demoable & functionally correct code, Code readability, Proper Entity modeling, Modularity & Extensibility, Separation of concerns, Abstractions. Use design patterns wherever applicable 
● You are not allowed to use any external databases like MySQL. Use only in memory data structures. 
● No need to create any UX 
● Please focus on the Bonus Feature only after ensuring the required features are complete and demoable. 



Sample

addWorkout(“Koramangala”, “Weights”, 6, 7, 100, “01-08-24”, “30-08-24”) 
addWorkout(“Koramangala”, “Cardio”, 7, 8, 150, “01-08-24”, “30-08-24) 
addWorkout(“Koramangala”, “Yoga”, 8, 9, 200, “01-08-24”, “30-08-24) 
addWorkout(“Koramangala”, “Swimming”, 8, 9, 200,“01-08-24”, “30-08-24) 

addWorkout(“Bellandur”, “Weights”, 18, 19, 100, “01-08-24”, “30-08-24) 
addWorkout(“Bellandur”, “Cardio”, 18, 19, 20, “01-08-24”, “30-08-24)

registerUser(“Sourabh”, “sourabh@email.com”, “Hsr”)

viewWorkoutSlotAvailability(“Weights”, “20-08-24”)
“Koramangala”, “Weights”, 6, 7, 100
“Bellandur”, “Weights”, 18, 19, 100

bookSession(“Sourabh”, “Koramangala”, “Weights”, 6, 7, “20-08-24”)

viewWorkoutSlotAvailability(“Weights”, “20-08-24”)
“Koramangala”, “Weights”, 6, 7, 99
“Bellandur”, “Weights”, 18, 19, 100

viewSchedule(“Sourabh”, “20-08-24”, “center: optional”, “workoutType: optional”)
“Koramangala”, “Weights”, 6, 7
