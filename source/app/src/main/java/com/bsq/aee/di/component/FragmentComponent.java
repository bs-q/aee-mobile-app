package com.bsq.aee.di.component;


import com.bsq.aee.di.module.FragmentModule;
import com.bsq.aee.di.scope.FragmentScope;
import com.bsq.aee.ui.main.account.AccountFragment;
import com.bsq.aee.ui.main.favorite.FavoriteFragment;
import com.bsq.aee.ui.main.home.HomeFragment;
import com.bsq.aee.ui.main.search.SearchFragment;
import com.bsq.aee.ui.main.university.UniversityFragment;

import dagger.Component;

@FragmentScope
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(AccountFragment fragment);

    void inject(UniversityFragment fragment);

    void inject(SearchFragment fragment);

    void inject(HomeFragment fragment);

    void inject(FavoriteFragment fragment);
}
