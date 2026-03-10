// Car.java
// Matt Ceran
// 3/8/26
// car object class for csv data

public class Car {

    private String carId;
    private String brand;
    private String model;
    private int year;
    private String fuelType;
    private String color;
    private double mileageKmpl;

    // contructor
    public Car(String carId, String brand, String model, int year,
               String fuelType, String color, double mileageKmpl){
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.color = color;
        this.mileageKmpl = mileageKmpl;
    }

    // getters
    public String getCarId(){ return carId; }
    public String getBrand(){ return brand; }
    public String getModel(){ return model; }
    public int getYear(){ return year; }
    public String getFuelType(){ return fuelType; }
    public String getColor(){ return color; }
    public double getMileageKmpl(){ return mileageKmpl; }

    // tostring prints one line with all the car info
    public String toString(){
        return carId + " | " + brand + " | " + model + " | " + year
            + " | " + fuelType + " | " + color + " | " + mileageKmpl + " kmpl";
    }
}