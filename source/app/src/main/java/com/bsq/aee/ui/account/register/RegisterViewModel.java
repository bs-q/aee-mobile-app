package com.bsq.aee.ui.account.register;

import androidx.databinding.ObservableField;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.R;
import com.bsq.aee.constant.Constants;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.request.CreateAccountRequest;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterViewModel extends BaseViewModel {
    public ObservableField<String> mEmail = new ObservableField<>("");
    public ObservableField<String> mPassword = new ObservableField<>("");
    public ObservableField<String> mConfirmPassword = new ObservableField<>("");
    public String mToken;
    public String mFirebaseUID;
    public RegisterViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public boolean validateForm(){
        if (!Objects.equals(mConfirmPassword.get(), mPassword.get())){
            showErrorMessage(application.getString(R.string.password_not_match));
            return false;
        }
        if (!Objects.requireNonNull(mPassword.get()).matches(Constants.PASSWORD_REGEX)){
            showErrorMessage(application.getString(R.string.password_wrong_regex));
            return false;
        }
        return true;
    }

    public void doRegister(BaseCallback callback){
        CreateAccountRequest request = new CreateAccountRequest();
        request.setEmail(mEmail.get());
        request.setFirebaseToken(mToken);
        request.setFirebaseUserId(mFirebaseUID);
        request.setPassword(mPassword.get());
        compositeDisposable.add(
                repository.getApiService().register(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response.isResult()) {
                        callback.doSuccess();
                    } else {
                        callback.doFail();
                    }
                },callback::doError)
        );
    }

}
