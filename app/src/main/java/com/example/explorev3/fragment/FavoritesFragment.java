package com.example.explorev3.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorev3.FavoriteAdapter;
import com.example.explorev3.FavoriteContract;
import com.example.explorev3.FavoriteDBHelper;
import com.example.explorev3.R;

public class FavoritesFragment extends Fragment implements View.OnClickListener {

    private SQLiteDatabase mDatabase;
    private FavoriteAdapter mAdapter;
    private Button btnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        FavoriteDBHelper dbHelper = new FavoriteDBHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = view.findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FavoriteAdapter(getContext(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        btnRefresh = view.findViewById(R.id.favorite_refresh);
        btnRefresh.setOnClickListener(this);

        return view;
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteContract.FavoriteEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_refresh:
                mAdapter.swapCursor(getAllItems());
                break;
        }
    }
}
