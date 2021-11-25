package com.bsq.aee.ui.main.search.detail;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.data.model.api.response.ReplyResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PostDetailViewModel extends BaseViewModel {
    ResponseListObj<ReplyResponse> replies;
    PostResponse header;

    public PostDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        replies = new ResponseListObj<>();
        replies.setData(new ArrayList<>());
    }

    public void getReplies(int page, int size, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getReplies(size,page,header.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            replies.copy(response);
                            callback.doSuccess();
                        },callback::doError
                )
        );
    }
}
