package com.example.doan2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoiTacDBHelper extends DatabaseHelper<DoiTac> {
    private ArrayList<DoiTac> doiTacs;
    private ArrayList<String> keys;

    public DoiTacDBHelper() {
        super();
        doiTacs=new ArrayList<>();
        keys=new ArrayList<>();
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public ArrayList<DoiTac> getDoiTacs() {
        return doiTacs;
    }

    @Override
    public DatabaseReference getReference() {
        return firebaseDatabase.getReference("DoiTac");
    }

    @Override
    public void read(DatabaseStatus<DoiTac> status) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doiTacs.clear();
                keys.clear();
                for(DataSnapshot node: snapshot.getChildren())
                {
                    keys.add(node.getKey());
                    try{
                        doiTacs.add(node.getValue(DoiTac.class));
                    }catch (Exception e)
                    {
                        Log.e("CAST_ERR", e.getMessage());
                    }
                }
                status.doWhenRead(doiTacs,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void insert(DatabaseStatus<DoiTac> status, DoiTac instance) {
        databaseReference.push().setValue(instance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.doWhenInserted();
            }
        });
    }

    @Override
    public void update(DatabaseStatus<DoiTac> status, String key, DoiTac instance) {
        databaseReference.child(key).setValue(instance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.doWhenUpdated();
            }
        });
    }

    @Override
    public void delete(DatabaseStatus<DoiTac> status, String key) {
        databaseReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.doWhenDeleted();
            }
        });
    }
}
