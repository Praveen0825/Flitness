package helper;

import service.UserService;
import service.WorkoutService;

public class ServiceFactory {

    private static UserService userService;
    private static WorkoutService workoutService;

    public static UserService getUserService(){
        if(userService == null){
            userService = new UserService();
        }

        return userService;
    }

    public static WorkoutService getWorkoutService(){
        if(workoutService == null){
            workoutService = new WorkoutService(getUserService());
        }

        return workoutService;
    }
}
