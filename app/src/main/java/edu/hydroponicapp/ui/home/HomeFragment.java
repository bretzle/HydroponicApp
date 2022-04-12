package edu.hydroponicapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;
import java.sql.Timestamp;
import java.util.HashMap;

import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentHomeBinding;
import edu.hydroponicapp.ui.gallery.GalleryFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    NavController navController;
    Button journalButton;
    Object returnThis;
    String urmom;
    final String db_url ="https://hydroponicsapp-7ca52-default-rtdb.firebaseio.com/";
    FirebaseDatabase database = FirebaseDatabase.getInstance(db_url);
    DatabaseReference dbRef = database.getReference("sensorValues");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

           /* final TextView textView = binding.textHome;
            homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });*/
        return root;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.journalBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_gallery);

            }

        });

        view.findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_slideshow2);
            }

        });
        view.findViewById(R.id.sensorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_sensor2);
            }

        });
      readCurrent(view);
        //makeAdjustment(view);

    }

    public String readCurrent(View view) {
        DatabaseReference cur = database.getReference("sensorValues/1-set");
        cur.child("ph").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Snackbar.make(view, String.valueOf(dataSnapshot.getValue()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                returnThis = dataSnapshot.getValue();
                Log.d("firebase", String.valueOf(returnThis));
                urmom = (String) returnThis;
            }
        })
//                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            Object returnThis;
//            String i ="p";
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Snackbar.make(view, "Error getting data", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    Log.e("firebase", "Error getting data", task.getException());
//                } else {
//                    Snackbar.make(view, String.valueOf(task.getResult().getValue()), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    returnThis = task.getResult().getValue();
//                    Log.d("firebase", String.valueOf(returnThis));
//                    urmom = String.valueOf(returnThis);
//                }
//            }
//        })
        ;

    return urmom;
    }
//    public Object readCurrentPH(View view) {
//        dbRef.child("1-set/ph").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Snackbar.make(view, "Error getting data", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    Log.e("firebase", "Error getting data", task.getException());
//                } else {
//                    urmom = String.valueOf(task.getResult().getValue());
//                }
//            }
//        });
//
//        return urmom;
//    }
    public void makeAdjustment(View view){
        /**
         * read in current ph
         * 3 cases
         * - below 7 -> remove water / write new water level and change dashboard
         * - above 7 -> add water / write new water level and change dashboard
         * - at 7 -> no suggestions, disable adjustment button?
         */
        String curPH = readCurrent(view);

    }


    //MIGHT NOT NEED TO DO THAT, just display text that indicates adjustments are being made, when the levels are adjusted,
    //we will set up a phone notification to let the user know
    private void updateWaterLevel(DatabaseReference dbRef, String water_level){
        DatabaseReference unit = dbRef.child("unit_name").child("Durant");
        unit.child("water_level").setValue(water_level);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}