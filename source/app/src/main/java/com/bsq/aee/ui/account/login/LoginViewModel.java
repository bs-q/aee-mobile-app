package com.bsq.aee.ui.account.login;

import androidx.databinding.ObservableField;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.request.CheckAccountRequest;
import com.bsq.aee.data.model.api.request.LoginRequest;
import com.bsq.aee.data.model.api.request.LoginWithGoogleRequest;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    public ObservableField<String> mEmail = new ObservableField<>();
    public ObservableField<String> mPassword = new ObservableField<>();
    public String mFirebaseToken;
    public String mFirebaseUID;
    public String mGmail;
    public String mAvatarPath;
    public boolean loginWithGoogle = false;

    public LoginViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void checkRegister(BaseCallback callback){
        CheckAccountRequest request = new CheckAccountRequest();
        request.setFirebaseToken(mFirebaseToken);
        request.setFirebaseUserId(mFirebaseUID);
        compositeDisposable.add(
                repository.getApiService().checkRegister(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            if (response.isResult()){
                                callback.doSuccess();
                            } else {
                                callback.doFail();
                            }
                        },callback::doError)
        );
    }

    public void doLogin(BaseCallback callback){
        LoginRequest request = new LoginRequest();
        request.setEmail(mEmail.get());
        request.setPassword(mPassword.get());
        compositeDisposable.add(
            repository.getApiService().login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isResult()){
                        repository.setToken(response.getData().getToken());
                        application.setAvatarPath(response.getData().getAvatarPath());
                        application.setFullName(response.getData().getFullName());
                        callback.doSuccess();
                    } else {
                        callback.doFail();
                    }
                }, callback::doError)
        );
    }
    public void doLoginWithGoogle(BaseCallback callback){
        LoginWithGoogleRequest request = new LoginWithGoogleRequest();
        request.setUid(mFirebaseUID);
        compositeDisposable.add(
                repository.getApiService().loginWithGoogle(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.isResult()){
                                repository.setToken(response.getData().getToken());
                                callback.doSuccess();
                            } else {
                                callback.doFail();
                            }
                        }, callback::doError)
        );
    }

    public void doProfile(BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().profile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response.isResult()){
                        setToken(response.getData().getToken());
                        setAvatarPath(response.getData().getAvatarPath());
                        setFullName(response.getData().getFullName());
                        callback.doSuccess();
                    } else {
                        callback.doFail();
                    }
                }, callback::doError)
        );
    }
}

