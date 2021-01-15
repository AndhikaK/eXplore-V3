package com.example.explorev3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorev3.pojo.PlaceItem;
import com.example.explorev3.R;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private Context mContext;
    private ArrayList<PlaceItem> mPlaceList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PlaceAdapter(Context mContext, ArrayList<PlaceItem> mPlaceList) {
        this.mContext = mContext;
        this.mPlaceList = mPlaceList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.place_item_card, parent, false);

        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceItem currentItem = mPlaceList.get(position);

//        String xid = currentItem.getmXid();
//        String imageURL = currentItem.getmImagePreview();
        String placeName = currentItem.getmPlaceName();
        int placeRating = currentItem.getmPlaceRating();

        holder.mTextPlaceName.setText(placeName);
        holder.mTextPlaceRating.setText("Rating: " + placeRating);

//        Picasso.get().load(imageURL).into(holder.mImagePreview);
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImagePreview;
        public TextView mTextPlaceName;
        public TextView mTextPlaceRating;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
//            mImagePreview = itemView.findViewById(R.id.image_place_preview);
            mTextPlaceName = itemView.findViewById(R.id.text_place_name);
            mTextPlaceRating = itemView.findViewById(R.id.text_place_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

