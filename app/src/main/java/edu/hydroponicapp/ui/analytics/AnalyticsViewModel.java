package edu.hydroponicapp.ui.analytics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomizationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CustomizationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is analytics fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}