package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(User user);

    @Query("select * from user")
    List<User> getListUser();

    @Query("select * from user where name= :username")
    List<User> checkUser(String username);

    @Update
    void udpateUser (User user);

    @Delete
    void deleteUser(User user);
}
