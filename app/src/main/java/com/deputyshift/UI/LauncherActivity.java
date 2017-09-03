package com.deputyshift.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.deputyshift.DeputyApplication;
import com.deputyshift.R;
import com.deputyshift.Server.ServerURLConstants;
import com.deputyshift.UI.Model.ShiftDetailsUIModel;
import com.deputyshift.Utility.DateTimeConverter;
import com.deputyshift.Utility.GPSTracker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";
    private static int MY_PERMISSION_LOCATION = 1;
    private String tag_json_obj = "json_obj_req";

    GPSTracker mGps;
    private double mLatitude;
    private double mLongitude;
    String mLocationAddress;
    ShiftDetailsUIModel mStartShiftDetailsUIModel = new ShiftDetailsUIModel();
    ShiftDetailsUIModel mEndShiftDetailsUIModel = new ShiftDetailsUIModel();

    @BindView(R.id.logo)
    ImageView mImagelogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!haveNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.no_Connection)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        sendRequestToGetBusinessImage();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        marshmallowGPSPremissionCheck();
    }

    private void marshmallowGPSPremissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            getLatitudeAndLongitude();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == MY_PERMISSION_LOCATION
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLatitudeAndLongitude();
            }
        }

    }

    private void getLatitudeAndLongitude() {
        mGps = new GPSTracker(LauncherActivity.this);
        if (mGps.canGetLocation()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mLatitude = mGps.getLatitude();
                    mLongitude = mGps.getLongitude();
                    Log.i("LAT ", mLatitude + " " + mLongitude);
                }
            });
        } else {
            mGps.showSettingsAlert();
        }
    }

    @OnClick(R.id.start_button)
    public void onClickStartShiftButton() {
        // Start Time
        String startTime = DateTimeConverter.convertDateToString();
        mStartShiftDetailsUIModel.setShiftStartTime(startTime);

        // Location Details
        mStartShiftDetailsUIModel.setShiftStartLat(mLatitude);
        mStartShiftDetailsUIModel.setShiftStartLon(mLongitude);
        mStartShiftDetailsUIModel.setStartLocationAddress(mLocationAddress);

        sendStartShiftRequest();
    }


    @OnClick(R.id.end_button)
    public void onClickEndShiftButton() {
        // End Time
        String endTime = DateTimeConverter.convertDateToString();
        mEndShiftDetailsUIModel.setshiftEndTime(endTime);

        // Location Details
        mEndShiftDetailsUIModel.setShiftStartLat(mLatitude);
        mEndShiftDetailsUIModel.setShiftStartLon(mLongitude);
        mEndShiftDetailsUIModel.setStartLocationAddress(mLocationAddress);

        sendEndShiftRequest();
    }

    @OnClick(R.id.viewshifts_button)
    public void onClickViewAllShiftsButton() {
        startActivity(new Intent(LauncherActivity.this, ListOfShiftsActivity.class));
    }

    private void sendStartShiftRequest() {
        JSONObject params = new JSONObject();
        try {
            params.put("time", mStartShiftDetailsUIModel.getShiftStartTime());
            params.put("latitude", mStartShiftDetailsUIModel.getShiftStartLat());
            params.put("longitude", mStartShiftDetailsUIModel.getShiftStartLon());
            Log.i(TAG, params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, ServerURLConstants.getStartShiftURL(), params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            if (response != null) {
                                String res = (String) response.get("result");
                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i(TAG, "NetworkResponse: " + error.fillInStackTrace());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "Deputy";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json");
                headers.put("sha1", "9eadc8fa224eebb865f005688a041b3ba050c57f");
                headers.put("Authorization", auth);

                return headers;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0) {
                        result = new JSONObject();
                        result.put("result", jsonString);
                    }

                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        // Adding request to request queue
        DeputyApplication.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    private void sendEndShiftRequest() {
        JSONObject params = new JSONObject();
        try {
            params.put("time", mEndShiftDetailsUIModel.getShiftEndTime());
            params.put("latitude", mEndShiftDetailsUIModel.getShiftStartLat());
            params.put("longitude", mEndShiftDetailsUIModel.getShiftStartLon());
            Log.i(TAG, params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, ServerURLConstants.getEndShiftURL(), params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            if (response != null) {
                                String res = (String) response.get("result");
                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i(TAG, "NetworkResponse: " + error.fillInStackTrace());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "Deputy";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json");
                headers.put("sha1", "9eadc8fa224eebb865f005688a041b3ba050c57f");
                headers.put("Authorization", auth);

                return headers;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0) {
                        result = new JSONObject();
                        result.put("result", jsonString);
                    }

                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        // Adding request to request queue
        DeputyApplication.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    private void sendRequestToGetBusinessImage() {

        StringRequest request = new StringRequest(Request.Method.GET, ServerURLConstants.getBusinessURL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        if (null != response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                Picasso.with(LauncherActivity.this).load(json.get("logo").toString()).into(mImagelogo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "Deputy";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json");
                headers.put("sha1", "9eadc8fa224eebb865f005688a041b3ba050c57f");
                headers.put("Authorization", auth);

                return headers;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        // Adding request to request queue
        DeputyApplication.getInstance().addToRequestQueue(request, tag_json_obj);
    }

}
