package edu.hydroponicapp.ui.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentSensorBinding;

public class SensorFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final int DATASET_COUNT = 10;
    private FragmentSensorBinding binding;
    protected TableLayout mTableView;
    protected String[][] mDataset;

    DatabaseReference dbRef;
    DatabaseHandler db;

    //TODO: MOST OF THE WRITING TO THE SENSOR VALUES DB WILL BE FROM THE ARDUINO
    //THE APPLICATION WILL MOSTLY BE DOING DATA READS AND POSSIBLY POSTS TO A NEW DB
    private void initDataset() {
        db = new DatabaseHandler(dbRef);

//        mDataset = new String[DATASET_COUNT][2];
//        for (int i = 0; i < DATASET_COUNT; i++) {
//            mDataset[i][0] = "##.#";
//            mDataset[i][1] = "##/## ##:##";
//        }
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

            TextView label_hello = new TextView(getContext());
            label_hello.setText(data[0]);
            label_hello.setLayoutParams(rowParams);
            row.addView(label_hello);

            TextView label_android = new TextView(getContext());
            label_android.setText(data[1]);
            label_android.setLayoutParams(rowParams);
            row.addView(label_android);

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