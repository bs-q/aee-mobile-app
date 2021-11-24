package com.bsq.aee.ui.main.search.create;

import androidx.databinding.ObservableField;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.request.CreatePostRequest;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreatePostViewModel extends BaseViewModel {
    public ObservableField<String> title = new ObservableField<>("");
    public String content;
    public CreatePostViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public boolean validate(){
        return (Objects.requireNonNull(title.get()).isEmpty() || Objects.equals(title.get(), ""));
    }
    public void createPost(BaseCallback callback){
        CreatePostRequest request = new CreatePostRequest();
        request.setContent(content);
        request.setTitle(title.get());
        compositeDisposable.add(repository.getApiService()
        .createPost(request).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response ->{
            if (response.isResult()){
                callback.doSuccess();
            } else {
                callback.doFail();
            }
        },callback::doError));
    }
}
