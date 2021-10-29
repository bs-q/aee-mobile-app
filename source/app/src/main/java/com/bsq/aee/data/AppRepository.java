package com.bsq.aee.data;

import com.bsq.aee.data.local.prefs.PreferencesService;
import com.bsq.aee.data.remote.ApiService;

import javax.inject.Inject;

public class AppRepository implements Repository {

    private final ApiService mApiService;
    private final PreferencesService mPreferencesHelper;

    @Inject
    public AppRepository(PreferencesService preferencesHelper, ApiService apiService) {
        this.mPreferencesHelper = preferencesHelper;
        this.mApiService = apiService;
    }

    /**
     * ################################## Preference section ##################################
     */
    @Override
    public String getToken() {
        return mPreferencesHelper.getToken();
    }

    @Override
    public void setToken(String token) {
        mPreferencesHelper.setToken(token);
    }

    @Override
    public PreferencesService getSharedPreferences(){
        return mPreferencesHelper;
    }


    /**
    *  ################################## Remote api ##################################
    */
    @Override
    public ApiService getApiService(){
        return mApiService;
    }



}
