package com.hq.remview.data;

import com.hq.remview.data.local.prefs.PreferencesService;
import com.hq.remview.data.local.sqlite.DbService;
import com.hq.remview.data.remote.ApiService;

import javax.inject.Inject;

public class AppRepository implements Repository {

    private final ApiService mApiService;
    private final DbService mDbService;
    private final PreferencesService mPreferencesHelper;

    @Inject
    public AppRepository(DbService mDbService, PreferencesService preferencesHelper, ApiService apiService) {
        this.mDbService = mDbService;
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
     * ################################## Sqlite section ##################################
     */
    @Override
    public DbService getSqliteService(){
        return mDbService;
    }




    /**
    *  ################################## Remote api ##################################
    */
    @Override
    public ApiService getApiService(){
        return mApiService;
    }



}
