package com.project.boardgames.utilities.payments;

public class PaymentRequest {
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private double price;
    private String status;


}
