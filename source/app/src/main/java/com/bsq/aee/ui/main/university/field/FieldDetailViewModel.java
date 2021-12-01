package com.bsq.aee.ui.main.university.field;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.ui.base.activity.BaseViewModel;

public class FieldDetailViewModel extends BaseViewModel {
    FieldResponse fieldResponse;
    public FieldDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
