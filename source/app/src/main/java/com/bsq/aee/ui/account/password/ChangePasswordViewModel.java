package com.bsq.aee.ui.account.password;

import androidx.databinding.ObservableField;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.R;
import com.bsq.aee.constant.Constants;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.request.ChangePasswordRequest;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePasswordViewModel extends BaseViewModel {
    public ObservableField<String> oldPassword = new ObservableField<>("");
    public ObservableField<String> newPassword = new ObservableField<>("");
    public ChangePasswordViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public boolean validateForm(){
        if (Objects.requireNonNull(oldPassword.get()).isEmpty()){
            showErrorMessage("Vui lòng nhập mật khẩu cũ");
            return false;
        } else if (Objects.requireNonNull(newPassword.get()).isEmpty()){
            showErrorMessage("Vui lòng nhập mật khẩu mới");
            return false;
        }
        if (!Objects.requireNonNull(oldPassword.get()).matches(Constants.PASSWORD_REGEX)){
            showErrorMessage(application.getString(R.string.old_password_wrong_regex));
            return false;
        } else if (!Objects.requireNonNull(newPassword.get()).matches(Constants.PASSWORD_REGEX)){
            showErrorMessage(application.getString(R.string.password_wrong_regex));
            return false;
        }
        return true;
    }

    public void changePassword(BaseCallback callback){
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword.get());
        request.setNewPassword(newPassword.get());
        compositeDisposable.add(
                repository.getApiService().changePassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response ->{
                            if (response.isResult()){
                                showSuccessMessage(response.getMessage());
                                callback.doSuccess();
                            } else {
                                showErrorMessage(response.getMessage());
                                callback.doFail();
                            }
                        },callback::doError
                )
        );
    }
}
