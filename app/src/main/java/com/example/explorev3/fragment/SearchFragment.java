package com.example.explorev3.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.explorev3.FilterSearchDialog;
import com.example.explorev3.activity.MainSwipeActivity;
import com.example.explorev3.adapter.PlaceAdapter;
import com.example.explorev3.activity.PlaceDetailActivity;
import com.example.explorev3.pojo.FilterData;
import com.example.explorev3.pojo.PlaceItem;
import com.example.explorev3.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment implements PlaceAdapter.OnItemClickListener, View.OnClickListener{

    public static final String EXTRA_XID = "xid";
    private static final int REQUEST_LOCATION_PERMISSION = 0;

    String JSON_URL = "";

    private RelativeLayout noResultView;
    private RecyclerView mRecyclerView;
    private PlaceAdapter mPlaceAdapter;
    private EditText searchView;
    private ImageView filterSearch, btnSearch;
    private View view;
    private ArrayList<PlaceItem> mPlaceList;
    private RequestQueue mRequestQueue;

    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private String deviceLat, deviceLon;

    private String filterRadius, filterName;
    private ArrayList<String> checkResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        // getting device current location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLocation();

        searchView = view.findViewById(R.id.search_view);
        filterSearch = view.findViewById(R.id.search_filter);
        btnSearch = view.findViewById(R.id.search_btn);
        noResultView = view.findViewById(R.id.search_no_item);

        if (getArguments() != null) {
            searchView.setText(getArguments().getString("COBA"));
        }

        mRecyclerView = view.findViewById(R.id.recycle_view_place_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mPlaceList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(view.getContext());

        btnSearch.setOnClickListener(this);
        filterSearch.setOnClickListener(this);

        mPlaceAdapter = new PlaceAdapter(view.getContext(), mPlaceList);
        mRecyclerView.setAdapter(mPlaceAdapter);
        mPlaceAdapter.setOnItemClickListener(SearchFragment.this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                parseJSON();
                break;

            case R.id.search_filter:
                filterSearchDialog();
                break;
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("GetLocation", "getLocation: permissions granted");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLastLocation = location;
                        deviceLat = Double.toString(mLastLocation.getLatitude());
                        deviceLon = Double.toString(mLastLocation.getLongitude());

                    } else {
                        Toast.makeText(getContext(), "Failed to retrieve location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getContext(), PlaceDetailActivity.class);
        PlaceItem clickedItem = mPlaceList.get(position);

        String rating = Integer.toString(clickedItem.getmPlaceRating());

        detailIntent.putExtra(EXTRA_XID, clickedItem.getmXid());
        detailIntent.putExtra("EXTRA_RATING", rating);
        detailIntent.putExtra("EXTRA_NAME", clickedItem.getmPlaceName());
        detailIntent.putExtra("EXTRA_LAT", clickedItem.getmLatt());
        detailIntent.putExtra("EXTRA_LON", clickedItem.getmLon());

        startActivity(detailIntent);
    }


    private void parseJSON() {

        String jsonURL = null;
        try {
            MainSwipeActivity activity = (MainSwipeActivity) getActivity();
            FilterData filter = activity.getData();

            Log.d("bruhh", "checkSomething: " + filter);
            Log.d("bruhh", "checkAll: " + filter.getRadius());
            Log.d("bruhh", "checkAll: " + filter.getLatt());
            Log.d("bruhh", "checkAll: " + filter.getLon());
            Log.d("bruhh", "checkAll: " + filter.getCheckResult());

            String checkKinds, name, lon, latt, radius;

            if (filter.getCheckResult() == null || filter.getCheckResult().size() < 1) {
                checkKinds = "";
            } else {
                StringBuffer result = new StringBuffer();

                for (String kind : filter.getCheckResult()) {
                    result.append(kind);
                    result.append("%2C");
                }
                String kinds = result.toString();
                int n = kinds.length() - 1;
                kinds = kinds.substring(0, n-2);
                checkKinds = "&kinds=" + kinds;
            }

            if (searchView.getText().toString().trim().length() < 3 || searchView.getText().toString().trim() == null) {
                name = "";
            } else {
                name = "&name=" + searchView.getText().toString().trim();
            }

            if (filter.getLatt() == null || filter.getLatt() == "" || filter.getLatt().isEmpty()) {
                latt = "&lat=" + deviceLat;
            } else {
                latt = "&lat=" + filter.getLatt();
            }

            if (filter.getLon() == null || filter.getLon() == "" || filter.getLon().isEmpty()) {
                lon = "&lon=" + deviceLon;
            } else {
                lon = "&lon=" + filter.getLon();
            }

            if (filter.getRadius() == null || filter.getRadius().isEmpty() ) {
                filterRadius = Integer.toString(100 * 1000);
                radius = "radius=" + filterRadius;
            } else {
                radius = "radius=" + filter.getRadius() + "000";
            }

            jsonURL = "https://api.opentripmap.com/0.1/en/places/radius?" +
                    radius +
                    lon +
                    latt +
                    checkKinds +
                    name +
                    "&format=json&limit=200&apikey=5ae2e3f221c38a28845f05b65ba166329393551235361ab9b66e2889";

            Log.d("bruhh", "Link jadi: " + jsonURL);
        } catch (Exception e) {
            Log.e("bruhh", "Error nih: " + e );
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, jsonURL, null,
                response -> {
                    try {
                        mPlaceList.clear();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject place = response.getJSONObject(i);

                            String xid = place.getString("xid");
                            String placeName = place.getString("name");
                            int placeRating = place.getInt("rate");
                            String latt = Double.toString(place.getJSONObject("point").getDouble("lat"));
                            String lon = Double.toString(place.getJSONObject("point").getDouble("lon"));

                            mPlaceList.add(new PlaceItem(xid, placeName, placeRating, latt, lon));
                        }

                        mPlaceAdapter.notifyDataSetChanged();

                        if (mPlaceList.size() < 1 || mPlaceList == null) {
                            noResultView.setVisibility(View.VISIBLE);
                        } else {
                            noResultView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {

                    }
                }, error -> error.printStackTrace());

        mRequestQueue.add(request);
    }

    public void filterSearchDialog() {
        FilterSearchDialog filterSearchDialog = new FilterSearchDialog();
        filterSearchDialog.show(getFragmentManager(), "Filter Search");
    }

}
