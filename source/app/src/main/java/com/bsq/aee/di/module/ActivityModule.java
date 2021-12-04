package com.bsq.aee.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.ViewModelProviderFactory;
import com.bsq.aee.data.Repository;
import com.bsq.aee.di.scope.ActivityScope;
import com.bsq.aee.ui.account.login.LoginViewModel;
import com.bsq.aee.ui.account.password.ChangePasswordViewModel;
import com.bsq.aee.ui.account.post.MyPostViewModel;
import com.bsq.aee.ui.account.register.RegisterViewModel;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.main.MainViewModel;
import com.bsq.aee.ui.main.search.create.CreatePostViewModel;
import com.bsq.aee.ui.main.search.detail.PostDetailViewModel;
import com.bsq.aee.ui.main.university.details.UniversityDetailsViewModel;
import com.bsq.aee.ui.main.university.field.FieldDetailViewModel;
import com.bsq.aee.ui.web.WebViewModel;
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

    @Provides
    @ActivityScope
    CreatePostViewModel provideCreatePostViewModel(Repository repository, Context application) {
        Supplier<CreatePostViewModel> supplier = () -> new CreatePostViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<CreatePostViewModel> factory = new ViewModelProviderFactory<>(CreatePostViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CreatePostViewModel.class);
    }

    @Provides
    @ActivityScope
    PostDetailViewModel providePostDetailViewModel(Repository repository, Context application) {
        Supplier<PostDetailViewModel> supplier = () -> new PostDetailViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<PostDetailViewModel> factory = new ViewModelProviderFactory<>(PostDetailViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PostDetailViewModel.class);
    }

    @Provides
    @ActivityScope
    FieldDetailViewModel provideFieldDetailViewModel(Repository repository, Context application) {
        Supplier<FieldDetailViewModel> supplier = () -> new FieldDetailViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<FieldDetailViewModel> factory = new ViewModelProviderFactory<>(FieldDetailViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(FieldDetailViewModel.class);
    }

    @Provides
    @ActivityScope
    WebViewModel provideWebViewModel(Repository repository, Context application) {
        Supplier<WebViewModel> supplier = () -> new WebViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<WebViewModel> factory = new ViewModelProviderFactory<>(WebViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(WebViewModel.class);
    }

    @Provides
    @ActivityScope
    ChangePasswordViewModel provideChangePasswordViewModel(Repository repository, Context application) {
        Supplier<ChangePasswordViewModel> supplier = () -> new ChangePasswordViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ChangePasswordViewModel> factory = new ViewModelProviderFactory<>(ChangePasswordViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ChangePasswordViewModel.class);
    }

    @Provides
    @ActivityScope
    MyPostViewModel provideMyPostViewModel(Repository repository, Context application) {
        Supplier<MyPostViewModel> supplier = () -> new MyPostViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<MyPostViewModel> factory = new ViewModelProviderFactory<>(MyPostViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MyPostViewModel.class);
    }
}
