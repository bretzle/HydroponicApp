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

    private void initDataset() {
        DatabaseReference dbRef = DbHolder.database.getReference("sensorValues");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {}

        for (DataSnapshot snap : a.getResult().getChildren()) {
            Map<String, Object> entry = (Map<String, Object>) snap.getValue();

            String[] cur = new String[4];
            cur[0] = String.valueOf(entry.get("ph"));
            cur[1] = (String) entry.get("temp");
            cur[2] = (String) entry.get("humidity");
            cur[3] = (String) entry.get("timestamp");

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