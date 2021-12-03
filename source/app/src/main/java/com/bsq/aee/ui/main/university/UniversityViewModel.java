package com.bsq.aee.ui.main.university;

import androidx.databinding.ObservableField;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.data.Repository;
import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragmentViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UniversityViewModel extends BaseFragmentViewModel {
    ResponseListObj<UniversityResponse> universities;

    public ObservableField<String> search = new ObservableField<>("");
    public UniversityViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        universities = new ResponseListObj<>();
        universities.setData(new ArrayList<>());
        universities.setTotalPages(0);
        universities.setCurrentPage(0);
    }
    public void getReplies(int page, int size, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().getUniversities(size,page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    universities.replace(response);
                                    callback.doSuccess();
                                },callback::doError
                        )
        );
    }
    public void search(String name, BaseCallback callback){
        compositeDisposable.add(
                repository.getApiService().searchUniversity(name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    universities.getData().clear();
                                    universities.getData().addAll(response.getData());
                                    callback.doSuccess();
                                },callback::doError
                        )
        );
    }
}
