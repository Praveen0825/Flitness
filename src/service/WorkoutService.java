package service;

import entity.*;
import entity.Slot;
import enums.CenterName;
import enums.WorkoutType;
import exception.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class WorkoutService {

    private List<Slot> slots = new ArrayList<>();
    private Map<String, List<Booking>> bookings = new HashMap<>();
    private UserService userService;
    private Map<CenterName, Center> centers = new HashMap<>();

    public WorkoutService(UserService userService) {
        this.userService = userService;
        initializeCenters();
    }

    private void initializeCenters() {
        centers.put(CenterName.KORAMANGALA, new Center(CenterName.KORAMANGALA));
        centers.put(CenterName.BELLANDUR, new Center(CenterName.BELLANDUR));
    }

    private Center getCenterByName(CenterName centerName) throws CenterNotFoundException {
        Center center = centers.get(centerName);
        if (center == null) {
            throw new CenterNotFoundException("Center not found: " + centerName);
        }
        return center;
    }


    public void addWorkout(String centerNameStr, String workoutTypeStr, int startTimeHour, int endTimeHour, int capacity, String startDateStr, String endDateStr) throws Exception {
        try {
            CenterName centerNameEnum = CenterName.valueOf(centerNameStr.toUpperCase());
            Center center = getCenterByName(centerNameEnum);
            WorkoutType workoutType = WorkoutType.valueOf(workoutTypeStr.toUpperCase());
            LocalTime startTime = LocalTime.of(startTimeHour, 0);
            LocalTime endTime = LocalTime.of(endTimeHour, 0);
            LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                throw new InvalidInputException("Invalid time slot: Start time must be before end time.");
            }
            if (capacity <= 0) {
                throw new InvalidInputException("Capacity must be a positive number.");
            }
            if (startDate.isAfter(endDate)) {
                throw new InvalidInputException("Invalid date range: Start date must be before end date.");
            }

            Slot workoutSlot = new Slot(centerNameEnum, workoutType, startTime, endTime, capacity, startDate, endDate);
            center.addSlot(workoutSlot);
            slots.add(workoutSlot);
            System.out.println("Workout added successfully: " + workoutType + " at " + centerNameStr + " from " + startTime + " to " + endTime);

        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid center name or workout type.");
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date format.");
        } catch (CenterNotFoundException e) {
            throw e;
        }
    }

    public List<Slot> viewSlotAvailability(String workoutTypeStr, String dateStr) throws Exception {
        try {
            WorkoutType workoutType = WorkoutType.valueOf(workoutTypeStr.toUpperCase());
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            return slots.stream()
                    .filter(slot -> slot.getWorkoutType() == workoutType && slot.isWithinDateRange(date) && slot.getAvailableSeats() > 0)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid workout type.");
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date format.");
        }
    }

    public void bookSession(String userName, String centerNameStr, String workoutTypeStr, int startTimeHour, int endTimeHour, String dateStr) throws Exception {
        try {
            User user = userService.findUserByEmail(userName);
            CenterName centerNameEnum = CenterName.valueOf(centerNameStr.toUpperCase());
            WorkoutType workoutType = WorkoutType.valueOf(workoutTypeStr.toUpperCase());
            LocalTime startTime = LocalTime.of(startTimeHour, 0);
            LocalTime endTime = LocalTime.of(endTimeHour, 0);
            LocalDate bookingDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            if(bookings.containsKey(userName)){
                long bookedSlotCnt = bookings.get(userName)
                        .stream()
                        .filter(booking -> booking.getBookingDate().equals(bookingDate) &&
                                booking.getSlot().isInSameSlot(startTime, endTime)).count();
                if(bookedSlotCnt>0)
                    throw new SameTimeSlotAlreadyBooked("user already booked another slot in same time");
            }

            Slot workoutSlotToBook = slots.stream()
                    .filter(slot -> slot.getCenterName() == centerNameEnum &&
                            slot.getWorkoutType() == workoutType &&
                            slot.isWithinDateRange(bookingDate) &&
                            slot.isInSameSlot(startTime, endTime))
                    .findFirst()
                    .orElseThrow(() -> new SlotNotFoundException("Workout slot not found for given criteria."));

            if (workoutSlotToBook.getAvailableSeats() <= 0) {
                throw new SeatFullException("No seats available for this workout slot.");
            }

            workoutSlotToBook.bookSeat();
            Booking booking = new Booking(user, workoutSlotToBook, bookingDate);
            bookings.computeIfAbsent(user.getEmail(), k -> new ArrayList<>()).add(booking);

            System.out.println("Booking successful for " + userName + " at " + centerNameStr + " for " + workoutType + " on " + dateStr + " from " + startTime + " to " + endTime);

        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid center name or workout type.");
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date format. Date should be in dd-MM-yyyy format.");
        } catch (Exception e) {
            throw e;
        }
    }


    public List<Slot> viewSchedule(String userName, String dateStr, String centerNameFilter, String workoutTypeFilter) throws Exception {
        try {
            User user = userService.findUserByEmail(userName);
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            List<Slot> userSlots=new ArrayList<>();
            if(bookings.containsKey(userName)){
                userSlots = bookings.get(userName)
                        .stream().filter(booking->booking.getBookingDate().isEqual(date))
                        .map(Booking::getSlot)
                        .collect(Collectors.toList());
            }
            else{
                System.out.println("No slots are found for this user");
                return userSlots;
            }

            List<Slot> filteredSlots = userSlots.stream()
                    .filter(slot -> slot.isWithinDateRange(date) && slot.getAvailableSeats() > 0)
                    .collect(Collectors.toList());

            if (centerNameFilter != null && !centerNameFilter.trim().isEmpty()) {
                CenterName centerNameEnum = CenterName.valueOf(centerNameFilter.trim().toUpperCase());
                filteredSlots = filteredSlots.stream()
                        .filter(slot -> slot.getCenterName() == centerNameEnum)
                        .collect(Collectors.toList());
            }

            if (workoutTypeFilter != null && !workoutTypeFilter.trim().isEmpty()) {
                WorkoutType workoutType = WorkoutType.valueOf(workoutTypeFilter.trim().toUpperCase());
                filteredSlots = filteredSlots.stream()
                        .filter(slot -> slot.getWorkoutType() == workoutType)
                        .collect(Collectors.toList());
            }
            return filteredSlots;

        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date format.");
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid center name or workout type filter.");
        }
    }

    public List<Booking> viewUserSchedule(String userEmail) throws UserNotFoundException {
        userService.findUserByEmail(userEmail);
        return bookings.getOrDefault(userEmail, new ArrayList<>());
    }


    public List<Slot> getWorkoutsByCenter(String centerNameStr) throws Exception {
        CenterName centerNameEnum = CenterName.valueOf(centerNameStr.toUpperCase());
        Center center = getCenterByName(centerNameEnum);
        return center.getSlots();
    }
}
