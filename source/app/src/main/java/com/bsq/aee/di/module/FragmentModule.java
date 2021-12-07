package com.bsq.aee.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.bsq.aee.MVVMApplication;
import com.bsq.aee.ViewModelProviderFactory;
import com.bsq.aee.data.Repository;
import com.bsq.aee.di.scope.FragmentScope;
import com.bsq.aee.ui.base.fragment.BaseFragment;
import com.bsq.aee.ui.main.account.AccountViewModel;
import com.bsq.aee.ui.main.favorite.FavoriteViewModel;
import com.bsq.aee.ui.main.home.HomeViewModel;
import com.bsq.aee.ui.main.search.SearchViewModel;
import com.bsq.aee.ui.main.university.UniversityViewModel;

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

    @Provides
    @FragmentScope
    AccountViewModel provideAccountViewModel(Repository repository, Context application) {
        Supplier<AccountViewModel> supplier = () -> new AccountViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<AccountViewModel> factory = new ViewModelProviderFactory<>(AccountViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(AccountViewModel.class);
    }
    @Provides
    @FragmentScope
    FavoriteViewModel provideFavoriteViewModel(Repository repository, Context application) {
            Supplier<FavoriteViewModel> supplier = () -> new FavoriteViewModel(repository, (MVVMApplication)application);
            ViewModelProviderFactory<FavoriteViewModel> factory = new ViewModelProviderFactory<>(FavoriteViewModel.class, supplier);
            return new ViewModelProvider(fragment, factory).get(FavoriteViewModel.class);
    }
    @Provides
    @FragmentScope
    HomeViewModel provideHomeViewModel(Repository repository, Context application) {
            Supplier<HomeViewModel> supplier = () -> new HomeViewModel(repository, (MVVMApplication)application);
            ViewModelProviderFactory<HomeViewModel> factory = new ViewModelProviderFactory<>(HomeViewModel.class, supplier);
            return new ViewModelProvider(fragment, factory).get(HomeViewModel.class);
        }
    @Provides
    @FragmentScope
    SearchViewModel provideSearchViewModel(Repository repository, Context application) {
            Supplier<SearchViewModel> supplier = () -> new SearchViewModel(repository, (MVVMApplication)application);
            ViewModelProviderFactory<SearchViewModel> factory = new ViewModelProviderFactory<>(SearchViewModel.class, supplier);
            return new ViewModelProvider(fragment, factory).get(SearchViewModel.class);
    }
    @Provides
    @FragmentScope
    UniversityViewModel provideUniversityViewModel(Repository repository, Context application) {
        Supplier<UniversityViewModel> supplier = () -> new UniversityViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<UniversityViewModel> factory = new ViewModelProviderFactory<>(UniversityViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(UniversityViewModel.class);
    }
}
