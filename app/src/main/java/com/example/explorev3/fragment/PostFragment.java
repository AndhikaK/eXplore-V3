package com.example.explorev3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorev3.AddPostActivity;
import com.example.explorev3.R;
import com.example.explorev3.adapter.PostAdapter;
import com.example.explorev3.pojo.PostItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.local.IndexedQueryEngine;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAddPost;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private DatabaseReference mDatabaseRef;
    private List<PostItem> postItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        fabAddPost = view.findViewById(R.id.fab_add_post);
        recyclerView = view.findViewById(R.id.recycle_view_post_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postItems = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("post");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postItems.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    PostItem postItem = postSnapshot.getValue(PostItem.class);
                    postItems.add(postItem);
                }

                postAdapter = new PostAdapter(getContext(), postItems);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error " + error, Toast.LENGTH_LONG).show();
            }
        });

        fabAddPost.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_post:
                Intent intent = new Intent(getContext(), AddPostActivity.class);
                startActivity(intent);
                break;
        }
    }
}
