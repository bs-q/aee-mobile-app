package com.bsq.aee.ui.main.home;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

public class HomeViewModel extends BaseFragmentViewModel {
    public HomeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
