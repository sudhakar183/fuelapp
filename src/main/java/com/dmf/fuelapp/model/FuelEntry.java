package com.dmf.fuelapp.model;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@Entity
public class FuelEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public enum FuelType { A95, A98, D };
    @ApiModelProperty(required = true)
    FuelType fuelType;
    @ApiModelProperty(required = true)
    double volume;
    @ApiModelProperty(required = true)
    double price;
    @ApiModelProperty(required = true)
    LocalDate date;
    @ApiModelProperty(required = true)
    int driverId;

    public FuelEntry() { }

    public FuelEntry (FuelType fuelType, String date, double volume, double price, int driverId) {
        setFuelType(fuelType);
        setVolume(volume);
        setPrice(price);
        setDriverId(driverId);
        setDate(date);
    }

    public double getVolume() {
        return volume;
    }

    private void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    private void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM.dd.yyyy"));
    }

    public int getDriverId() {
        return driverId;
    }

    private void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    private void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Month retrieveMonth () {
        return date.getMonth();
    }
}
