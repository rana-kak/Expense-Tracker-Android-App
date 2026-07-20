package com.example.expensestracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ExpenseDao expenseDao();
    private static AppDatabase INSTANCE;
    //build the database in one place, then get the instance
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "expense_db"
            ).build();
        }
        return INSTANCE;
    }
    //Ensures only one database instance


    //we use these to create a new thread only here and then return it and use elsewhere
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(1); //threads?

    public static ExecutorService getExecutor() {
        return databaseWriteExecutor;
    }

}

