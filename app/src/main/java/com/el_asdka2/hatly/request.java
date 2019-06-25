package com.el_asdka2.hatly;

public class request {
    private String description;
    private String price;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public request() {
    }


/* public request(String description, Double price, String location) {
        this.description = description;
        this.price = price;


    }
    */
}
