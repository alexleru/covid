package ru.alexleru.covid19Widget;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Covid19App extends Application {
    public static void saveConfig(int widgetId, int position, Context context) {
        String widgetIdString = String.valueOf(widgetId);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Covid19App.class.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(widgetIdString, position);
        editor.apply();
    }

    public static int loadConfig(int widgetId, Context context) {
        String widgetIdString = String.valueOf(widgetId);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Covid19App.class.getName(), MODE_PRIVATE);
        return sharedPreferences.getInt(widgetIdString, 0);
    }

}
