package com.example.doan2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class DatabaseHelper<T> {
    protected FirebaseDatabase firebaseDatabase;
    protected DatabaseReference databaseReference;
    public DatabaseHelper() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=getReference();
    }

    public abstract DatabaseReference getReference();
    public abstract void read(final DatabaseStatus<T> status);
    public abstract void insert(final DatabaseStatus<T> status, T instance);
    public abstract void update(final DatabaseStatus<T> status, String key, T instance);
    public abstract void delete(final DatabaseStatus<T> status, String key);
}
