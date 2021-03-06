package com.geekware.geekware;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment.SavedState;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public abstract class fspa extends PagerAdapter {
    private static final String TAG = "FragmentStatePagerAdapter";
    private static final boolean DEBUG = false;
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private ArrayList<SavedState> mSavedState = new ArrayList();
    private ArrayList<Fragment> mFragments = new ArrayList();

    public fspa(FragmentManager fm) {
        this.mFragmentManager = fm;
    }

    public abstract Fragment getItem(int var1);

    public void startUpdate(View container) {
    }

    public Object instantiateItem(View container, int position) {
        Fragment fragment;
        if(this.mFragments.size() > position) {
            fragment = (Fragment)this.mFragments.get(position);
            if(fragment != null) {
                return fragment;
            }
        }

        if(this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        fragment = this.getItem(position);
        if(this.mSavedState.size() > position) {
            SavedState fss = (SavedState)this.mSavedState.get(position);
            if(fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }

        while(this.mFragments.size() <= position) {
            this.mFragments.add((Fragment)null);
        }

        this.mFragments.set(position, fragment);
        this.mCurTransaction.add(container.getId(), fragment);
        return fragment;
    }

    public void destroyItem(View container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if(this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        while(this.mSavedState.size() <= position) {
            this.mSavedState.add((Fragment.SavedState)null);
        }

        this.mSavedState.set(position, this.mFragmentManager.saveFragmentInstanceState(fragment));
        this.mFragments.set(position, (Fragment)null);
        this.mCurTransaction.remove(fragment);
    }

    public void finishUpdate(View container) {
        if(this.mCurTransaction != null) {
            this.mCurTransaction.commit();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }

    }

    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    public Parcelable saveState() {
        Bundle state = null;
        if(this.mSavedState.size() > 0) {
            state = new Bundle();
            SavedState[] i = new SavedState[this.mSavedState.size()];
            this.mSavedState.toArray(i);
            state.putParcelableArray("states", i);
        }

        for(int var5 = 0; var5 < this.mFragments.size(); ++var5) {
            Fragment f = (Fragment)this.mFragments.get(var5);
            if(f != null) {
                if(state == null) {
                    state = new Bundle();
                }

                String key = "f" + var5;
                this.mFragmentManager.putFragment(state, key, f);
            }
        }

        return state;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
        if(state != null) {
            Bundle bundle = (Bundle)state;
            bundle.setClassLoader(loader);
            Parcelable[] fss = bundle.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if(fss != null) {
                for(int keys = 0; keys < fss.length; ++keys) {
                    this.mSavedState.add((SavedState)fss[keys]);
                }
            }

            Set var10 = bundle.keySet();
            Iterator i$ = var10.iterator();

            while(true) {
                while(true) {
                    String key;
                    do {
                        if(!i$.hasNext()) {
                            return;
                        }

                        key = (String)i$.next();
                    } while(!key.startsWith("f"));

                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = this.mFragmentManager.getFragment(bundle, key);
                    if(f != null) {
                        while(this.mFragments.size() <= index) {
                            this.mFragments.add((Fragment)null);
                        }

                        this.mFragments.set(index, f);
                    } else {
                        Log.w("FragmentStatePagerAdapter", "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}
