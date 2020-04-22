package com.prepare.prepareurself.profile.data.model;

import java.util.List;

public class AllPreferencesResponseModel {

    private int error_code;
    private List<PreferredTechStack> preferences;

    public AllPreferencesResponseModel(){

    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<PreferredTechStack> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<PreferredTechStack> preferences) {
        this.preferences = preferences;
    }
}
