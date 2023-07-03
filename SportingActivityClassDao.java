package com.example.mysportfriends_school_project;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SportingActivityClassDao {

    @Insert
    void insert(SportingActivityClass sportingActivity);

    @Update
    void update(SportingActivityClass sportingActivity);

    @Delete
    void delete(SportingActivityClass sportingActivity);
    @Query("SELECT * FROM SportingActivityClass WHERE customerId = :customerId")
    List<SportingActivityClass> getSportingActivitiesByUserId(int customerId);
    @Query("SELECT * FROM SportingActivityClass WHERE customerId = :customerId")
    LiveData<SportingActivityClass> getSportingActivitiesByUserIdNoList(int customerId);

    @Query("SELECT * FROM SportingActivityClass")
    LiveData<List<SportingActivityClass>> getAllSportingActivitiesLD();
    @Query("SELECT * FROM SportingActivityClass")
    List<SportingActivityClass> getAllSportingActivities();

    @Query("SELECT * FROM SportingActivityClass WHERE customerId = :customerId AND activityTitle = :title")
    LiveData<SportingActivityClass> getSportingActivityByCustomerAndTitle(int customerId, String title);

    @Query("SELECT * FROM SportingActivityClass WHERE id = :id")
    SportingActivityClass getSportingActivityById(int id);
    @Query("SELECT * FROM SportingActivityClass WHERE activityTitle = :title")
    SportingActivityClass getSportingActivityByTitle(String title);

    @Query("DELETE FROM SportingActivityClass")
    void deleteAllSportingActivities();
}