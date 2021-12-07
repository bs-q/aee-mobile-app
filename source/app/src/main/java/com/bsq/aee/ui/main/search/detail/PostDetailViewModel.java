package com.bsq.aee.ui.main.search.detail;

import androidx.databinding.ObservableField;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.request.ReplyRequest;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.data.model.api.response.ReplyResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PostDetailViewModel extends BaseViewModel {
    ResponseListObj<ReplyResponse> replies;
    public ObservableField<String> reply = new ObservableField<>("");
    PostResponse header;

    public PostDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        replies = new ResponseListObj<>();
        replies.setData(new ArrayList<>());
        replies.setTotalPages(0);
        replies.setCurrentPage(0);
    }

    public void getReplies(int page, int size, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getReplies(size,page,header.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            replies.replace(response);
                            callback.doSuccess();
                        },callback::doError
                )
        );
    }
    public void resetReplies(int page, int size, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getReplies(size,page,header.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    replies = response;
                                    callback.doSuccess();
                                },callback::doError
                        )
        );
    }
    public void reply(BaseCallback callback){
        ReplyRequest request = new ReplyRequest();
        request.setContent(reply.get());
        request.setPostId(header.getId());
        compositeDisposable.add(
                repository.getApiService().reply(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                   callback.doSuccess();
                                },callback::doError
                        )
        );
    }
}
