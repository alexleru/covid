package ru.alexleru.covid19Widget.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

import ru.alexleru.covid19Widget.Model.ModelCovidLastData;
import ru.alexleru.covid19Widget.R;


public class CovidAdapter extends ArrayAdapter<ModelCovidLastData> {

    public CovidAdapter(@NonNull Context context, List<ModelCovidLastData> modelCovidLastDataList) {
        super(context, R.layout.list_layout, modelCovidLastDataList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ModelCovidLastData modelCovidLastData = getItem(position);
        View viewLayout = convertView;
        TextView textCountry;
        TextView textConfirmed;
        if (viewLayout == null) {
            viewLayout = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, null);
        }
        textCountry = viewLayout.findViewById(R.id.text_country);
        textCountry.setText(modelCovidLastData.country);
        textConfirmed = viewLayout.findViewById(R.id.text_confirmed);
        textConfirmed.setText(intToStringConvertor(modelCovidLastData.sumC));

        return viewLayout;
    }
    private static String intToStringConvertor(int value) {
        String result = String.format(Locale.CANADA_FRENCH, "%,d", value);
        return result;
    }
}
