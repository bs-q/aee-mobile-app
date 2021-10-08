package com.hq.remview.di.component;


import com.hq.remview.di.module.FragmentModule;
import com.hq.remview.di.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {

}
