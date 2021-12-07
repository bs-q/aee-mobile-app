package com.bsq.aee.ui.main.account;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

public class AccountViewModel extends BaseFragmentViewModel {
    public AccountViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void clearToken(){
        repository.setToken(null);
    }
}
