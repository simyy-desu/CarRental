public class Booking {
    private String location;
    private String pickDate;
    private String returnDate;
    private String pickTime;
    private String returnTime;
    private String carClass;
    private Car selectedCar; 
    private String paymentMethod;

    //constructor
    public Booking() {
    }

    public Booking(String location, String pickDate, String returnDate, String pickTime, String returnTime,String carClass) {
        this.location = location;
        this.pickDate = pickDate;
        this.returnDate = returnDate;
        this.pickTime = pickTime;
        this.returnTime = returnTime;
        this.carClass= carClass;
    }

    //getter
    public String getLocation() { return location; }
    public String getPickDate() { return pickDate; }
    public String getReturnDate() { return returnDate; }
    public String getPickTime() { return pickTime; }
    public String getReturnTime() { return returnTime; }
    public String getcarClass() { return carClass; }
    public Car getSelectedCar() { return selectedCar; }
    public String getPaymentMethod() { return paymentMethod; }

    //setter
    public void setLocation(String location) { this.location = location; }
    public void setPickDate(String pickDate) { this.pickDate = pickDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setPickTime(String pickTime) { this.pickTime = pickTime; }
    public void setReturnTime(String returnTime) { this.returnTime = returnTime; }
    public void setSelectedCar(Car car) { this.selectedCar = car; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}