package edu.hydroponicapp.ui.analytics.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
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

public class tab3_humidity extends Fragment {
    protected List<String> humValues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab3, container, false);


    }

    private ArrayList<Number> convert(List<String> strings) {
        ArrayList<Number> returnThis = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            returnThis.add(Double.parseDouble(strings.get(i)));

        }
        return returnThis;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        need to populate xy plot here
        initHumData();
        PixelUtils.init(view.getContext());

        XYPlot humPlot = (XYPlot) view.findViewById(R.id.humPlot);

        ArrayList<Number> hum = convert(humValues);
        XYSeries humSeries = new SimpleXYSeries(hum, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "humidity Series");
        LineAndPointFormatter humFormat = new LineAndPointFormatter();

        humPlot.addSeries(humSeries, humFormat);

    }

    private void initHumData() {
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

            humValues.add(cur[3]);

        }
    }

}