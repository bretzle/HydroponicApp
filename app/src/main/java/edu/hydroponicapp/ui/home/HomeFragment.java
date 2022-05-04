/*package edu.hydroponicapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    NavController navController;
    Button journalButton;

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
            });
        return root;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.journalBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_journal);

            }

        });

        view.findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_analytics);
            }

        });
        view.findViewById(R.id.sensorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_sensor2);
            }

        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}*/

package edu.hydroponicapp.ui.home;

import static java.lang.Integer.*;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    protected List<String[]> mDataset = new ArrayList<>();
    protected List<String[]> mDataset_ = new ArrayList<>();
    private FragmentHomeBinding binding;
    NavController navController;
    String cur;
    String lastChange;
    boolean changeState;
    String[] cur_data = new String[5];

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
        navController = Navigation.findNavController(view);

        initDataset();
        Button adjust_btn = view.findViewById(R.id.adjust_button);
        TextView textView_custom = view.findViewById(R.id.custom_solution_message);

        TextView date_text = view.findViewById(R.id.date_text_dash);
        TextView ph_text = view.findViewById(R.id.ph_text_);
        TextView temp_text = view.findViewById(R.id.temp_text_);
        TextView hum_text = view.findViewById(R.id.hum_text_);


        date_text.setText(cur_data[0]);
        ph_text.setText(cur_data[1]);
        temp_text.setText(cur_data[2]);
        hum_text.setText(cur_data[3]);

        boolean changing=getChangeState();

        //initUi(changing)

        String b=getAdjustmentMessage(view);
        TextView textView = view.findViewById(R.id.custom_solution_message);
        textView.setText(b);

        String buttonTxt ="Change Water";
        if(changing){
            buttonTxt="Deposit Minerals";
            b="After you change the water, press the button to deposit the mineral solution";
        }
        adjust_btn.setText(buttonTxt);
        textView.setText(b);

        view.findViewById(R.id.journalBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_journal);
            }
        });
        view.findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_nav_analytics);
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
                //access db here
                boolean changing = getChangeState();
                String poo="";
                String buttonText="";

                //if button has been pressed once
                if(!changing){//if in state 1, first push:
                    buttonText = "Deposit Minerals";
                    poo="After you change the water, press the button to deposit the mineral solution";
                    setChangeState(true);
                    setDepositState(false);
                }
                else { //if in state 2;
                    poo=getAdjustmentMessage(view);
                    buttonText = "Change Water";
                    setDepositState(true);
                    setChangeState(false);
                }
                textView_custom.setText(poo);
                adjust_btn.setText(buttonText);

                /**
                 * TODO:
                 * - Change text on dashboard
                 * - Change button text
                 * - on second button press, toast alert to verify decision
                 * - change db currentlychanging value to correct state
                 */

            }
        });


    }
    private void initUI(boolean changeState){

    }
    private String readCurrent(View view) {
        DatabaseReference dbRef = DbHolder.database.getReference("SensorValues/1");//sensorValues/1-set");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {}
        cur = String.valueOf(a.getResult());

        return cur;
    }
    private Date getLastWaterChange() throws ParseException {
        DatabaseReference dbRef = DbHolder.database.getReference("waterDetails/lastChange");//sensorValues/1-set");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {}
        lastChange = String.valueOf(a.getResult().getValue());

        DateFormat df = new SimpleDateFormat("MM/dd/yy (HH:mm:ss)");
        Date date = df.parse(lastChange);

        return date;

    }

    private boolean getChangeState(){
        DatabaseReference dbRef = database.getReference("waterDetails/currentlyChanging");
        Task<DataSnapshot> a = dbRef.get();
        while (!a.isComplete()) {}

        changeState =Boolean.parseBoolean(String.valueOf(a.getResult().getValue()));
        return changeState;
    }

    private void setChangeState(boolean newState){
        DatabaseReference dbRef = database.getReference("waterDetails/currentlyChanging");
        dbRef.setValue(newState);

    }
    private void setDepositState(boolean depositMin){
        DatabaseReference dbRef = database.getReference("waterDetails/deposit");
        dbRef.setValue(depositMin); //should be true when hitting deposit button, reset to false in python

    }


    public String getAdjustmentMessage(View view){
        /**
         * 2 cases:
         * need to change water
         * - if it has been one week since last change
         * - need to update waterdetails in the app, then this will trigger the pumps to add mineral solution
         *
         * do not need to change water
         * - give them the message countdown til when they need to change the water
         */
        long urmom = 0;
        try {
            Date d1 = getLastWaterChange();
            Date d2 = Date.from(Instant.now());

            long difference_In_Time
                    = d2.getTime() - d1.getTime();
            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60));
            urmom = difference_In_Hours;
        }catch (Exception e){
            e.printStackTrace();
        }

        String dashboardMessage = "";
        if(urmom>168){
            dashboardMessage = "It has been "+urmom+ " hours since you have changed your water, time to change";
            //send notification
            //enable button

        } else{
            dashboardMessage = "It has been "+urmom+ " hours since you have changed your water, we will notify you when you need to change";

        }
        return dashboardMessage;

    }

    private void initDataset() {
        //DatabaseReference dbRef = DbHolder.database.getReference("sensorValues"); RESTORE
        DatabaseReference dbRef = DbHolder.database.getReference("sensorValues/1-set");//TEST
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {}

        //DataSnapshot b = a.getResult().child("1-set");//RESTORE
        DataSnapshot b = a.getResult();//.child("1-set");//TEST

        Map<String, Object> initial_entry = (Map<String, Object>) b.getValue();

        cur_data[0] = (String) initial_entry.get("timestamp");
        cur_data[1] = String.valueOf(initial_entry.get("ph"));
        cur_data[2] = (String) initial_entry.get("temperature");
        cur_data[3] = (String) initial_entry.get("humidity");
        cur_data[4] = (String) initial_entry.get("mineral");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
/**
 * deprecated methods:
 *     private String fixui(boolean state){
 *         String poo="";
 *         if(state){
 * //            adjust_btn.setEnabled(false);
 * //            adjust_btn.setBackgroundColor(Color.DKGRAY);
 *             poo="Deposit Minerals";
 *         } else{
 * //            adjust_btn.setEnabled(true);
 * //            adjust_btn.setBackgroundColor(1992772);
 *             poo="Change Water";
 *
 *         }
 *         return poo;
 *
 *     }
 *
 *     //        view.findViewById(R.id.mineral_button).setOnClickListener(new View.OnClickListener() {
 * //            @Override
 * //            public void onClick(View v) {
 * //                //access db here
 * //                //boolean changing = getChangeState();
 * //                String poo="";
 * //
 * //                if(changing){//if in state 1, first push:
 * //                    fixui(true, adjust_btn,mineral_btn);
 * //                    poo=getAdjustmentMessage(v);
 * //                    setChangeState(false);
 * //                }
 * //
 * //                textView_custom.setText(poo);
 * //
 * //            }
 * //        });
 * //        TextView ph_text = view.findViewById(R.id.ph_text);
 * //        ph_text.setText(mDataset.get(0)[1]);
 * //
 * //        TextView temp_text = view.findViewById(R.id.temp_text);
 * //        temp_text.setText(mDataset.get(0)[2]);
 * //
 * //        TextView hum_text = view.findViewById(R.id.hum_text);
 * //        hum_text.setText(mDataset.get(0)[3]);
 * //
 * //        TextView date_text = view.findViewById(R.id.date_text);
 * //        date_text.setText(mDataset.get(0)[0]);
 *
 * //        int x=11;
 * //        double mineral=0.0;
 * //        String curMin = readCurrentMineral(view);
 * //        try{
 * //            mineral = Double.parseDouble(curMin);
 * //        } catch (Exception e){
 * //            e.printStackTrace();
 * //        }
 * //    private String readCurrentPH(View view) {
 * //        String b = readCurrent(view);
 * //        int p = b.indexOf("ph")+3;
 * //        String c = b.substring(p, p+3);
 * //
 * //    }
 *     private String readCurrentMineral(View view){
 * //        String b = readCurrent(view);
 * //        int p = b.indexOf("mineral")+8;
 * //        String c = b.substring(p);
 * //        String d = c.substring(0,c.indexOf(","));
 *         return cur_data[4];
 *
 *     }
 */