package com.example.explorev3.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorev3.FavoriteContract;
import com.example.explorev3.R;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context mContext;
    Cursor mCursor;

    public FavoriteAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorites_item, parent, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLLUMN_NAME));
        String rating = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLLUMN_RATING));
        long id = mCursor.getLong(mCursor.getColumnIndex(FavoriteContract.FavoriteEntry._ID));


        holder.itemView.setTag(id);
        holder.favoriteName.setText(name);
        holder.favoriteRating.setText(rating);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        public TextView favoriteName;
        public TextView favoriteRating;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteName = itemView.findViewById(R.id.favorite_place_name);
            favoriteRating = itemView.findViewById(R.id.favorite_place_rating);
        }
    }
}
