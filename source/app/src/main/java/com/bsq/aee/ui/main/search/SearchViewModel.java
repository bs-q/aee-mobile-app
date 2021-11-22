package com.bsq.aee.ui.main.search;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends BaseFragmentViewModel {

    List<PostResponse> responseList = new ArrayList<>();

    public SearchViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
