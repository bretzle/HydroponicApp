package edu.hydroponicapp.ui.sensor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SensorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sensor fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}