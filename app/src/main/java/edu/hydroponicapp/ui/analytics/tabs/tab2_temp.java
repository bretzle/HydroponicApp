package edu.hydroponicapp.ui.analytics.tabs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.R;


public class tab2_temp extends Fragment {
    // = view.findViewById(R.id.plot);
    protected List<String> tempValues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false);


    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        need to populate xy plot here
        initTempData();
        PixelUtils.init(view.getContext());

        XYPlot tempPlot = (XYPlot) view.findViewById(R.id.tempPlot);

//        ArrayList<Number> temp = convert(tempValues);
        int x=55;
        int y=75;
        XYSeries tempSeries = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "temp Series",56,62,59,60,63,59,65,64,67,64,63,65,68,63,59);
        XYSeries control6 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", x,x,x,x,x,x,x,x,x,x,x,x,x,x,x);
        XYSeries control7 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", y,y,y,y,y,y,y,y,y,y,y,y,y,y,y);
        XYSeries control = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", 50,80);

        LineAndPointFormatter tempFormat = new LineAndPointFormatter(Color.GREEN, null, null, null);
        LineAndPointFormatter controlFormat = new LineAndPointFormatter(Color.rgb(128,0,0), null, null, null);
        LineAndPointFormatter urmomFormat = new LineAndPointFormatter(null, null, null, null);

        tempPlot.centerOnRangeOrigin(65);
        tempPlot.setDomainBoundaries(50,80, BoundaryMode.GROW);

        tempPlot.setDomainStep(StepMode.INCREMENT_BY_VAL,(int) 1);

        tempPlot.addSeries(tempSeries, tempFormat);
        tempPlot.addSeries(control6, controlFormat);
        tempPlot.addSeries(control7, controlFormat);
        tempPlot.addSeries(control, urmomFormat);
    }

    private ArrayList<Number> convert(List<String> strings) {
        ArrayList<Number> returnThis = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            returnThis.add(Double.parseDouble(strings.get(i)));

        }
        return returnThis;
    }

    private void initTempData() {
        DatabaseReference dbRef = DbHolder.database.getReference("sensorValues/2-push");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {
        }

        for (DataSnapshot snap : a.getResult().getChildren()) {
            Map<String, Object> entry = (Map<String, Object>) snap.getValue();
            String[] cur = new String[5];


//            cur[0] = (String) entry.get("timestamp");
//            cur[1] = String.valueOf(entry.get("ph"));
//            cur[2] = (String) entry.get("temp");
//            cur[3] = (String) entry.get("humidity");0 =
//            cur[4] = (String) entry.get("mineral"); //if we want to show when the mineral deposits happen

            cur[0] = "05/01 (16:53:13)";
            cur[1] = "7.2";
            cur[2] = "80";
            cur[3] = "70";
            cur[4] = "60";

            tempValues.add(cur[2]);

        }
    }
}