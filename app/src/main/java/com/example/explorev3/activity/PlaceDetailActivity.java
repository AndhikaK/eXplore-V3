package com.example.explorev3.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.explorev3.FavoriteContract;
import com.example.explorev3.FavoriteDBHelper;
import com.example.explorev3.R;
import com.example.explorev3.fragment.FavoritesFragment;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.explorev3.fragment.SearchFragment.EXTRA_XID;

public class PlaceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;
    double placeLong;
    double placeLatt;

    private SQLiteDatabase mDatabase;

    String mPlaceName, mPlaceLat, mPlaceLon, mPlaceRating, mPlaceXID;

    TextView[] mStar;
    LinearLayout mStarLayout;
    RelativeLayout imageLoading, btnFavorite;

    String placeName;
    TextView detailCategory, detailName, detailAddress, detailPlace;
    ImageView detailImage;
    Button btnIntentMaps;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        FavoriteDBHelper dbHelper = new FavoriteDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        String xid = intent.getStringExtra(EXTRA_XID);


        mPlaceXID = xid;
        mPlaceName = intent.getStringExtra("EXTRA_NAME");
        mPlaceLat = intent.getStringExtra("EXTRA_LAT");
        mPlaceLon = intent.getStringExtra("EXTRA_LON");
        mPlaceRating = intent.getStringExtra("EXTRA_RATING");

        btnFavorite = findViewById(R.id.favorites_btn);
//        imageLoading = findViewById(R.id.image_loading);
        mStarLayout = findViewById(R.id.detail_rating);
        requestQueue = Volley.newRequestQueue(this);
        detailName = findViewById(R.id.detailName);
        detailAddress = findViewById(R.id.detailAddress);
        detailPlace = findViewById(R.id.detailPlace);
        detailImage = findViewById(R.id.detailImage);

        detailName.setText(mPlaceName);

        btnIntentMaps = findViewById(R.id.btn_navigate_map);
        btnIntentMaps.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);

        addRatingIcon();

        parseJSON(xid);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_navigate_map:
                Uri gmmURI = Uri.parse("geo:" + mPlaceLat + "," + mPlaceLon + "?z=18&q=" + mPlaceName);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmURI);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Can't run maps", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.favorites_btn:
                addFavorite();
                break;
        }
    }

    private void addRatingIcon() {
        int rating = Integer.parseInt(mPlaceRating);
        mStar = new TextView[rating];
        mStarLayout.removeAllViews();

        for (int i = 0; i < mStar.length; i++) {
            mStar[i] = new TextView(this);
            mStar[i].setText(Html.fromHtml("&#9733"));
            mStar[i].setTextSize(35);
            mStar[i].setTextColor(getResources().getColor(R.color.star));
            mStar[i].setPadding(5, 0, 5, 0);

            mStarLayout.addView(mStar[i]);
        }
    }

    private void parseJSON(String xid) {
        String JSON_URL = "https://api.opentripmap.com/0.1/en/places/xid/" + xid + "?apikey=5ae2e3f221c38a28845f05b65ba166329393551235361ab9b66e2889";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String dataAddress = response.getJSONObject("address").getString("village") + ", " +
                            response.getJSONObject("address").getString("state");
                    String dataPlace = response.getJSONObject("wikipedia_extracts").getString("text");
                    String dataImage = response.getJSONObject("preview").getString("source");

                    // Formatting url karena gk sesuai
                    dataImage = dataImage.replaceFirst("thumb/", "");
                    int lastIndex = dataImage.indexOf(".jpg/") + 4;
                    dataImage = dataImage.substring(0, lastIndex);

                    detailAddress.setText(dataAddress);
                    detailPlace.setText(dataPlace);

//                    imageLoading.setVisibility(View.INVISIBLE);

                    Picasso.get().load(dataImage).into(detailImage);
                    detailImage.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
//                    Toast.makeText(PlaceDetailActivity.this, "Something when wrong!", Toast.LENGTH_SHORT).show();
                    detailAddress.setText("Position\n  Lat : " + mPlaceLat + "\n  Lon : " + mPlaceLon);
                    detailPlace.setText("This place doesn't has any detail");

//                    imageLoading.setVisibility(View.INVISIBLE);
                    detailImage.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.getMessage());
            }
        });
        requestQueue.add(objectRequest);
    }

    private void addFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLLUMN_XID, mPlaceXID);
        cv.put(FavoriteContract.FavoriteEntry.COLLUMN_NAME, mPlaceName);
        cv.put(FavoriteContract.FavoriteEntry.COLLUMN_LAT, mPlaceLat);
        cv.put(FavoriteContract.FavoriteEntry.COLLUMN_LON, mPlaceLon);
        cv.put(FavoriteContract.FavoriteEntry.COLLUMN_RATING, mPlaceRating);

        mDatabase.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, cv);

        Toast.makeText(this, mPlaceName + " added to favorites", Toast.LENGTH_SHORT).show();
    }
}