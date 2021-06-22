package com.example.doan2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChuyenDiDBHelper extends DatabaseHelper<ChuyenDi> {
    private ArrayList<ChuyenDi> chuyenDis;

    public ChuyenDiDBHelper() {
        super();
        chuyenDis=new ArrayList<>();
    }

    @Override
    public DatabaseReference getReference() {
        return firebaseDatabase.getReference("ChuyenDi");
    }

    @Override
    public void read(DatabaseStatus<ChuyenDi> status) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chuyenDis.clear();
                ArrayList<String> keys=new ArrayList<>();
                for(DataSnapshot node: snapshot.getChildren())
                {
                    keys.add(node.getKey());
                    try{
                        chuyenDis.add(node.getValue(ChuyenDi.class));
                    }catch (Exception e)
                    {
                        Log.e("CAST_ERR", e.getMessage());
                    }
                }
                status.doWhenRead(chuyenDis,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void insert(DatabaseStatus<ChuyenDi> status, ChuyenDi instance) {
        databaseReference.push().setValue(instance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.doWhenInserted();
            }
        });
    }

    @Override
    public void update(DatabaseStatus<ChuyenDi> status, String key, ChuyenDi instance) {
        databaseReference.child(key).setValue(instance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.doWhenUpdated();
            }
        });
    }

    @Override
    public void delete(DatabaseStatus<ChuyenDi> status, String key) {
        databaseReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.doWhenDeleted();
            }
        });
    }

}
