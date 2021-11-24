package com.bsq.aee.ui.main.search;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchViewModel extends BaseFragmentViewModel {

    ResponseListObj<PostResponse> responseList;

    public SearchViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getPost(int page, int size, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getPosts(size,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    if (responseList == null){
                        responseList = response;
                    } else {
                        responseList.copy(response);
                    }
                    callback.doSuccess();
                },callback::doError)
        );
    }
}
