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
import com.androidplot.xy.StepModel;
import com.androidplot.xy.XValueMarker;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.R;

public class tab1_ph extends Fragment {
    protected List<String> phValues = new ArrayList<>();
    protected List<String> timestamps = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPhData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //        need to populate xy plot here
        PixelUtils.init(view.getContext());

        XYPlot phPlot = (XYPlot) view.findViewById(R.id.phPlot);

        ArrayList<Number> ph = convert(phValues);
        double x=7.5;
        double y=6.5;
        XYSeries tempSeries = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "ph Series", 6.7,6.8,6.8,7.0,6.8,6.8,6.9,7.1,6.8,6.8,6.8,6.9,7.1,6.8,6.9);
        XYSeries control6 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", x,x,x,x,x,x,x,x,x,x,x,x,x,x,x);
        XYSeries control7 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", y,y,y,y,y,y,y,y,y,y,y,y,y,y,y);
        XYSeries control = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", 6,8);

        LineAndPointFormatter phFormat = new LineAndPointFormatter(Color.RED, null, null, null);
        LineAndPointFormatter controlFormat = new LineAndPointFormatter(Color.rgb(128,0,0), null, null, null);
        LineAndPointFormatter urmomFormat = new LineAndPointFormatter(null, null, null, null);

        phPlot.centerOnRangeOrigin(7);
        phPlot.setDomainBoundaries(6,8, BoundaryMode.GROW);

        phPlot.setDomainStep(StepMode.INCREMENT_BY_VAL,(int) 1);

        phPlot.addSeries(tempSeries, phFormat);
        phPlot.addSeries(control6, controlFormat);
        phPlot.addSeries(control7, controlFormat);
        phPlot.addSeries(control, urmomFormat);
    }

    private ArrayList<Number> convert(List<String> strings) {
        ArrayList<Number> returnThis = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            returnThis.add(Double.parseDouble(strings.get(i)));

        }
        return returnThis;
    }

    private void initPhData() {
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
            cur[1] = "6.9";
            cur[2] = "80";
            cur[3] = "70";
            cur[4] = "60";

            phValues.add(cur[1]);
            timestamps.add(cur[0]);

        }
    }
}
