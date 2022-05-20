package edu.hydroponicapp.ui.journal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentJournalBinding;

public class JournalFragment extends Fragment {

    private JouurnalViewModel galleryViewModel;
    private FragmentJournalBinding binding;
    final String db_url = "https://hydroponicsapp-7ca52-default-rtdb.firebaseio.com/";
    FirebaseDatabase database = FirebaseDatabase.getInstance(db_url);
    DatabaseReference dbRef = database.getReference("journalEntries");

    ArrayList<CharSequence> arrayListCollection = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter;
    EditText txt; // user input bar
    String d2;
    CardView demo;
    TextView demo2;
    ArrayList<String> entries = new ArrayList<>();
    int visible = 0;
    String poo = "";
    LinearLayout n;

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initJournal(view);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(JouurnalViewModel.class);

        binding = FragmentJournalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.findViewById(R.id.entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertName = new AlertDialog.Builder(getContext());
                final EditText editTextName1 = new EditText(getContext());

                alertName.setTitle("New Entry");

                alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(getContext());
                layoutName.setOrientation(LinearLayout.VERTICAL);
                layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);
//                demo=new CardView(getContext());
                demo2 = new TextView(getContext());
                n = root.findViewById(R.id.urmom);

                alertName.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(DialogInterface dialog, int whichButton) {
                        txt = editTextName1; // variable to collect user input
                        //access db and push
                        d2 = Date.from(Instant.now()).toString();
                        ArrayList<String> list = new ArrayList<>();

                        list.add(txt.getText().toString());
                        list.add(d2);
                        visible = 1;
                        dbRef.push().setValue(list);
                        poo = txt.getText().toString() + "\n" + d2 + "\n\n";

                        demo2.setText(poo);
                        demo2.setVisibility(View.VISIBLE);
                        demo2.setTextSize(20);

                        n.addView(demo2);


                    }
                });

                alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel(); // closes dialog
                        // display the dialog
                        poo = "";
                        demo2.setText(poo);
                    }
                });

                alertName.show();
            }
        });


        return root;
    }


    private void initJournal(View root) {
        DatabaseReference dbRef = DbHolder.database.getReference("journalEntries");//TEST
        Task<DataSnapshot> a = dbRef.get();

        TextView demo2 = new TextView(root.getContext());
        LinearLayout n0 = root.findViewById(R.id.urmom);

        String poo2 = "";

        while (!a.isComplete()) {
        }

        Iterable<DataSnapshot> snapshot = a.getResult().getChildren();
        while (snapshot.iterator().hasNext()) {
            Object entry = snapshot.iterator().next().getValue();

            poo2 = entry.toString();

            demo2.setText(poo2);
            demo2.setVisibility(View.VISIBLE);

            n0.addView(demo2);

        }

        String urmom = "hello";

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}