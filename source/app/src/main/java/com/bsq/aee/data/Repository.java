package com.hq.remview.data;

import com.hq.remview.data.local.prefs.PreferencesService;
import com.hq.remview.data.local.sqlite.DbService;
import com.hq.remview.data.remote.ApiService;


public interface Repository {

    /**
     * ################################## Preference section ##################################
     */
    String getToken();
    void setToken(String token);
    PreferencesService getSharedPreferences();


    /**
     * ################################## Sqlite section ##################################
     */
    DbService getSqliteService();



    /**
     *  ################################## Remote api ##################################
     */
    ApiService getApiService();


}
