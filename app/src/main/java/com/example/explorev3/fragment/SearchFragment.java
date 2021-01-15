package com.example.explorev3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.explorev3.FilterSearchDialog;
import com.example.explorev3.MainSwipeActivity;
import com.example.explorev3.adapter.PlaceAdapter;
import com.example.explorev3.PlaceDetailActivity;
import com.example.explorev3.pojo.FilterData;
import com.example.explorev3.pojo.PlaceItem;
import com.example.explorev3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements PlaceAdapter.OnItemClickListener, View.OnClickListener{

    public static final String EXTRA_XID = "xid";

    String JSON_URL = "";

    private RelativeLayout noResultView;
    private RecyclerView mRecyclerView;
    private PlaceAdapter mPlaceAdapter;
    private EditText searchView;
    private ImageView filterSearch, btnSearch;
    private View view;
    private ArrayList<PlaceItem> mPlaceList;
    private RequestQueue mRequestQueue;

    private String filterLatt, filterLon, filterRadius, filterName;
    private ArrayList<String> checkResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

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
            FilterData data = activity.getData();

            Log.d("bruhh", "checkSomething: " + data);
            Log.d("bruhh", "checkAll: " + data.getRadius());
            Log.d("bruhh", "checkAll: " + data.getLatt());
            Log.d("bruhh", "checkAll: " + data.getLon());
            Log.d("bruhh", "checkAll: " + data.getCheckResult());

            String checkKinds, name, lon, latt, radius;

            if (data.getCheckResult() == null || data.getCheckResult().size() < 1) {
                checkKinds = "";
            } else {
                StringBuffer result = new StringBuffer();

                for (String kind : data.getCheckResult()) {
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

            if (data.getLatt() == null || data.getLatt() == "" || data.getLatt().isEmpty()) {
                filterLatt = "-4.281609";
                latt = "&lat=" + filterLatt;
            } else {
                latt = "&lat=" + data.getLatt();
            }

            if (data.getLon() == null || data.getLon() == "" || data.getLon().isEmpty()) {
                filterLon = "104.319946";
                lon = "&lon=" + filterLon;
            } else {
                lon = "&lon=" + data.getLon();
            }

            if (data.getRadius() == null || data.getRadius().isEmpty() ) {
                filterRadius = Integer.toString(100 * 1000);
                radius = "radius=" + filterRadius;
            } else {
                radius = "radius=" + data.getRadius() + "000";
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
