package com.bsq.aee.ui.main.university.field;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.activity.BaseViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FieldDetailViewModel extends BaseViewModel {
    FieldResponse fieldResponse;
    public FieldDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void addFavorite(BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().addFavorite(fieldResponse.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> callback.doSuccess(),callback::doError
                )
        );
    }
}
