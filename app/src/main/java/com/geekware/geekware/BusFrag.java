package com.geekware.geekware;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.geekware.geekware.R;

import java.io.File;
import java.util.ArrayList;

public class BusFrag extends Fragment {

    int pgn;
    static ArrayList<String> from_list;
    public static BusFrag create(int pagenumber,ArrayList<String> from_l){
        BusFrag fragment = new BusFrag();
        Bundle args = new Bundle();
        args.putInt("pgn", pagenumber);
        fragment.setArguments(args);
        from_list = from_l;
        return fragment;

    }
    public BusFrag(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pgn = getArguments().getInt("pgn");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.text_fragment,container,false);
        ((TextView)vg.findViewById(R.id.bus_from)).setText(from_list.get(pgn));
        return vg;
    }

    public int getPageNumber() {
        return pgn;
    }
}
