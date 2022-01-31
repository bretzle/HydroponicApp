package edu.hydroponicapp.ui.sensor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHandler {

    DatabaseReference dbRef;

    DatabaseHandler(DatabaseReference dbRef) {
        this.dbRef = dbRef;
    }

    public String readLatestPost(String unit_name) {
        String b = dbRef.child("unit_name").child(unit_name).get().toString();

//        b.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                } else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
        return b;
    }

    private void newUnit() {
        //TODO
        //This funcitonality
    }

    private void newValue(String unit_name) {

    }
}
