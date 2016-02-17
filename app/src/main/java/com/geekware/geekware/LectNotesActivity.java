package com.geekware.geekware;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LectNotesActivity extends FragmentActivity{
    ViewPager pager;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lect_notes);
        pager= (ViewPager)findViewById(R.id.pager);
        adapter = new fragadapter(getFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
               /* Toast.makeText(LectNotesActivity.this, "changing to "+position
                        , Toast.LENGTH_SHORT).show();*/
                invalidateOptionsMenu();
            }
        });
    }

    private class fragadapter extends fspa {


        public fragadapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return NotesFragment.create(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}