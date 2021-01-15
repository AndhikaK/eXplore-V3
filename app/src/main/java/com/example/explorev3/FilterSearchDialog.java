package com.example.explorev3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.explorev3.fragment.SearchFragment;

import java.util.ArrayList;

public class FilterSearchDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private CheckBox checkSport, checkNatural, checkFacilities, checkHistorical, checkCultural, checkAccomodation;
    private EditText edtLatt, edtLong, edtRadius;
    private ArrayList<String> filterResult;
    private FilterDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_search_dialog, null);

        builder.setView(view)
                .setTitle("Search Filter")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String latt = edtLatt.getText().toString().trim();
                        String lon = edtLong.getText().toString().trim();
                        String radius = edtRadius.getText().toString().trim();
                        ArrayList<String> checkResult = filterResult;

                        listener.applyText(latt, lon, radius, checkResult);
                    }
                });

        filterResult = new ArrayList<>();
        checkAccomodation = view.findViewById(R.id.check_accomodation_filter);
        checkCultural = view.findViewById(R.id.check_cultural_filter);
        checkNatural = view.findViewById(R.id.check_natural_filter);
        checkFacilities = view.findViewById(R.id.check_Facilities_filter);
        checkSport = view.findViewById(R.id.check_sport_filter);
        checkHistorical = view.findViewById(R.id.check_historical_filter);
        edtLatt = view.findViewById(R.id.filter_latt);
        edtLong = view.findViewById(R.id.filter_lon);
        edtRadius = view.findViewById(R.id.filter_radius);

        checkHistorical.setOnClickListener(this);
        checkSport.setOnClickListener(this);
        checkNatural.setOnClickListener(this);
        checkCultural.setOnClickListener(this);
        checkFacilities.setOnClickListener(this);
        checkAccomodation.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_accomodation_filter:
                if (checkAccomodation.isChecked())
                    filterResult.add("accomodations");
                else
                    filterResult.remove("accomodations");
                break;
            case R.id.check_Facilities_filter:
                if (checkFacilities.isChecked())
                    filterResult.add("other");
                else
                    filterResult.remove("other");
                break;
            case R.id.check_historical_filter:
                if (checkHistorical.isChecked())
                    filterResult.add("historic");
                else
                    filterResult.remove("historic");
                break;
            case R.id.check_cultural_filter:
                if (checkCultural.isChecked())
                    filterResult.add("cultural");
                else
                    filterResult.remove("cultural");
                break;
            case R.id.check_sport_filter:
                if (checkSport.isChecked())
                    filterResult.add("sport");
                else
                    filterResult.remove("sport");
                break;
            case R.id.check_natural_filter:
                if (checkNatural.isChecked())
                    filterResult.add("natural");
                else
                    filterResult.remove("natural");
                break;

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements FilterDialogListener");
        }
    }

    public interface FilterDialogListener {
        void applyText(String latt, String lon, String radius, ArrayList<String> checkResult);
    }
}
