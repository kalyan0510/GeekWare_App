package com.geekware.geekware;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkalyan0510 on 12/18/2015.
 */
public class SellnBuyFragment extends Fragment {
    ArrayList<Custom_Buy_Item> list;
    int pgn;
    static ImageButton ib;
    static Application app;
   static String selectedImagePath;
    EditText name,pr,ct,usd;
    static Context context;
    ListView lv;
    Button sell;
    Uri selectedImageUri;
    Custom_Buy_Adapter adapter;
    public static SellnBuyFragment create(int pagenumber){
        SellnBuyFragment fragment = new SellnBuyFragment();
        Bundle args = new Bundle();
        args.putInt("pgn", pagenumber);
        fragment.setArguments(args);
        return fragment;
    }
    public SellnBuyFragment(){

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
            ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.buy_fragment,container,false);
            list = new ArrayList<Custom_Buy_Item>();
            lv = (ListView)vg.findViewById(R.id.buylist);
           // list.add(new Custom_Buy_Item(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
             //       "Nexus", "phd I yr", 15000, "7024510641", 15));
            adapter = new Custom_Buy_Adapter(context,list);
            lv.setAdapter(adapter);
            lv.setEnabled(false);
            adapter.notifyDataSetChanged();
            new LoadItems().execute();
            ((Button)vg.findViewById(R.id.ref)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utilities.isNetworkAvailable(app)) {
                        Toast.makeText(context,"No Network Available",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("sellnbuy");
                        query.fromLocalDatastore();
                        ParseObject.unpinAll(query.find());
                        new LoadItems().execute();
                        // new Loadbuses().execute();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            return vg;
        }
        else if(pgn==1){
            ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.sell_fragment, container, false);
            name=(EditText)vg.findViewById(R.id.itemet);
                    pr =(EditText)vg.findViewById(R.id.priceet);
                    usd =(EditText)vg.findViewById(R.id.usedet);
                    ct =(EditText)vg.findViewById(R.id.contactet);
            ib = (ImageButton)vg.findViewById(R.id.sellfragpic);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 56);
                }
            });
            sell = ((Button)vg.findViewById(R.id.sell));
            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LetItGO();
                }
            });


            return vg;
        }
        else
        {
            ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.feedback_fragment,container,false);
            return vg;
        }

    }
    String itm,cost,old,cont;ParseFile file;
    Void LetItGO(){
         itm=name.getText().toString().trim();
         cost=pr.getText().toString().trim();
         old=usd.getText().toString().trim();
         cont=ct.getText().toString().trim();

        try {
            if(selectedImagePath==null||itm.equals("")||cost.equals("")||old.equals("")||cont.equals(""))
            {
                Toast.makeText(context,"Please enter the data and select an Image",Toast.LENGTH_LONG).show();
                return  null;
            }
            if (!Utilities.isNetworkAvailable(app)) {
                Toast.makeText(context, "No Network Available"
                        , Toast.LENGTH_SHORT).show();
                return null;
            }
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver()
                    , selectedImageUri);
            bitmap = Bitmap.createScaledBitmap(bitmap,270,200,false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 45, stream);
            final byte[] array=stream.toByteArray();

             file = new ParseFile("img.jpg",array);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        Toast.makeText(context, "Upload Completed " + array.length, Toast.LENGTH_SHORT).show();
                        savesellingobj();
                    }
                    else
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    Toast.makeText(context, "Uploading " + integer, Toast.LENGTH_SHORT).show();
                    // pd.setProgress(integer);
                    // pd.setMessage("Uploading "+integer);

                }
            });



            Log.d("test", array.length + " is len");
        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }
    void savesellingobj(){
        ParseObject object = new ParseObject("sellnbuy");
        object.put("ImageFile", file);
        object.put("contact",cont);
        object.put("used",Integer.parseInt(old));
        object.put("price",Integer.parseInt(cost));
        object.put("item", itm);

        try {
            object.save();
            Toast.makeText(context,"Completed",Toast.LENGTH_LONG).show();
            object.pin();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Toast.makeText(context,"Entered function",Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
           // Toast.makeText(context,"passed resultCode == -1",Toast.LENGTH_LONG).show();
            if (requestCode == 56) {
                 selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);

                //if (selectedImagePath == null)
                  //  Toast.makeText(context,"path null",Toast.LENGTH_LONG).show();
                if (selectedImagePath == null)
                    return;
                Toast.makeText(context,selectedImagePath,Toast.LENGTH_LONG).show();
                ib.setImageURI(selectedImageUri);


            }
        }
    }
    public String getPath(Uri uri){
        if(uri==null)
            return null;
        return uri.getPath();
    }
    static Void setImage(Uri path){
        ib.setImageURI(path);
        return null;
    }
    public int getPageNumber() {
        return pgn;
    }


    ////////////////AsyncTask for Downloading Data/////////////////////////////////////////////////////////////////////////////////////////////

    enum Taskstate{
        SUCCESS,NO_INTERNET,EXCEPTION_THROWN,OFFLINE
    }
    ProgressDialog pd;
    class LoadItems extends AsyncTask<Void, String,Taskstate > {

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(getActivity());


            pd.setMessage("Loading Items "+need_forceupdate());
            pd.show();
        }

        boolean need_forceupdate(){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("sellnbuy");
            query.fromLocalDatastore();

            try {
                ParseObject obj =query.getFirst();
                // Toast.makeText(MylecturesActivity.this, "stored- "+obj.getString("stored_at")+" today-"+today, Toast.LENGTH_SHORT).show();

                /*if(obj.getString("stored_at").equals(today)){
                    return false;
                }

                else*/ return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return true;
            }
        }
        String error="";

        @Override
        protected Taskstate doInBackground(Void... params) {
            if(!Utilities.isNetworkAvailable(app))
                return Taskstate.NO_INTERNET;

           /* if(!need_forceupdate()){
                return Taskstate.OFFLINE;
            }*/

            ParseQuery<ParseObject> query = ParseQuery.getQuery("sellnbuy");


            list.clear();
            List<ParseObject> lectlist;
            try {
                lectlist=query.find();
                /*for(ParseObject obj:lectlist){
                    obj.put("stored_at",today);
                }*/
                ParseObject.pinAllInBackground(lectlist);
               /* if(lectlist.isEmpty()){
                    list.add(new Custom_Buy_Item(BitmapFactory.decodeByteArray()));
                }*/
                byte[] array;
                for(ParseObject obj:lectlist){
                    array=obj.getParseFile("ImageFile").getData();
                    list.add(new Custom_Buy_Item(BitmapFactory.decodeByteArray(array, 0, array.length),
                            obj.getString("item"),"phd I yr",obj.getInt("price"),obj.getString("contact"),obj.getInt("used") ));
                    System.out.println(obj.getObjectId() + "<====" + array.length + "===============");
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
        protected void onPostExecute(Taskstate taskstate) {
            super.onPostExecute(taskstate);
            pd.setMessage("");
            pd.dismiss();
            if(taskstate==Taskstate.EXCEPTION_THROWN){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Exception Thrown")
                        .setTitle("exception")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else if(taskstate == Taskstate.NO_INTERNET||taskstate == Taskstate.OFFLINE){
                Toast.makeText(context,"No Network Available",Toast.LENGTH_LONG).show();

                /*Toast.makeText(MylecturesActivity.this, "Offline State or No Network " + tdla, Toast.LENGTH_SHORT).show();
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

                }*/ ParseQuery<ParseObject> query = ParseQuery.getQuery("sellnbuy");
                    query.fromLocalDatastore();

                list.clear();
                List<ParseObject> lectlist;
                try {
                    lectlist=query.find();
                /*for(ParseObject obj:lectlist){
                    obj.put("stored_at",today);
                }*/
               /* if(lectlist.isEmpty()){
                    list.add(new Custom_Buy_Item(BitmapFactory.decodeByteArray()));
                }*/
                    byte[] array;int x=0;
                    for(ParseObject obj:lectlist){
                        array=obj.getParseFile("ImageFile").getData();
                        list.add(new Custom_Buy_Item(BitmapFactory.decodeByteArray(array, 0, array.length),
                                obj.getString("item"),"phd I yr",obj.getInt("price"),obj.getString("contact"),obj.getInt("used") ));
                        System.out.println(obj.getObjectId() + "<====" + array.length + "========OFFLINE=======");
                        //x++;if(x>5)break;
                    }
                    lv.setEnabled(true);
                    adapter.notifyDataSetChanged();
                } catch (ParseException e) {
                    e.printStackTrace();
                    error = e.getMessage();
                    System.out.println("  \n\n\n\n\n\n\n\n\n\n\n\n"+e.getMessage()+"  !! "+e.toString() );
                }


            }
            else if(taskstate== Taskstate.SUCCESS){
                Toast.makeText(context, "Success from  SERVER ", Toast.LENGTH_SHORT).show();
                lv.setEnabled(true);
                adapter.notifyDataSetChanged();
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////



}
