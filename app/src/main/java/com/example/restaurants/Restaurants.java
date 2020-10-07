package com.example.restaurants;

import java.io.Serializable;

public class Restaurants implements Serializable {
    String imageViewRestaurant;
    String nameRestaurant;
    String addressRestaurant;
    String ratingRestaurant;
    String latitudeRestaurant;
    String longitudeRestaurant;
    String idsRestaurant;
    public Restaurants(String imageViewRestaurant, String nameRestaurant, String addressRestaurant,String idsRestaurant,String ratingRestaurant,String latitudeRestaurant,String longitudeRestaurant ) {
        this.imageViewRestaurant = imageViewRestaurant;
        this.nameRestaurant = nameRestaurant;
        this.addressRestaurant = addressRestaurant;
        this.idsRestaurant = idsRestaurant;
        this.ratingRestaurant = ratingRestaurant;
        this.latitudeRestaurant = latitudeRestaurant;
        this.longitudeRestaurant = longitudeRestaurant;
    }
    public String getRatingRestaurant() {
        return ratingRestaurant;
    }

    public void setRatingRestaurant(String ratingRestaurant) {
        this.ratingRestaurant = ratingRestaurant;
    }

    public String getLatitudeRestaurant() {
        return latitudeRestaurant;
    }

    public void setLatitudeRestaurant(String latitudeRestaurant) {
        this.latitudeRestaurant = latitudeRestaurant;
    }

    public String getLongitudeRestaurant() {
        return longitudeRestaurant;
    }

    public void setLongitudeRestaurant(String longitudeRestaurant) {
        this.longitudeRestaurant = longitudeRestaurant;
    }

    public String getIdsRestaurant() {
        return idsRestaurant;
    }

    public void setIdsRestaurant(String idsRestaurant) {
        this.idsRestaurant = idsRestaurant;
    }




    public String getImageViewRestaurant() {
        return imageViewRestaurant;
    }

    public void setImageViewRestaurant(String imageViewRestaurant) {
        this.imageViewRestaurant = imageViewRestaurant;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getAddressRestaurant() {
        return addressRestaurant;
    }

    public void setAddressRestaurant(String addressRestaurant) {
        this.addressRestaurant = addressRestaurant;
    }
}