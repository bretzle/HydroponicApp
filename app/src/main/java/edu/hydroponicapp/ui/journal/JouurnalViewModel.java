package edu.hydroponicapp.ui.journal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JouurnalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JouurnalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is journal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}