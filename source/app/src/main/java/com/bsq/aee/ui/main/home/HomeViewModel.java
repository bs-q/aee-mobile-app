package com.bsq.aee.ui.main.home;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.R;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.data.model.api.response.news.NewsResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class HomeViewModel extends BaseFragmentViewModel {
    public HomeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public List<NewsResponse> newsResponseList = new ArrayList<>();
    public void getNews(BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getNews()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response->{
                            newsResponseList = response.getData();
                            callback.doSuccess();
                        },throwable -> {
                            showErrorMessage(application.getString(R.string.api_error));
                            Timber.d(throwable);
                            callback.doError(throwable);
                        })
        );

    }

}
