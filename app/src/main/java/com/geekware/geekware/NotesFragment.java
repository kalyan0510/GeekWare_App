package com.geekware.geekware;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

/**
 * Created by gkalyan0510 on 12/18/2015.
 */
public class NotesFragment extends Fragment {

    int pgn;
    public static NotesFragment create(int pagenumber){
         NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putInt("pgn", pagenumber);
        fragment.setArguments(args);
        return fragment;
    }
    public NotesFragment(){

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
        if(pgn==0){
            ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.notes_fragment,container,false);
            Button op=(Button)vg.findViewById(R.id.openbut);
            op.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file=new File("/mnt/sdcard/ds.ppt");
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path,"application/vnd.ms-powerpoint");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            return vg;
        }
        else if(pgn==1){
            ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.tutorial_fragment,container,false);
            return vg;
        }
        else
        {
            ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.feedback_fragment,container,false);
            return vg;
        }

    }

    public int getPageNumber() {
        return pgn;
    }
}
