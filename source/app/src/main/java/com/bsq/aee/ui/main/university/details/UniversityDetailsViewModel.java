package com.bsq.aee.ui.main.university.details;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.ui.base.activity.BaseViewModel;

public class UniversityDetailsViewModel extends BaseViewModel {

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> address = new ObservableField<>("");
    public ObservableField<String> description = new ObservableField<>("");
    public ObservableInt rating = new ObservableInt();

    // false: show description, true: show fields
    public ObservableBoolean page = new ObservableBoolean(false);

    public UniversityDetailsViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
