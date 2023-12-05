import java.util.ArrayList;

abstract class Vehicle {
    protected ArrayList<ParkingSlot> parkingSlots = new ArrayList<ParkingSlot>();
    protected String licPlate;
    protected int slotsNeeded;
    protected VSize siz;
    protected String companyName;

    public int getSlotsNeeded() {
        return slotsNeeded;
    }

    public void parkInSlot(ParkingSlot s) {
        parkingSlots.add(s);
    }

    public VSize getSize() {
        return siz;
    }

    public void clearSlots() {
        for (int i = 0; i < parkingSlots.size(); i++) {
            parkingSlots.get(i).removeVehicle();
        }
        parkingSlots.clear();
    }

    public abstract boolean canFitInSlot(ParkingSlot spot);
}

class Car extends Vehicle {
    public Car(String licPlate, String companyName) {
        slotsNeeded = 1;
        siz = VSize.CarSize;
        this.licPlate = licPlate;
        this.companyName = companyName;
    }

    public boolean canFitInSlot(ParkingSlot spot) {
        return spot.getSize() == VSize.CarSize;
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String licPlate, String companyName) {
        slotsNeeded = 1;
        siz = VSize.Motorcycle;
        this.licPlate = licPlate;
        this.companyName = companyName;
    }

    public boolean canFitInSlot(ParkingSlot slot) {
        return slot.getSize() == VSize.CarSize || slot.getSize() == VSize.Motorcycle;
    }
}

class Lvl {
    private int floor;
    private ParkingSlot[] slots;
    private int availableSlots = 0;
    private static final int SLOT_PER_ROW = 10;

    public Lvl(int flr, int numberSlots) {
        floor = flr;
        availableSlots = numberSlots;
        slots = new ParkingSlot[numberSlots];
        int largeSlots = numberSlots / 4;
        int bikeSlots = numberSlots / 4;
        int compactSlots = numberSlots - largeSlots - bikeSlots;

        for (int j = 0; j < numberSlots; j++) {
            VSize siz = VSize.Motorcycle;
            if (j < largeSlots + compactSlots) {
                siz = VSize.CarSize;
            }
            int row = j / SLOT_PER_ROW;
            slots[j] = new ParkingSlot(this, row, j, siz);
        }
    }

    public int availableSlots() {
        return availableSlots;
    }

    public boolean parkVehicle(Vehicle vh) {
        if (availableSlots() < vh.getSlotsNeeded())
            return false;

        int slotNumber = findAvailableSlots(vh);
        if (slotNumber < 0)
            return false;
        System.out.print(", Slot Number " + slotNumber);
        return parkStartingAtSlot(slotNumber, vh);
    }

    private boolean parkStartingAtSlot(int num, Vehicle vh) {
        vh.clearSlots();
        boolean success = true;
        for (int j = num; j < num + vh.slotsNeeded; j++) {
            success &= slots[j].park(vh);
        }
        availableSlots = availableSlots - vh.slotsNeeded;
        return success;
    }

    private int findAvailableSlots(Vehicle vh) {
        int slotsNeeded = vh.getSlotsNeeded();
        int lastRow = -1;
        int slotsFound = 0;

        for (int j = 0; j < slots.length; j++) {
            ParkingSlot spot = slots[j];
            if (lastRow != slots[j].getLane()) {
                slotsFound = 0;
                lastRow = slots[j].getLane();
            }
            if (slots[j].canFitVehicle(vh)) {
                slotsFound = slotsFound + 1;
            } else {
                slotsFound = 0;
            }
            if (slotsFound == slotsNeeded) {
                if (vh.siz == VSize.CarSize)
                    System.out.print("It is a Car parked in ");
                else
                    System.out.print("It is a Motorcycle parked in ");
                System.out.print("Lane Number  " + lastRow);
                return j - (slotsNeeded - 1);
            }
        }
        return -1;
    }

    public void slotFreed() {
        availableSlots = availableSlots + 1;
        System.out.println("Available Slots in the current level :" + availableSlots);
    }
}

class ParkingSlot {
    private Vehicle vh;
    private VSize siz;
    private int lane;
    private int slotNumber;
    private Lvl l;

    public ParkingSlot(Lvl lvl, int r, int num, VSize vs) {
        l = lvl;
        lane = r;
        slotNumber = num;
        siz = vs;
    }

    public boolean isAvailable() {
        return vh == null;
    }

    public boolean canFitVehicle(Vehicle vh) {
        return isAvailable() && vh.canFitInSlot(this);
    }

    public boolean park(Vehicle vObj) {
        if (!canFitVehicle(vObj)) {
            return false;
        }

        vh = vObj;
        vh.parkInSlot(this);
        return true;
    }

    public int getLane() {
        return lane;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public VSize getSize() {
        return siz;
    }

    public void removeVehicle() {
        l.slotFreed();
        vh = null;
    }
}


