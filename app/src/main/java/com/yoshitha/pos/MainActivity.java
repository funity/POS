package com.yoshitha.pos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

    Spinner spinnerMake;
    Spinner spinnerDestination;
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
        spinnerMake = findViewById(R.id.spinnerMake);
        spinnerDestination = findViewById(R.id.spinnerDestination);

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
                    jsonParam.put("make", spinnerMake.getSelectedItem().toString().trim());
                    jsonParam.put("model", editTextModel.getText().toString().trim());
                    jsonParam.put("branch", editTextBranch.getText().toString().trim());
                    jsonParam.put("destination", spinnerDestination.getSelectedItem().toString().trim());
                    jsonParam.put("issued_by", editTextIssuedBy.getText().toString().trim());
                    jsonParam.put("phone_number", editTextPhoneNumber.getText().toString().trim());
                    jsonParam.put("chassis", editTextChassisNumber.getText().toString().trim());

                    Log.i("JSON", jsonParam.toString());

                    httpURLConnection.setFixedLengthStreamingMode(jsonParam.toString().getBytes().length);
                    PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
                    out.print(jsonParam);
                    out.close();

                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    StringBuilder data = new StringBuilder();
                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data.append(current);
                    }

                    String responseCode = String.valueOf(httpURLConnection.getResponseCode());

                    Log.i("RESPONSE", data.toString());
                    Log.i("RESPONSE CODE", responseCode);
                    Log.i("MSG", httpURLConnection.getResponseMessage());


                    httpURLConnection.disconnect();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Response Code: " + responseCode, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.i("ERROR***", e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        thread.start();
    }

}