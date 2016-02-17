package com.geekware.geekware;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BusActivity extends ActionBarActivity {

    Button prev, next;
    TextView td;
    ListView lv;
    String from;
    final Context ct = BusActivity.this;
    Custom_bus_adapter adapter;
    ArrayList<Custom_bus_Item> list;
    ArrayList<String> from_list;
    String today;
    int Day, day;
    Calendar calendar;
    private static final String[] Daynames = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    ViewPager pager;
    PagerAdapter pgadapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        td = (TextView) findViewById(R.id.day);
        lv = (ListView) findViewById(R.id.buslv);
        calendar = Calendar.getInstance();
        Day = calendar.get(Calendar.DAY_OF_WEEK);
        day = Day;
        today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        td.setText(Daynames[Day]);
        list = new ArrayList<>();
        from_list = new ArrayList<>();
        from = "";


        // list.add(new Custom_bus_Item("", "loading", "", "", 0, ""));
        adapter = new Custom_bus_adapter(this, list);
        lv.setAdapter(adapter);
        lv.setEnabled(false);
        //adapter.notifyDataSetChanged();




        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utilities.isNetworkAvailable(getApplication())) {
                    Toast.makeText(BusActivity.this, "No Network Available"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("busTime");
                    query.fromLocalDatastore();
                    ParseObject.unpinAll(query.find());

                    new Loadbuses().execute();
                    // new Loadbuses().execute();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Day--;
                Day = mod(Day - 1, 7);
                Day++;
                td.setText(Daynames[Day]);
                from_list.clear();
                from = "";
                pgadapter = new fragadapter(getFragmentManager());
                pager.setAdapter(pgadapter);
                new Loadbuses().execute();
                pager.setCurrentItem(1, true);
                if (Day != day) {
                    ((RelativeLayout) findViewById(R.id.bus_relativelayout)).removeView(pager);
                    pager.setBackgroundColor(((RelativeLayout) findViewById(R.id.bus_relativelayout)).getSolidColor());

                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Day--;
                Day = mod(Day + 1, 7);
                Day++;
                td.setText(Daynames[Day]);
                from_list.clear();
                from = "";
                pgadapter = new fragadapter(getFragmentManager());
                pager.setAdapter(pgadapter);
                new Loadbuses().execute();
                pager.setCurrentItem(1, true);
                if (Day != day) {
                    ((RelativeLayout) findViewById(R.id.bus_relativelayout)).removeView(pager);
                    pager.setBackgroundColor(((RelativeLayout) findViewById(R.id.bus_relativelayout)).getSolidColor());
                }

            }
        });


        new Loadbuses().execute();

        pager = (ViewPager) findViewById(R.id.pger);
        pgadapter = new fragadapter(getFragmentManager());
        pager.setAdapter(pgadapter);
        pager.setBackgroundColor(((RelativeLayout) findViewById(R.id.bus_relativelayout)).getSolidColor());
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
               /* Toast.makeText(LectNotesActivity.this, "changing to "+position
                        , Toast.LENGTH_SHORT).show();*/


                from = from_list.get(position);
                new Loadbuses().execute();
                invalidateOptionsMenu();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    int mod(int a, int b) {
        int res = a % b;
        return (res < 0) ? res + b : res;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Bus Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.geekware.geekware/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Bus Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.geekware.geekware/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    enum Taskstate {
        SUCCESS, NO_INTERNET, EXCEPTION_THROWN, OFFLINE
    }


    ProgressDialog pd;




    class Loadbuses extends AsyncTask<Void, String, Taskstate> {
        boolean is_offline;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BusActivity.this);
            pd.setMessage("Downloading data..." + "\n please be patient,this may take few seconds");
            if (need_forceupdate()) {
                pd.show();
                Toast.makeText(BusActivity.this, "from server " + Day + " " + today
                        , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BusActivity.this, "from offline " + str + " " + today
                        , Toast.LENGTH_SHORT).show();
            }

        }

        String str;

        boolean need_forceupdate() {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("busTime");
            query.fromLocalDatastore();
            query.whereContains("day_avail", Day + "");
            try {
                ParseObject obj = query.getFirst();
                if (obj == null)
                    return true;
                str = obj.getString("stored_at");
                if (obj.getString("stored_at").equals(today)) {
                    return false;
                } else return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return true;
            }
        }

        String error = "";

        int compare_time(String depart, String now) {
            String[] dep = depart.split(":");
            String[] nw = now.split(":");
            System.out.println("\n\n\n\n\n\n" + depart + "-----" + dep[0] + "  " + dep[1] + "\n\n\n\n\n\n");
            System.out.println("\n\n\n\n\n\n" + now + "------" + nw[0] + "  " + nw[1] + "\n\n\n\n\n\n");
            int dH = Integer.parseInt(dep[0]),
                    dM = Integer.parseInt(dep[1]);
            int nH = Integer.parseInt(nw[0]),
                    nM = Integer.parseInt(nw[1]);
            if (dH > nH)
                return 1;
            else if (dH < nH)
                return -1;
            else if (dM > nM)
                return 1;
            else
                return -1;
        }

        @Override
        protected Taskstate doInBackground(Void... params) {
            from_list.clear();
            if (!Utilities.isNetworkAvailable(getApplication()))
                return Taskstate.NO_INTERNET;
            if (!need_forceupdate()) {
                is_offline = true;
                return Taskstate.OFFLINE;
            }
            is_offline = false;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("busTime");
            query.addAscendingOrder("depart");
            List<ParseObject> buslist;
            try {
                buslist = query.find();

                for (ParseObject obj : buslist) {
                    obj.put("stored_at", today);
                }
                ParseObject.pinAllInBackground(buslist);
                Thread.sleep(500);// else It will not load data , cuz it is taking time to save data in database
                return Taskstate.OFFLINE;
            } catch (ParseException e) {
                e.printStackTrace();
                return Taskstate.EXCEPTION_THROWN;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Taskstate.EXCEPTION_THROWN;
            }

            /*List<ParseObject> buslist;
            try {
                buslist=query.find();

                if(buslist.isEmpty()){
                    list.add(new Custom_bus_Item("","No Buses","","",0,"",0));
                }
                for(ParseObject obj:buslist){
                    obj.put("stored_at", today);
                }
                ParseObject.pinAllInBackground(buslist);
                int x=0,p=0;
                String time_now=new SimpleDateFormat("HH:mm").format(calendar.getTime());
                ArrayList<String> addedtohighlight = new ArrayList<>();

                for(ParseObject obj:buslist){
                    if(!from_list.contains(obj.getString("from")))
                        from_list.add(obj.getString("from"));
                }
                for(ParseObject obj:buslist){
                    x=0;
                    if(p==0&&compare_time(obj.getString("depart"),time_now)==1){
                        p=1;
                    }

                    if(!from_list.isEmpty()){
                        if(p==1){
                            if(addedtohighlight.contains(obj.getString("to"))&&obj.getString("from").equals(from)){
                                addedtohighlight.add(obj.getString("to"));
                                x++;
                                System.out.println("\n\n***********************************************************\n");
                            }
                        }
                    }
                    list.add(new Custom_bus_Item(obj.getString("depart"), obj.getString("from"), obj.getString("to"), obj.getString("day_avail"), obj.getInt("bus_no"), obj.getString("users"),x));
                    //publishProgress(obj.getString("lecture"));
                }
                x=0;


            } catch (ParseException e) {
                e.printStackTrace();
                error=e.getMessage();
                System.out.println("  \n\n\n\n\n\n\n\n\n\n\n\n" + e.getMessage() + "  !! " + e.toString());

                return Taskstate.EXCEPTION_THROWN;
            }*/

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //pd.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Taskstate taskstate) {
            super.onPostExecute(taskstate);
            pd.setMessage("");
            pd.dismiss();
            /*if(is_offline){
                Toast.makeText(BusActivity.this, "loading Data from OFFLINE"
                        , Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(BusActivity.this, "Data from Server"
                        , Toast.LENGTH_SHORT).show();
            }*/
            if (taskstate == Taskstate.EXCEPTION_THROWN) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BusActivity.this);
                builder.setMessage("Exception Thrown")
                        .setTitle("exception! Day-" + error)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (taskstate == Taskstate.NO_INTERNET || taskstate == Taskstate.OFFLINE) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MylecturesActivity.this);
                builder.setMessage(R.string.no_internet_msg)
                        .setTitle(R.string.no_internet_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();*/


                ParseQuery<ParseObject> query = ParseQuery.getQuery("busTime");
                query.fromLocalDatastore();
                query.whereContains("day_avail", Day + "");
                query.addAscendingOrder("depart");

                list.clear();
                List<ParseObject> lectlist;
                try {
                    lectlist = query.find();

                    if (lectlist.isEmpty()) {
                        list.add(new Custom_bus_Item("", "No Buses", "", "", 0, "", 0));
                    }
                    int x = 0, p = 0;
                    String time_now = new SimpleDateFormat("HH:mm").format(calendar.getTime());
                    ArrayList<String> addedtohighlight = new ArrayList<>();
                    from_list.clear();
                    if (Day == day)
                        for (ParseObject obj : lectlist) {
                            if (!from_list.contains(obj.getString("from")))
                                from_list.add(obj.getString("from"));
                        }
                    if (from.equals(""))
                        from = lectlist.get(0).getString("from");
                    for (ParseObject obj : lectlist) {
                        x = 0;
                        if (p == 0 && compare_time(obj.getString("depart"), time_now) == 1) {
                            p = 1;
                        }
                        if (!from_list.isEmpty()) {
                            System.out.println("\n\n**************" + obj.getString("to") + p + obj.getString("from") + "/" + from + "************\n");
                            if (p == 1) {
                                if ((!addedtohighlight.contains(obj.getString("to"))) && obj.getString("from").equals(from)) {
                                    addedtohighlight.add(obj.getString("to"));
                                    x++;
                                    System.out.println("\n\n---------------------------" + obj.getString("to") + "--------------------------\n");
                                }
                            }
                        }
                        list.add(new Custom_bus_Item(obj.getString("depart"), obj.getString("from"), obj.getString("to"), obj.getString("day_avail"), obj.getInt("bus_no"), obj.getString("users"), x));

                    }
                    if (lectlist.isEmpty()) {
                        list.add(new Custom_bus_Item("", "", "Empty", "", 4, "u", 1));

                    }
                    addedtohighlight.clear();

                    lv.setEnabled(true);
                    adapter.notifyDataSetChanged();
                    pgadapter.notifyDataSetChanged();
                } catch (ParseException e) {
                    e.printStackTrace();
                    error = e.getMessage();
                    System.out.println("  \n\n\n\n\n\n\n\n\n\n\n\n" + e.getMessage() + "  !! " + e.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(BusActivity.this);
                    builder.setMessage(R.string.no_internet_msg)
                            .setTitle(R.string.no_internet_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }


            } else if (taskstate == Taskstate.SUCCESS) {
                lv.setEnabled(true);
                pgadapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        }
    }


    private class fragadapter extends fspa {


        public fragadapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return BusFrag.create(position, from_list);
        }

        @Override
        public int getCount() {
            return from_list.size();
        }
    }


}
