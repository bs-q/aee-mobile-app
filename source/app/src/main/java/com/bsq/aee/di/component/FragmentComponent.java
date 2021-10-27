package com.bsq.aee.di.component;


import com.bsq.aee.di.module.FragmentModule;
import com.bsq.aee.di.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {

}
