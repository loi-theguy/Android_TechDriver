package com.example.doan2;

import java.util.ArrayList;

public interface DatabaseStatus<T> {
    void doWhenInserted();
    void doWhenRead(ArrayList<T> data, ArrayList<String> keys);
    void doWhenUpdated();
    void doWhenDeleted();
}
