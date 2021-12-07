package com.bsq.aee.ui.main.favorite;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteViewModel extends BaseFragmentViewModel {

    ResponseListObj<FieldResponse> favorite;
    public FavoriteViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getFavorite(BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            favorite = response;
                            callback.doSuccess();
                        }, callback::doError
                )
        );
    }
}
