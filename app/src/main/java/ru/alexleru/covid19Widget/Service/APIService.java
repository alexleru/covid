package ru.alexleru.covid19Widget.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.alexleru.covid19Widget.Model.ModelCovidLastData;


public interface APIService {
    @GET("covid_last_data.php")
    Call<List<ModelCovidLastData>> getCovidLastData();
}
