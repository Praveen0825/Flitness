package exception;

public class SameTimeSlotAlreadyBooked extends Exception{
    public SameTimeSlotAlreadyBooked(String message) {
        super(message);
    }
}
