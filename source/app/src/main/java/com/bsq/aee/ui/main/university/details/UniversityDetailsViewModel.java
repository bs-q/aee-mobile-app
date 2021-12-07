package com.bsq.aee.ui.main.university.details;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.R;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class UniversityDetailsViewModel extends BaseViewModel {

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> address = new ObservableField<>("");
    public ObservableField<String> description = new ObservableField<>("");
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<String> universityName = new ObservableField<>("");
    public ObservableInt rating = new ObservableInt();
    // false: show description, true: show fields
    public ObservableBoolean page = new ObservableBoolean(false);
    public List<FieldResponse> fieldResponseList = new ArrayList<>();

    public UniversityDetailsViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getField(long id, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getField(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    fieldResponseList = response.getData();
                    callback.doSuccess();
                },throwable -> {
                    showErrorMessage(application.getString(R.string.api_error));
                    Timber.d(throwable);
                    callback.doError(throwable);
                })
        );

    }
}

