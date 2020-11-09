package com.yoshitha.pos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextLicenceNo;
    EditText editTextDriverName;
    EditText editTextNationality;
    EditText editTextMake;
    EditText editTextModel;
    EditText editTextBranch;
    EditText editTextDestination;
    EditText editTextIssuedBy;
    EditText editTextPhoneNumber;
    EditText editTextChassisNumber;
    Button buttonSubmit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextLicenceNo = findViewById(R.id.editTextDriverLicenceNumber);
        editTextDriverName = findViewById(R.id.editTextDriverName);
        editTextNationality = findViewById(R.id.editTextNationality);
        editTextMake = findViewById(R.id.editTextMake);
        editTextModel = findViewById(R.id.editTextModel);
        editTextBranch = findViewById(R.id.editTextBranch);
        editTextDestination = findViewById(R.id.editTextDestination);
        editTextIssuedBy = findViewById(R.id.editTextIssuedBy);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextChassisNumber = findViewById(R.id.editTextChasis);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        progressBar = findViewById(R.id.progressBar);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToServer();
            }
        });


    }

    public void postDataToServer() {

        progressBar.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://85ea6703eb97.ngrok.io/sys/users/admin/create_sticker.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");


                    // TODO uncomment the true user id
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("licence_number", editTextLicenceNo.getText().toString().trim());
                    jsonParam.put("driver_name", editTextDriverName.getText().toString().trim());
                    jsonParam.put("nationality", editTextNationality.getText().toString().trim());
                    jsonParam.put("make", editTextMake.getText().toString().trim());
                    jsonParam.put("model", editTextModel.getText().toString().trim());
                    jsonParam.put("branch", editTextBranch.getText().toString().trim());
                    jsonParam.put("destination", editTextDestination.getText().toString().trim());
                    jsonParam.put("issued_by", editTextIssuedBy.getText().toString().trim());
                    jsonParam.put("phone_number", editTextPhoneNumber.getText().toString().trim());
                    jsonParam.put("chassis", editTextChassisNumber.getText().toString().trim());

                    Log.i("JSON", jsonParam.toString());

                    httpURLConnection.setFixedLengthStreamingMode(jsonParam.toString().getBytes().length);
                    PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
                    out.print(jsonParam);
                    out.close();

                    Log.i("JSON", "1");

                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    Log.i("JSON", "2");


                    StringBuilder data = new StringBuilder();
                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data.append(current);
                    }

//                    JSONObject res = new JSONObject(data.toString());
//                    final String uploadResponseCode = res.getString("uploadResponseCode");
//                    final String userid = res.getString("userid");
//                    final int number = res.getInt("number");
//                    final String names = res.getString("names");
//                    final String message = res.getString("message");

                    String responseCode = String.valueOf(httpURLConnection.getResponseCode());

                    Log.i("RESPONSE", data.toString());
                    Log.i("STATUS", responseCode);
                    Log.i("MSG", httpURLConnection.getResponseMessage());


                    httpURLConnection.disconnect();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Response Code: "+responseCode, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.i("ERROR***", e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Error: "+ e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        thread.start();
    }

//    public class sendUserDetailTOServer extends AsyncTask< ArrayList<String>, Void, String> {
//
//        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
//        }
//
//        protected String doInBackground(ArrayList<String>... alldata) {
//
//            ArrayList<String> passed = alldata[0]; //get passed arraylist
//            String usnm = passed.get(0);
//            String pass = passed.get(1);
//            String phone = passed.get(2);
//            String hintq = passed.get(3);
//            String hingas = passed.get(4);
//
//            // current time
//            time = df.format(Calendar.getInstance().getTime());
//            date = dt.format(c.getTime());
//            try {
//                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
//                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("username", usnm ));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
//                nameValuePairs.add(new BasicNameValuePair("phonenumber",  phone));
//                nameValuePairs.add(new BasicNameValuePair("hintq", hintq));
//                nameValuePairs.add(new BasicNameValuePair("hintanswer", hingas));
//                nameValuePairs.add(new BasicNameValuePair("date", date));
//                nameValuePairs.add(new BasicNameValuePair("time", time));
//
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost("http://blueappsoftware.in/android/tutorial/StoreUserData.php");
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs ,"UTF-8")); // UTF-8  support multi language
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity entity = response.getEntity();
//                is = entity.getContent();
//                //     Log.e("pass 1", "connection success ");
//            }
//            catch(Exception e)
//            {
//                //  Log.e("Fail 1", e.toString());
//                //  Log.d("setup Activity ","  fail 1  "+e.toString());
//            }
//
//            try
//            {
//                BufferedReader reader = new BufferedReader (new InputStreamReader(is,"iso-8859-1"),8);
//                StringBuilder sb = new StringBuilder();
//                while ((line = reader.readLine()) != null)
//                {
//                    sb.append(line + "\n");
//                }
//                is.close();
//                result = sb.toString();
//                // Log.d("pass 2", "connection success " + result);
//            }
//            catch(Exception e)
//            {
//                //  Log.e("Fail 2", e.toString());
//                //  Log.e("setup Activity  "," fail  2 "+ e.toString());
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            //  Log.e(" setup acc ","  signup result  " + result);
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//
//            try
//            {
//                JSONObject json_data = new JSONObject(result);
//                code=(json_data.getInt("Tripcode"));
//                //   Log.d("pass 3", "connection success " + result);
//                if(code==1)
//                {
//                    try {
//                        String Res_username =json_data.getString("Res_username");
//                        String Res_password =json_data.getString("Res_password");
//
//                        Intent nextScreen = new Intent(getApplicationContext(), HomeScreen.class);
//                        nextScreen.putExtra("username", Res_username );
//                        nextScreen.putExtra("password", Res_password);
//                        //Sending data to another Activity
//                        startActivity(nextScreen);
//
//                    }catch (Exception e){
//
//                    }
//                }
//
//
//            }
//            catch(Exception e)
//            {
//                LoginError(" Network Problem , Please try again");
//                //    Log.e("Fail 3 main result ", e.toString());
//                // Log.d(" setup Activity "," fail 3 error - "+ result );
//            }
//        }
//
//    }
}