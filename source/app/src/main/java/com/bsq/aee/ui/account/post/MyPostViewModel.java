package com.bsq.aee.ui.account.post;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyPostViewModel extends BaseViewModel {
    List<PostResponse> postResponses;
    public MyPostViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getPost(BaseCallback callback) {
        compositeDisposable.add(
                repository.getApiService().myPost()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            postResponses = response.getData();
                            callback.doSuccess();
                        }, callback::doError)
        );
    }
}
