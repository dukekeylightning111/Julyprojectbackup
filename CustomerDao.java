package com.example.mysportfriends_school_project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Customer customer);

    @Delete
    void delete(Customer customer);

    @Update
    void update(Customer customer);

    @Query("DELETE FROM customers")
    Void deleteAllCustomers();

    //
    @Query("SELECT * FROM sportingactivityclass where customerId = :customerID")
    List<SportingActivityClass> getAllActivitiesByCustomerID(int customerID);



    @Query("SELECT * FROM customers WHERE id = :id")
    Customer getCustomerById(int id);

    @Query("SELECT * FROM customers WHERE id = :id")
    Customer getCustomerByIdNotLD(int id);

    @Query("SELECT id FROM customers WHERE name = :customerName")
    LiveData<Integer> getCustomerId(String customerName);

    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomersSync();

    @Query("SELECT * FROM customers WHERE name LIKE :customer_name")
    LiveData<Customer> getCustomersByName(String customer_name);

    @Query("SELECT * FROM customers WHERE name LIKE :customer_name")
    Customer getCustomersByNameNoLD(String customer_name);

    @Query("SELECT * FROM customers WHERE name LIKE :customer_name")
    Customer getCustomersByNameNoLiveData(String customer_name);

    @Query("SELECT COUNT(*) FROM customers WHERE id = :id")
    int countCustomersWithId(int id);

    @Query("SELECT COUNT(*) FROM CUSTOMERS WHERE name = :userName AND password = :password")
    int checkCustomerExists(String userName, String password);

    @Query("SELECT * FROM CUSTOMERS WHERE name = :userName AND password = :password")
    Customer getCustomerByNameAndPassword(String userName, String password);

    default boolean isValidCustomerId(int id) {
        int count = countCustomersWithId(id);
        return count == 1;
    }
}