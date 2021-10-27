package com.bsq.aee.di.module;

import com.bsq.aee.data.Repository;
import com.bsq.aee.di.scope.FragmentScope;
import com.bsq.aee.ui.base.fragment.BaseFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Named("access_token")
    @Provides
    @FragmentScope
    String provideToken(Repository repository) {
        return repository.getToken();
    }



}
