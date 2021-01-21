package com.example.explorev3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorev3.R;
import com.example.explorev3.pojo.PostItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<PostItem> postItem;

    public PostAdapter(Context context, List<PostItem> postItem) {
        this.context = context;
        this.postItem = postItem;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post_item, parent,false);

        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostItem postCurrent = postItem.get(position);

        holder.tvName.setText(postCurrent.getName());
        holder.tvAddress.setText(postCurrent.getAddress());
        holder.tvDesc.setText(postCurrent.getDesc());
        holder.tvPoster.setText("Posted by " + postCurrent.getUid());

        Picasso.get()
                .load(postCurrent.getImgUrl())
                .into(holder.imgPost);
    }

    @Override
    public int getItemCount() {
        return postItem.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPost;
        public TextView tvName, tvAddress, tvDesc, tvPoster;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPost = itemView.findViewById(R.id.img_post);
            tvName = itemView.findViewById(R.id.tv_post_name);
            tvAddress = itemView.findViewById(R.id.tv_post_address);
            tvDesc = itemView.findViewById(R.id.tv_post_desc);
            tvPoster = itemView.findViewById(R.id.tv_post_poster);

        }
    }

}
