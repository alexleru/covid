package ru.alexleru.covid19Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alexleru.covid19Widget.Adapter.CovidAdapter;
import ru.alexleru.covid19Widget.Model.ModelCovidLastData;
import ru.alexleru.covid19Widget.Service.NetworkService;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<ModelCovidLastData> modelCovidLastData;
    List<ModelCovidLastData> modelCovidLastDataFilter;
    SearchView searchView;
    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        views();
        getResponse();
        intentExtras();
        if (!isNetworkConnection()) {
            dialogAlert(R.string.no_internet);
        }

    }

    private void views() {
        listView = findViewById(R.id.list_layout);
        searchView = findViewById(R.id.search_field);
        listView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    int positionInitialList = modelCovidLastData.indexOf(modelCovidLastDataFilter.get(position));
                    createWidget(getBaseContext(), positionInitialList);
                }
        );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setAdapterListView(filterList(modelCovidLastData, query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setAdapterListView(filterList(modelCovidLastData, newText));
                return false;
            }
        });
    }

    private void intentExtras() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    private boolean isNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    private void createWidget(Context context, int position) {
        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            Covid19App.saveConfig(appWidgetId, position, getBaseContext());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Covid19Widget.getResponse(context, appWidgetManager, appWidgetId);

            Intent intentToWidget = new Intent();
            intentToWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, intentToWidget);
            finish();
        } else {
            dialogAlert(R.string.alert_message);
        }
    }

    private void dialogAlert(@StringRes int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_title);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.alert_button, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getResponse() {
        NetworkService.getNetworkService()
                .getAPI().getCovidLastData()
                .enqueue(new Callback<List<ModelCovidLastData>>() {
                    @Override
                    public void onResponse(Call<List<ModelCovidLastData>> call, Response<List<ModelCovidLastData>> response) {
                        modelCovidLastData = response.body();
                        modelCovidLastDataFilter = modelCovidLastData;
                        setAdapterListView(modelCovidLastData);
                    }

                    @Override
                    public void onFailure(Call<List<ModelCovidLastData>> call, Throwable t) {
                        dialogAlert(R.string.server_not_respond);
                    }
                });
    }

    private void setAdapterListView(List<ModelCovidLastData> modelCovidLastData) {
        CovidAdapter covidAdapter = new CovidAdapter(getBaseContext(), modelCovidLastData);
        listView.setAdapter(covidAdapter);
    }

    private List<ModelCovidLastData> filterList(List<ModelCovidLastData> initialList, String s) {
        List<ModelCovidLastData> newList = new ArrayList<>();
        for (ModelCovidLastData item : initialList) {
            if (item.country.toLowerCase().contains(s.toLowerCase())) {
                newList.add(item);
            }
        }
        return modelCovidLastDataFilter = newList;
    }
}
