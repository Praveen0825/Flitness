package entity;

import enums.CenterName;
import enums.WorkoutType;
import exception.SeatFullException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Slot {
    private CenterName centerName;
    private WorkoutType workoutType;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private int availableSeats;

    public Slot(CenterName centerName, WorkoutType workoutType, LocalTime startTime, LocalTime endTime, int capacity, LocalDate startDate, LocalDate endDate) {
        this.centerName = centerName;
        this.workoutType = workoutType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.availableSeats = capacity;
    }

    public CenterName getCenterName() {
        return centerName;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeat() throws SeatFullException {
        if (availableSeats > 0) {
            availableSeats--;
        } else {
            throw new SeatFullException("No seats available for this workout slot.");
        }
    }

    public boolean isWithinDateRange(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isInSameSlot(LocalTime startTime, LocalTime endTime) {
        return this.startTime.equals(startTime) && this.endTime.equals(endTime);
    }
    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("Center: %s, Workout: %s, Time: %s-%s, Seats: %d",
                centerName, workoutType, startTime.format(timeFormatter), endTime.format(timeFormatter), availableSeats);
    }

}
