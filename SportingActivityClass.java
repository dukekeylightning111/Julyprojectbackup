package com.example.mysportfriends_school_project;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

@Entity(tableName = "SportingActivityClass")
@TypeConverters(CustomerTypeConvereter.class)
public class SportingActivityClass implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "customer")
    private Customer customer;
    @ColumnInfo(name= "customerId")
    private Integer customerId;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "activityTitle")
    private String title;


    public SportingActivityClass(String title, String category, Customer customer, String time, String description,String location,String date) {
        this.title = title;
        this.category = category;
        this.customer = customer;
        this.customerId=customer.getId();
        this.time = time;
        this.description = description;
        this.location = location;
        this.date = date;
    }



    public Integer getCustomerId(){
        return customerId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public boolean isDeletable() {
        Customer currentUser = this.customer;
        return currentUser != null && customer.getId() == (currentUser.getId());
    }


    public int getId() {
        return id;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDescription(String description){
        this.description= description;
    }
    public String getDescription() {
        return description;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    } public String getDate() {
        return date;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public void setTitle(String title) {
        this.title = title;
    }



}