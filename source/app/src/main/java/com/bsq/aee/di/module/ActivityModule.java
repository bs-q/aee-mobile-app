package com.bsq.aee.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.ViewModelProviderFactory;
import com.bsq.aee.data.Repository;
import com.bsq.aee.di.scope.ActivityScope;
import com.bsq.aee.ui.account.login.LoginViewModel;
import com.bsq.aee.ui.account.register.RegisterViewModel;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.main.MainViewModel;
import com.bsq.aee.ui.main.university.details.UniversityDetailsViewModel;
import com.bsq.aee.utils.GetInfo;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Named("access_token")
    @Provides
    @ActivityScope
    String provideToken(Repository repository){
        return repository.getToken();
    }

    @Named("device_id")
    @Provides
    @ActivityScope
    String provideDeviceId( Context applicationContext){
        return GetInfo.getAll(applicationContext);
    }

    @Provides
    @ActivityScope
    LoginViewModel provideLoginViewModel(Repository repository, Context application) {
        Supplier<LoginViewModel> supplier = () -> new LoginViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<LoginViewModel> factory = new ViewModelProviderFactory<>(LoginViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginViewModel.class);
    }

    @Provides
    @ActivityScope
    RegisterViewModel provideRegisterViewModel(Repository repository, Context application) {
        Supplier<RegisterViewModel> supplier = () -> new RegisterViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<RegisterViewModel> factory = new ViewModelProviderFactory<>(RegisterViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(RegisterViewModel.class);
    }

    @Provides
    @ActivityScope
    MainViewModel provideMainViewModel(Repository repository, Context application) {
        Supplier<MainViewModel> supplier = () -> new MainViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<MainViewModel> factory = new ViewModelProviderFactory<>(MainViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    @Provides
    @ActivityScope
    UniversityDetailsViewModel provideUniversityDetailsViewModel(Repository repository, Context application) {
        Supplier<UniversityDetailsViewModel> supplier = () -> new UniversityDetailsViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<UniversityDetailsViewModel> factory = new ViewModelProviderFactory<>(UniversityDetailsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(UniversityDetailsViewModel.class);
    }

}
