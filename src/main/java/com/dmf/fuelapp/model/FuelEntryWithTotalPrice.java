package com.dmf.fuelapp.model;

public class FuelEntryWithTotalPrice extends FuelEntry {
    private double totalPrice;

    public FuelEntryWithTotalPrice(FuelEntry decorated, double totalPrice) {
        setTotalPrice(totalPrice);
        this.fuelType = decorated.getFuelType();
        this.volume = decorated.getVolume();
        this.price = decorated.getPrice();
        this.date = decorated.getDate();
        this.driverId = decorated.getDriverId();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
