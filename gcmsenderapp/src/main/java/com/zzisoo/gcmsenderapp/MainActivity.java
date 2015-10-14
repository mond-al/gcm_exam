package com.zzisoo.gcmsenderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    public static final String API_KEY = "";
    public static final String API_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent.hasExtra("TOKEN")) {
            API_TOKEN =  intent.getStringExtra("TOKEN");
        }


        final EditText etToken = (EditText) findViewById(com.zzisoo.gcmsenderapp.R.id.etToken);
        final EditText etApiKey = (EditText) findViewById(R.id.etApiKey);

        etToken.setText(API_TOKEN);
        etApiKey.setText(API_KEY);

        // set Send button action
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // Prepare JSON containing the GCM message content. What to send and where to send.
                    GcmData gcmData = new GcmData();

                    EditText etTitle = (EditText) findViewById(R.id.etTitle);
                    EditText etBody = (EditText) findViewById(R.id.etBody);

                    RadioGroup rgTargetSelect = (RadioGroup) findViewById(R.id.rgTargetSelect);

                    if (rgTargetSelect.getCheckedRadioButtonId() == R.id.rbToken) {
                        gcmData.to = etToken.getText().toString();
                    } else if (rgTargetSelect.getCheckedRadioButtonId() == R.id.rbTopic) {
                        gcmData.to = "/topics/global";
                    }

                    boolean bCheckedNormalPriority =((CheckBox)findViewById(R.id.cbPriority)).isChecked();
                    if(bCheckedNormalPriority) {
                        gcmData.priority = "normal";
                    }

                    Toast.makeText(MainActivity.this, "Priority : "+gcmData.priority, Toast.LENGTH_SHORT).show();

                    gcmData.data.title = etTitle.getText().toString();
                    gcmData.data.body = etBody.getText().toString();

                    RestAdapter.Builder builder = new RestAdapter.Builder()
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .setEndpoint("https://android.googleapis.com");

                    builder.setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("Accept", "application/json");
                            request.addHeader("Authorization", "key=" + etApiKey.getText().toString());
                        }
                    });

                    RestAdapter gcmSendRestAdapter = builder.build();

                    gcmSendRestAdapter.create(GoogleApiInterface.class)
                            .GcmSend(gcmData, new Callback<JSONObject>() {
                                @Override
                                public void success(JSONObject jsonObject, Response response) {
                                    Log.e(TAG, "JSONObject : " + jsonObject.toString());
                                    Log.e(TAG, "Response  ");
                                    Log.e(TAG, "Reason : " + response.getReason());
                                    Log.e(TAG, "Status : " + response.getStatus());
                                    Log.e(TAG, "Url : " + response.getUrl());
                                    Log.e(TAG, "Headers : " + response.getHeaders());
                                    Log.e(TAG, "Body : " + new String(((TypedByteArray) response.getBody()).getBytes()));
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e(TAG, "error: " + error.getResponse());
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
