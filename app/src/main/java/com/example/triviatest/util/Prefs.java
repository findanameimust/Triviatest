package com.example.triviatest.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighScore(int score){
        int currentSccore = score;
        int lastScore = preferences.getInt("high scre",0);
        if(currentSccore>lastScore){
            preferences.edit().putInt("high_score",currentSccore).apply();

        }

    }
    public int getHighScore(){
        return preferences.getInt("high_score",0);
    }


    public void setState(int index){
        preferences.edit().putInt("index_state",index).apply();
    }

    public int getState(){
        return preferences.getInt("index_state",0);
    }
}
