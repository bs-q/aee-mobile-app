package com.bsq.aee.ui.base.activity;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.other.ToastMessage;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.Setter;

public class BaseViewModel extends ViewModel {
    protected CompositeDisposable compositeDisposable;
    protected final ObservableBoolean mIsLoading = new ObservableBoolean();
    protected final MutableLiveData<ToastMessage> mErrorMessage = new MutableLiveData<>();

    @Setter
    protected String token;

    @Setter
    protected String deviceId;

    protected final Repository repository;
    protected final MVVMApplication application;

    public BaseViewModel(Repository repository, MVVMApplication application){
        this.repository = repository;
        this.application = application;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void showLoading(){
        mIsLoading.set(true);
    }

    public void hideLoading(){
        mIsLoading.set(false);
    }

    public void showSuccessMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_SUCCESS,message));
    }

    public void showNormalMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_NORMAL,message));
    }

    public void showWarningMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_WARNING,message));
    }

    public void showErrorMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_ERROR,message));
    }
}
