public class Main {
    public static void main(String argvs[]) {
        ParkingLot pl = new ParkingLot(2);

        Car car1 = new Car("1234", "Microsoft");
        Motorcycle m1 = new Motorcycle("4016", "Microsoft");
        Car car2 = new Car("1609", "Google");
        Motorcycle m2 = new Motorcycle("1389", "Google");
        Car car3 = new Car("1809", "Microsoft");

        pl.parkVehicle(car1);
        pl.parkVehicle(m1);
        pl.parkVehicle(car2);

        pl.ComapnyParked("Microsoft");
        pl.ComapnyParked("Google");

        pl.leave(m1, 0); // Note that m1 is from Microsoft

        pl.ComapnyParked("Microsoft");

        pl.parkVehicle(m2);
        pl.parkVehicle(car3);
    }
}
