package com.geekware.geekware;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MylecturesActivity extends Activity {

    ListView lvlect ;
    ArrayList<Custom_lect_list_item> list;
    Custom_lect_list_adapter adapter;
    int Day;
    String today;
    String batch;
    private static final String[] Daynames = { "","Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mylectures);
        lvlect= (ListView)findViewById(R.id.ltvlect);
        Calendar calendar = Calendar.getInstance();
        Day=calendar.get(Calendar.DAY_OF_WEEK);
        today=  new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        batch= "btech-2-cs";
        list= new ArrayList<>();
        list.add(new Custom_lect_list_item("Loading... ", "", ""));
        adapter = new Custom_lect_list_adapter(this,list);
        lvlect.setAdapter(adapter);
        lvlect.setEnabled(false);
        adapter.notifyDataSetChanged();
        new LoadlecturesTask()
                .execute();
        ((TextView)findViewById(R.id.daylect)).setText(Daynames[Day]);
        ((Button)findViewById(R.id.refbut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utilities.isNetworkAvailable(getApplication())) {
                    Toast.makeText(MylecturesActivity.this, "No Network Available"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("myLect");
                    query.fromLocalDatastore();
                    ParseObject.unpinAll(query.find());
                    new Loadalllects().execute();
                    new LoadlecturesTask().execute();
                    // new Loadbuses().execute();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        new Loadalllects().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mylectures, menu);
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

    enum Taskstate{
        SUCCESS,NO_INTERNET,EXCEPTION_THROWN,OFFLINE
    }

    ProgressDialog pd;
    class Loadalllects extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            if (!Utilities.isNetworkAvailable(getApplication())) {
                return null;
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("myLect");
            List<ParseObject> lectlist;
            try {
                lectlist=query.find();
                for(ParseObject obj:lectlist){
                    obj.put("stored_at", today);
                }
                ParseObject.pinAllInBackground(lectlist);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    class LoadlecturesTask extends AsyncTask<Void, String,Taskstate >{

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(MylecturesActivity.this);


            pd.setMessage("Loading your \n Timetable "+need_forceupdate());
            pd.show();
        }
        String tdla = "";
        boolean need_forceupdate(){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("myLect");
            query.fromLocalDatastore();
            query.whereEqualTo("day",Day);
            query.whereEqualTo("batch",batch);

            try {
                ParseObject obj =query.getFirst();
               // Toast.makeText(MylecturesActivity.this, "stored- "+obj.getString("stored_at")+" today-"+today, Toast.LENGTH_SHORT).show();
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                tdla=obj.getString("stored_at")+" "+today+" "+obj.getString("stored_at").equals(today);
                if(obj.getString("stored_at").equals(today)){
                    return false;
                }

                else return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return true;
            }
        }
        String error="";

        @Override
        protected Taskstate doInBackground(Void... params) {
            if(!Utilities.isNetworkAvailable(getApplication()))
                return Taskstate.NO_INTERNET;

           if(!need_forceupdate()){
                return Taskstate.OFFLINE;
            }

            ParseQuery<ParseObject> query = ParseQuery.getQuery("myLect");
            query.whereEqualTo("day",Day);
            query.whereEqualTo("batch", batch);
            query.addAscendingOrder("start_time");

            list.clear();
            List<ParseObject> lectlist;
            try {
                 lectlist=query.find();
                for(ParseObject obj:lectlist){
                    obj.put("stored_at",today);
                }
                ParseObject.pinAllInBackground(lectlist);
                if(lectlist.isEmpty()){
                    list.add(new Custom_lect_list_item("No Lectures","",""));
                }
                for(ParseObject obj:lectlist){
                   list.add(new Custom_lect_list_item(obj.getString("lecture"),obj.getString("start_time"), obj.getString("end_time")));
                    publishProgress(obj.getString("lecture"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                error = e.getMessage();
                System.out.println("  \n\n\n\n\n\n\n\n\n\n\n\n"+e.getMessage()+"  !! "+e.toString() );

                return Taskstate.EXCEPTION_THROWN;
            }
            return Taskstate.SUCCESS;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pd.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Taskstate taskstate) {
            super.onPostExecute(taskstate);
            pd.setMessage("");
            pd.dismiss();
            if(taskstate==Taskstate.EXCEPTION_THROWN){
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MylecturesActivity.this);
                builder.setMessage("Exception Thrown")
                        .setTitle("exception! Day-"+Day)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();*/
                Toast.makeText(MylecturesActivity.this, "Exception Thrown from Async Task", Toast.LENGTH_SHORT).show();
            }
            else if(taskstate == Taskstate.NO_INTERNET||taskstate == Taskstate.OFFLINE){
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MylecturesActivity.this);
                builder.setMessage(R.string.no_internet_msg)
                        .setTitle(R.string.no_internet_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();*/

                Toast.makeText(MylecturesActivity.this, "Offline State or No Network "+tdla, Toast.LENGTH_SHORT).show();
                        String batch;
                batch= "btech-2-cs";
                ParseQuery<ParseObject> query = ParseQuery.getQuery("myLect");
                query.fromLocalDatastore();
                query.whereEqualTo("day", Day);
                query.whereEqualTo("batch",batch);
                query.addAscendingOrder("start_time");
                list.clear();
                List<ParseObject> lectlist;
                try {
                    lectlist=query.find();
                    ParseObject.pinAllInBackground(lectlist);
                    if(lectlist.isEmpty()){
                        list.add(new Custom_lect_list_item("No Lectures","",""));
                    }
                    for(ParseObject obj:lectlist){
                        list.add(new Custom_lect_list_item(obj.getString("lecture"), obj.getString("start_time"), obj.getString("end_time")));

                    }
                    adapter.notifyDataSetChanged();
                } catch (ParseException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                    System.out.println("  \n\n\n\n\n\n\n\n\n\n\n\n"+e.getMessage()+"  !! "+e.toString() );
                    AlertDialog.Builder builder = new AlertDialog.Builder(MylecturesActivity.this);
                    builder.setMessage(R.string.no_internet_msg)
                            .setTitle(R.string.no_internet_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }


            }
            else if(taskstate== Taskstate.SUCCESS){
                Toast.makeText(MylecturesActivity.this, "Success from  SERVER "+tdla, Toast.LENGTH_SHORT).show();
                lvlect.setEnabled(true);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
