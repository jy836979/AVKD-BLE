package com.avkd.humible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avkd.humible.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    List<Notification> items = new ArrayList<>();

    public static Fragment newInstance() {
        Fragment frag = new NotificationsFragment();
        Bundle args = new Bundle();

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_notifications, container, false);


        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcv_notices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items.add(new Notification("Humi", "29분 전", "현재 안방1의 실내 습도가 11%입니다. 가습이 필요합니다."));
        items.add(new Notification("Humi", "1시간 전", "현재 공부방2의 실내 습도가 17%입니다. 가습이 필요합니다."));
        items.add(new Notification("Roomi", "1시간 전", "현재 모든 방에 환기가 필요합니다. ‘강’모드로 작동해주세요."));

        recyclerView.setAdapter(new NotiRecyclerAdapter(getActivity(), items));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDecoration(DisplayUtil.dip2px(getActivity(),15)));

        return view;
    }


}
