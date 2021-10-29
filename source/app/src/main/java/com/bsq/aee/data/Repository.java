package com.bsq.aee.data;

import com.bsq.aee.data.local.prefs.PreferencesService;
import com.bsq.aee.data.remote.ApiService;


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


    /**
     *  ################################## Remote api ##################################
     */
    ApiService getApiService();


}
