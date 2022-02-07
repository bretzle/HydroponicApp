package edu.hydroponicapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DbHolder {
    private static final String db_url ="https://hydroponicsapp-7ca52-default-rtdb.firebaseio.com/";
    public static FirebaseDatabase database = FirebaseDatabase.getInstance(db_url);

//    DatabaseReference dbRef = database.getReference("sensorValues");
}
