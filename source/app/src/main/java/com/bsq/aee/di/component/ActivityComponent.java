package com.hq.remview.di.component;

import com.hq.remview.di.module.ActivityModule;
import com.hq.remview.di.scope.ActivityScope;
import com.hq.remview.ui.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

}

