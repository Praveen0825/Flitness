package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Booking {
    private User user;
    private Slot slot;
    private LocalDate bookingDate;

    public Booking(User user, Slot slot, LocalDate bookingDate) {
        this.user = user;
        this.slot = slot;
        this.bookingDate = bookingDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format("User: %s, %s, Date: %s", user.getName(), slot, bookingDate.format(dateFormatter));
    }
}
