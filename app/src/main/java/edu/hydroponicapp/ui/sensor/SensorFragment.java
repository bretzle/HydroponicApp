package edu.hydroponicapp.ui.sensor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentSensorBinding;

public class SensorFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private FragmentSensorBinding binding;

    protected TableLayout mTableView;
    protected List<String[]> mDataset = new ArrayList<>();
    String[] cur_data = new String[4];


    private void initDataset() {
        DatabaseReference dbRef_history = DbHolder.database.getReference("sensorValues/2-push");
        DatabaseReference dbRef_current = DbHolder.database.getReference("sensorValues/1-set");

        Task<DataSnapshot> a = dbRef_history.get();
        Task<DataSnapshot> b = dbRef_current.get();

        while (!a.isComplete() || !b.isComplete()) {}

        cur_data[0] = (String)b.getResult().child("timestamp").getValue();
        cur_data[1] = (String)b.getResult().child("ph").getValue();
        cur_data[2] = (String)b.getResult().child("temperature").getValue();
        cur_data[3] = "16.00";//(String)b.getResult().child("humidity").getValue();

        for (DataSnapshot snap : a.getResult().getChildren()) {
            Map<String, Object> entry = (Map<String, Object>) snap.getValue();

            String[] cur = new String[4];

            cur[0] = "05/10 (16:53:13)";//(String) entry.get("timestamp");
            cur[1] ="7.2";// String.valueOf(entry.get("ph"));
            cur[2] = "81.32";//(String) entry.get("temperature");
            cur[3] = "16.00";//(String) entry.get("humidity");

            mDataset.add(cur);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataset();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSensorBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        rootView.setTag(TAG);

        mTableView = rootView.findViewById(R.id.phTableView);

        int last = mTableView.getChildCount() - 1;

        TableRow rr = (TableRow) mTableView.getChildAt(last);

        TextView date = rootView.findViewById(R.id.sensor_data);
        TextView temp = rootView.findViewById(R.id.sensor_temp);
        TextView hum = rootView.findViewById(R.id.sensor_hum);
        TextView ph = rootView.findViewById(R.id.sensor_ph);

        date.setText(cur_data[0]);
        ph.setText(cur_data[1]);
        temp.setText(cur_data[2]);
        hum.setText(cur_data[3]);

        for (String[] data : mDataset) {
            TableRow row = new TableRow(getContext());

            ViewGroup.LayoutParams rowParams = rr.getChildAt(0).getLayoutParams();

            TextView label_ph = new TextView(getContext());
            label_ph.setText(data[0]);
            label_ph.setLayoutParams(rowParams);
            row.addView(label_ph);

            TextView label_temp = new TextView(getContext());
            label_temp.setText(data[1]);
            label_temp.setLayoutParams(rowParams);
            row.addView(label_temp);

            TextView label_humid = new TextView(getContext());
            label_humid.setText(data[2]);
            label_humid.setLayoutParams(rowParams);
            row.addView(label_humid);

            TextView label_date = new TextView(getContext());
            label_date.setText(data[3]);
            label_date.setLayoutParams(rowParams);
            row.addView(label_date);

            mTableView.addView(row);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}