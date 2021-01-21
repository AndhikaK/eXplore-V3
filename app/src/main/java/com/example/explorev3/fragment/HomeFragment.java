package com.example.explorev3.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.explorev3.R;
import com.example.explorev3.activity.MainSwipeActivity;
import com.example.explorev3.pojo.FilterData;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private TextView tvHello;

    String userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvHello = view.findViewById(R.id.welcome_tv);

        MainSwipeActivity activity = (MainSwipeActivity) getActivity();

        new Handler().postDelayed((Runnable) () -> {
            userName = activity.getUserName();
            tvHello.setText("Hello, " + userName);
        }, 5000);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}
