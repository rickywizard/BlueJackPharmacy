package com.ricky.bluejackpharmacy.model;

public class Medicine {
    private String name, manufacturer, image, description;
    private Integer price;

    public Medicine() {

    }

    public Medicine(String name, String manufacturer, Integer price, String image, String description) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
