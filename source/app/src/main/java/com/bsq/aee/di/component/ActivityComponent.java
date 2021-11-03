package com.bsq.aee.di.component;

import com.bsq.aee.di.module.ActivityModule;
import com.bsq.aee.di.scope.ActivityScope;
import com.bsq.aee.ui.account.login.LoginActivity;
import com.bsq.aee.ui.account.register.RegisterActivity;
import com.bsq.aee.ui.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(MainActivity activity);
}

