package com.geekware.geekware;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class LoginActivity extends ActionBarActivity {
    Button btnLogin,but,but1;Button but2;
    EditText etUsername, etPassword;
    ProgressDialog pd;
    String username, password;      //To pass data to the LoginTask
    String selectedImagePath;
    String imgid;
    byte[] byar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgid="LJZIQvUCfa";
        pd = new ProgressDialog(LoginActivity.this);
        etUsername = (EditText) findViewById(R.id.etUsernameLogin);
        etPassword = (EditText) findViewById(R.id.etPasswordLogin);
        btnLogin   = (Button)   findViewById(R.id.btnLogin);
        but= (Button) findViewById(R.id.gt);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MylecturesActivity.class);
                startActivity(i);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                new LoginTask().execute();
            }
        });
        but1=(Button)findViewById(R.id.gts);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, LectNotesActivity.class);
                startActivity(i);
            }
        });

        but2=(Button)findViewById(R.id.buses);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, BusActivity.class);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.img)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 45);
            }
        });
        ((Button)findViewById(R.id.getimg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgid=((EditText)findViewById(R.id.imgid)).getText().toString();
                new async().execute();
            }
        });

        ((Button)findViewById(R.id.snb)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(LoginActivity.this, SellnbuyActivity.class);
                startActivity(i);
            }
        });

    }


    class async extends AsyncTask<Void , Integer , Integer>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait while downloading Image....");
            pd.show();
            Toast.makeText(LoginActivity.this,"imgid->>>"+imgid, Toast.LENGTH_SHORT).show();
        }
        Bitmap bp;byte[] bmp;
        @Override
        protected void onPostExecute(Integer aVoid) {
            pd.dismiss();
            super.onPostExecute(aVoid);
            if(bp==null){
                // Toast.makeText(LoginActivity.this,+bmp.length, Toast.LENGTH_SHORT).show();

                bp=BitmapFactory.decodeByteArray(bmp,0,bmp.length);
            }
            else
                Toast.makeText(LoginActivity.this,"Download Completed", Toast.LENGTH_SHORT).show();
            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bp);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
            Toast.makeText(LoginActivity.this, "Downloading "+values[0], Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ParseQuery query = new ParseQuery("sellnbuy");
            /*query.getInBackground(imgid,new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("test", "The object was not found...");
                    } else {
                        Log.d("test", "Retrieved the object.");
                        ParseFile fileObject = (ParseFile)object.get("ImageFile");
                        fileObject.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Log.d("test", "We've got data in data."+data.length);
                                    // use data for something
                                     bmp = data;



                                } else {
                                    Log.d("test", "THIS ERROR_>"+e.getMessage()+" "+e.toString());
                                }
                            }
                        });
                    }
                }
            });*/

            try {
                ParseObject obj = query.get(imgid);
                ParseFile file = obj.getParseFile("ImageFile");

                bmp= file.getData();
                bp=BitmapFactory.decodeByteArray(bmp,0,bmp.length);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("sellnbuy");

            query.getInBackground(imgid, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    ParseFile fileobj = (ParseFile)parseObject.get("ImageFile");
                    fileobj.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                bp=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            }
                            else {
                                bp=null;
                            }
                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {
                            publishProgress(integer);
                        }
                    });
                }
            });*/
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode == RESULT_OK) {
            if (requestCode == 45) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);


                if (selectedImagePath == null)
                    return;
                ((Button) findViewById(R.id.img)).setText(selectedImagePath);
                ImageView imgvw = (ImageView) findViewById(R.id.imageView);
                imgvw.setImageURI(selectedImageUri);


                pd.setMessage("Uploading Image");
                pd.show();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),selectedImageUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap,270,200,false);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 45, stream);
                    final byte[] array=stream.toByteArray();
                    bitmap=null;
                    byar=array;
                    ParseFile file = new ParseFile("img.jpg",array);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                Toast.makeText(LoginActivity.this, "Upload Completed " + array.length, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                            pd.dismiss();

                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {
                            Toast.makeText(LoginActivity.this, "Uploading " + integer, Toast.LENGTH_SHORT).show();
                            pd.setProgress(integer);
                            pd.setMessage("Uploading "+integer);

                        }
                    });


                    ParseObject object = new ParseObject("sellnbuy");
                    object.put("ImageFile", file);
                    object.save();
                    Log.d("test", array.length + " is len");

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //imgvw.setImageURI(selectedImageUri);






            }
        }
    }
    class uploadimg extends AsyncTask<Uri,Integer,Void>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Uploading Image");
            pd.show();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pd.setProgress(values[0]);
            pd.setMessage("Uploading Image"+values[0]+"%");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
        }

        @Override
        protected Void doInBackground(Uri... params) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), params[0]);
                bitmap = Bitmap.createScaledBitmap(bitmap,270,200,false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 45, stream);
                final byte[] array=stream.toByteArray();
                byar=array;
                ParseFile file = new ParseFile("img.jpg",array);
                file.save();
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                            Toast.makeText(LoginActivity.this, "Upload Completed " + array.length, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();


                    }
                }, new ProgressCallback() {
                    @Override
                    public void done(Integer integer) {
                        //Toast.makeText(LoginActivity.this, "Uploading " + integer, Toast.LENGTH_SHORT).show();
                        publishProgress(integer);

                    }
                });


                ParseObject object = new ParseObject("sellnbuy");
                object.put("ImageFile", file);
                object.saveInBackground();
                Log.d("test", array.length + " is len");

            } catch (IOException e) {
                e.printStackTrace();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //imgvw.setImageURI(selectedImageUri);

            return null;
        }
    }

    public String getPath(Uri uri){
        if(uri==null)
            return null;
        return uri.getPath();
    }



    /**
     * All the possible states the LoginTask can return with
     */
    private enum LoginTaskState
    {
        SUCCESS,
        NO_INTERNET,
        EMPTY_LOGIN,
        EMAIL_UNVERIFIED,
        EXCEPTION_THROWN
    }

    class LoginTask extends AsyncTask<Void,Boolean, LoginTaskState> {
        ProgressDialog pd;
        String errMsg;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait while logging in...");
            pd.show();
        }

        @Override
        protected LoginTaskState doInBackground(Void... params) {
            publishProgress(false);             //Disable login button
            pd.setMessage("Logging in...");

            if (!Utilities.isNetworkAvailable(LoginActivity.this)) {
                return LoginTaskState.NO_INTERNET;
            }
            username = username.trim();
            password = password.trim();
            if (username.isEmpty() || password.isEmpty()) {
                return LoginTaskState.EMPTY_LOGIN;
            } else {
                try {
                    ParseUser user = ParseUser.logIn(username, password);
                    boolean verified = user.getBoolean("emailVerified");
                    if (!verified) {
                        ParseUser.logOut();
                        return LoginTaskState.EMAIL_UNVERIFIED;
                    } else {
                        // Email Verified! :)
                        Utilities.setCurrentUser(user);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    errMsg = e.getMessage();
                    return LoginTaskState.EXCEPTION_THROWN;
                }
            }
            return LoginTaskState.SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Boolean... isEnabled) {
            btnLogin.setEnabled(isEnabled[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }


        @Override
        protected void onPostExecute(LoginTaskState state) {
            pd.setMessage("");
            pd.dismiss();

            // refresh UI
            if (state == LoginTaskState.SUCCESS) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (state == LoginTaskState.NO_INTERNET) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(R.string.no_internet_msg)
                        .setTitle(R.string.no_internet_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (state == LoginTaskState.EMPTY_LOGIN) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(R.string.login_error_message)
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (state == LoginTaskState.EMAIL_UNVERIFIED) {
                // Not Verified Email!
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(R.string.verify_email_msg)
                        .setTitle(R.string.verify_email_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (state == LoginTaskState.EXCEPTION_THROWN) {
                // Fail
                String errorMsg = errMsg.replace("parameters", "Email ID or password");
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(errorMsg)
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            btnLogin.setEnabled(true);
        }
    }

}
