package com.example.mysportfriends_school_project;
import androidx.room.TypeConverter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Converters {
    @TypeConverter
    public static ArrayList<Customer> fromJsonString(String value) {
        Type listType = new TypeToken<ArrayList<Customer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromCustomer(Customer customer) {
        return customer == null ? null : customer.toString();
    }

    @TypeConverter
    public static String fromLatLng(LatLng latLng) {
        return new Gson().toJson(latLng);
    }

    @TypeConverter
    public static LatLng toLatLng(String value) {
        return new Gson().fromJson(value, LatLng.class);
    }

    @TypeConverter
    public static ArrayList<SportingActivityClass> fromString(String value) {
        Type listType = new TypeToken<ArrayList<SportingActivityClass>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }
    // convert from array list
    @TypeConverter
    public static String fromArrayList(ArrayList<SportingActivityClass> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
// convert from array list

    @TypeConverter
    public static String toJsonString(ArrayList<Customer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}