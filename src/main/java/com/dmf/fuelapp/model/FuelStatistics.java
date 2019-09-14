package com.dmf.fuelapp.model;

public class FuelStatistics {
    private FuelEntry.FuelType fuelType;
    private double volume;
    private double averagePrice;
    private double totalPrice;

    public FuelStatistics(FuelEntry.FuelType fuelType, double volume, double averagePrice, double totalPrice) {
        setFuelType(fuelType);
        setVolume(volume);
        setAveragePrice(averagePrice);
        setTotalPrice(totalPrice);
    }

    public FuelEntry.FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelEntry.FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
