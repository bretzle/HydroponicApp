package edu.hydroponicapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.MainActivity;
import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentHomeBinding;
import edu.hydroponicapp.ui.gallery.GalleryFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    protected List<String[]> mDataset = new ArrayList<>();
    protected List<String[]> mDataset_ = new ArrayList<>();
    private FragmentHomeBinding binding;
    NavController navController;
    String cur;
    String[] cur_data = new String[4];

    final String db_url ="https://hydroponicsapp-7ca52-default-rtdb.firebaseio.com/";
    FirebaseDatabase database = FirebaseDatabase.getInstance(db_url);
    DatabaseReference dbRef = database.getReference("sensorValues");
    DatabaseReference dbRefPH = database.getReference("sensorValues/1-set/ph");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initDataset();

        navController = Navigation.findNavController(view);

        TextView ph_text = view.findViewById(R.id.ph_text_);
        ph_text.setText(cur_data[1]);

        TextView temp_text = view.findViewById(R.id.temp_text_);
        temp_text.setText(cur_data[2]);

        TextView hum_text = view.findViewById(R.id.hum_text_);
        hum_text.setText(cur_data[3]);

        TextView date_text = view.findViewById(R.id.date_text_dash);
        date_text.setText(cur_data[0]);

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
        view.findViewById(R.id.adjust_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b = getAdjustmentMessage(v);
                TextView textView = view.findViewById(R.id.custom_solution_message);
                textView.setText(b);

                //send message
            }
        });
//        TextView ph_text = view.findViewById(R.id.ph_text);
//        ph_text.setText(mDataset.get(0)[1]);
//
//        TextView temp_text = view.findViewById(R.id.temp_text);
//        temp_text.setText(mDataset.get(0)[2]);
//
//        TextView hum_text = view.findViewById(R.id.hum_text);
//        hum_text.setText(mDataset.get(0)[3]);
//
//        TextView date_text = view.findViewById(R.id.date_text);
//        date_text.setText(mDataset.get(0)[0]);
        //makeAdjustment(view);

    }

    private String readCurrent(View view) {
        DatabaseReference dbRef = DbHolder.database.getReference("sensorValues/1-set");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {}
        cur = String.valueOf(a.getResult());

        return cur;
    }

    private String readCurrentPH(View view) {
        String b = readCurrent(view);
        int p = b.indexOf("ph")+3;
        String c = b.substring(p, p+3);

        return c;
    }
    private String readCurrentWaterLevel(View view){
        String b = readCurrent(view);
        int p = b.indexOf("water_level")+12;
        String c = b.substring(p);
        String d = c.substring(c.indexOf(0),c.indexOf(","));
        return d;

    }

    public String getAdjustmentMessage(View view){
        /**
         * read in current ph
         * 3 cases
         * - below 7 -> remove water / write new water level and change dashboard
         * - above 7 -> add water / write new water level and change dashboard
         * - at 7 -> no suggestions, disable adjustment button?
         */
        double ph=0.0;
        String curPH = readCurrentPH(view);
        try{
            ph = Double.parseDouble(curPH);
        } catch (Exception e){
            e.printStackTrace();
        }
        String dashboardMessage = "";
        if(ph>7){
            dashboardMessage = "The current pH value of "+ph+" is higher than the ideal concentration. Press the adjustment button below to optimize solution";
        } else if(ph<6){
            dashboardMessage = "The current pH value of "+ph+" is lower than the ideal concentration. Press the adjustment button below to optimize solution";

        } else{
            dashboardMessage = "The current pH value of "+ph+" is within the ideal range for radish growth! Good work!";
        }
        return dashboardMessage;

    }


    //MIGHT NOT NEED TO DO THAT, just display text that indicates adjustments are being made, when the levels are adjusted,
    //we will set up a phone notification to let the user know
    private void updateWaterLevel(DatabaseReference dbRef, String water_level){
        DatabaseReference unit = dbRef.child("unit_name").child("Durant");
        unit.child("water_level").setValue(water_level);
    }

    private void initDataset() {
        DatabaseReference dbRef = DbHolder.database.getReference("sensorValues");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {}

        DataSnapshot b = a.getResult().child("1-set");
        Map<String, Object> initial_entry = (Map<String, Object>) b.getValue();

        cur_data[0] = (String) initial_entry.get("timestamp");
        cur_data[1] = String.valueOf(initial_entry.get("ph"));
        cur_data[2] = (String) initial_entry.get("temperature");
        cur_data[3] = (String) initial_entry.get("humidity");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}