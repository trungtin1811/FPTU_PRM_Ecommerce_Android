package com.example.ecommerceproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecommerceproject.entities.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Account... accounts);

    @Update
    void updateUsers(Account... accounts);

    @Delete
    void delete(Account account);

    @Query("SELECT * FROM accounts")
    List<Account> getAll();

    @Query("SELECT * FROM accounts a WHERE a.id == :id")
    Account getById(String id);

    @Query("SELECT * FROM accounts a WHERE a.email == :email")
    Account getByEmail(String email);

}
