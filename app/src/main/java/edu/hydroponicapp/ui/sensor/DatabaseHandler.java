package edu.hydroponicapp.ui.sensor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHandler {
    final String db_url ="https://hydroponicsapp-7ca52-default-rtdb.firebaseio.com/";
    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference(db_url);
    DatabaseHandler(DatabaseReference dbRef) {
        dbRef = this.dbRef;
    }
    private void readLatestPost(){

    }
    private void newUnit(){
        //TODO
        //This funcitonality
    }
    private void newValue(String unit_name){

    }
}
