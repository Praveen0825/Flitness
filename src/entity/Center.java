package entity;

import enums.CenterName;

import java.util.ArrayList;
import java.util.List;

public class Center {
    private CenterName name;
    private List<Slot> slots;

    public Center(CenterName name) {
        this.name = name;
        this.slots = new ArrayList<>();
    }

    public CenterName getName() {
        return name;
    }

    public void setName(CenterName name) {
        this.name = name;
    }

    public void setSlots(List<Slot> workoutSlots) {
        this.slots = workoutSlots;
    }

    public List<Slot> getSlots() { // Add getter for workoutSlots
        return slots;
    }

    public void addSlot(Slot slot) { // Method to add workout slots to a center
        this.slots.add(slot);
    }
    @Override
    public String toString() {
        return name.toString();
    }

}
