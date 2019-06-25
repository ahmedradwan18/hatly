package com.el_asdka2.hatly;

public class request_delivery {
    private String description;
    private String price;
    private String latitude;
    private String longitude;
   // private String customer_name;
    private String id;

   /* public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }*/

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    private String fare;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public request_delivery() {

    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
