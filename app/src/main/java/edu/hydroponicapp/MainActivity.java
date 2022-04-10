package edu.hydroponicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Timestamp;

import edu.hydroponicapp.databinding.ActivityMainBinding;
import edu.hydroponicapp.databinding.FragmentJournalBinding;
import edu.hydroponicapp.databinding.FragmentHomeBinding;
import edu.hydroponicapp.ui.journal.JournalFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FragmentHomeBinding homeBinding;
    private FragmentJournalBinding journalBinding;
    final String db_url ="https://hydroponicsapp-7ca52-default-rtdb.firebaseio.com/";
    FirebaseDatabase database = FirebaseDatabase.getInstance(db_url);
    NavController navC;
    DatabaseReference dbRef = database.getReference("sensorValues");
    Timestamp timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseDatabase.getInstance(db_url).setPersistenceEnabled(true);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
//        navC= Navigation.findNavController();
//
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navC.navigate(R.id.action_nav_home_to_nav_gallery);
//
//
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    private void testWrite(DatabaseReference dbRef, Timestamp time){
        DatabaseReference unit = dbRef.child("unit_name").child("Durant");
        unit.child("ph").setValue("2");
        unit.child("time").setValue(time.toString());
    }
    private void testRead(DatabaseReference dbRef, View view){
        dbRef.child("unit_name").child("Durant").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Snackbar.make(view, "Error getting data", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Snackbar.make(view, String.valueOf(task.getResult().getValue()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void onJournalClick(View view) {

        view.findViewById(R.id.JournalCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), JournalFragment.class);
                startActivity(intent);
            }

        });
    }


}