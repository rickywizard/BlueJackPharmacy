package com.ricky.bluejackpharmacy.model;

public class Transaction {
    private String date;
    private Integer trID, medicineID, userID, quantity;

    public Transaction() {

    }

    public Transaction(Integer trID, String date, Integer medicineID, Integer userID, Integer quantity) {
        this.trID = trID;
        this.date = date;
        this.medicineID = medicineID;
        this.userID = userID;
        this.quantity = quantity;
    }

    public Integer getTrID() {
        return trID;
    }

    public void setTrID(Integer trID) {
        this.trID = trID;
    }

    public String getDate() {
        return date;
    }

    public Integer getMedicineID() {
        return medicineID;
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMedicineID(Integer medicineID) {
        this.medicineID = medicineID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
