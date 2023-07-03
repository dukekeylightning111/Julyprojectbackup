package com.example.mysportfriends_school_project;


import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

class CustomerTypeConvereter {
    // converting to string
    @TypeConverter
    public static Customer fromString(String value) {
        return new Customer(value);
    }

    @TypeConverter
    public static String customerToString(Customer customer) {
        return customer.toString();
    }
    @TypeConverter
    public static String toString(LatLng latLng) {
        return new Gson().toJson(latLng);
    }

}