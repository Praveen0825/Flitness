import helper.ServiceFactory;
import service.UserService;
import service.WorkoutService;

public class Main {
    public static void main(String[] args){
        UserService userService = ServiceFactory.getUserService();
        WorkoutService workoutService = ServiceFactory.getWorkoutService();

        try {
            workoutService.addWorkout("Koramangala", "Weights", 6, 7, 100, "01-09-2024", "30-09-2024");
            workoutService.addWorkout("Koramangala", "Cardio", 7, 8, 150, "01-09-2024", "30-09-2024");
            workoutService.addWorkout("Bellandur", "Weights", 18, 19, 100, "01-09-2024", "30-09-2024");
            workoutService.addWorkout("Bellandur", "Cardio", 18, 19, 20, "01-09-2024", "30-09-2024");

            userService.registerUser("Sourabh", "sourabh@email.com", "Hsr");
            userService.registerUser("Sourabh", "sourabh1@email.com", "Hsr");

            System.out.println("\nWorkout availability for Weights on 20-09-2024:");
            workoutService.viewSlotAvailability("Weights", "20-09-2024").forEach(System.out::println);


            //workoutService.bookSession("sourabh@email.com", "Koramangala", "Weights", 6, 7, "20-09-2024");
            //workoutService.bookSession("sourabh@email.com", "Koramangala", "Cardio", 7, 8, "20-09-2024");
            workoutService.bookSession("sourabh1@email.com", "Bellandur", "Cardio", 18, 19, "20-09-2024");
            workoutService.bookSession("sourabh@email.com", "Bellandur", "Cardio", 18, 19, "20-09-2024");

            System.out.println("\nAvailability for Weights on 20-09-2024 after booking:");
            workoutService.viewSlotAvailability("Weights", "20-09-2024").forEach(System.out::println);


            System.out.println("\nSchedule for Sourabh:");
            workoutService.viewSchedule("sourabh@email.com", "20-09-2024", null, null).forEach(System.out::println);

            System.out.println("\nWorkouts at Koramangala Center:");
            workoutService.getWorkoutsByCenter("Koramangala").forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}