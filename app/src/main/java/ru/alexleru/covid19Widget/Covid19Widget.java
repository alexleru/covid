package ru.alexleru.covid19Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alexleru.covid19Widget.Model.ModelCovidLastData;
import ru.alexleru.covid19Widget.Service.NetworkService;

public class Covid19Widget extends AppWidgetProvider {

    public static void getResponse(final Context context, final AppWidgetManager appWidgetManager,
                                   final int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.covid19_widget);
        Intent intentToConfig = new Intent(context, MainActivity.class);
        intentToConfig.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId,
                intentToConfig, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.settings_layout, pendingIntent);
        int position = Covid19App.loadConfig(appWidgetId, context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        NetworkService.getNetworkService()
                .getAPI().getCovidLastData()
                .enqueue(new Callback<List<ModelCovidLastData>>() {
                    @Override
                    public void onResponse(Call<List<ModelCovidLastData>> call, Response<List<ModelCovidLastData>> response) {
                        List<ModelCovidLastData> ModelCovidLastData = response.body();
                        remoteViews.setTextViewText(R.id.date, stringConvertToData(ModelCovidLastData.get(position).date));
                        remoteViews.setTextViewText(R.id.country, String.valueOf(ModelCovidLastData.get(position).country));
                        remoteViews.setTextViewText(R.id.count_confirmed, intToStringConvertor(ModelCovidLastData.get(position).sumC, "%,d"));
                        remoteViews.setTextViewText(R.id.count_recovered, intToStringConvertor(ModelCovidLastData.get(position).sumR, "%,d"));
                        remoteViews.setTextViewText(R.id.change_count_confirmed, intToStringConvertor(ModelCovidLastData.get(position).sumC_change, "+%,d"));
                        remoteViews.setTextViewText(R.id.change_count_recovered, intToStringConvertor(ModelCovidLastData.get(position).sumR_change, "+%,d"));
                        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

                    }

                    @Override
                    public void onFailure(Call<List<ModelCovidLastData>> call, Throwable t) {
                    }
                });
    }

    private static String intToStringConvertor(int value, String format) {
        String result = String.format(Locale.CANADA_FRENCH, format, value);
        return value == 0 ? "0" : result;
    }

    private static String stringConvertToData(String date) {
        String[] strings = date.split("-");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(strings[0]),
                Integer.parseInt(strings[1]) - 1,
                Integer.parseInt(strings[2]));
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.UK);
        String s = "Data for " + dateFormat.format(calendar.getTime());
        return s;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            getResponse(context, appWidgetManager, appWidgetId);
        }
    }
}
