package com.bsq.aee.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityMainBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.main.home.HomeFragment;
import com.bsq.aee.ui.main.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel> {

    public static final String UNIVERSITY = "UNIVERSITY";
    public static final String HOME = "HOME";
    public static final String CHAT = "CHAT";
    public static final String FAVORITE = "FAVORITE";
    public static final String ACCOUNT = "ACCOUNT";
    private FragmentManager fm;
    private HomeFragment homeFragment;
    private Fragment active;
    private SearchFragment searchFragment;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        active = homeFragment;
        fm.beginTransaction().add(R.id.nav_host_fragment,homeFragment,HOME).commitNow();
        viewBinding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    fm.beginTransaction().hide(active).show(homeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commitNow();
                    active = homeFragment;
                    return true;
                case R.id.search:
                    if (searchFragment == null){
                        searchFragment = new SearchFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, searchFragment, CHAT).hide(active).commitNow();
                    } else {
                        fm.beginTransaction().hide(active).show(searchFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
                    }
                    active = searchFragment;
                    return true;
                default:
                    break;
            }
            return false;
        });

    }
}
