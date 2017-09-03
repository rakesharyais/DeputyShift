package com.deputyshift.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deputyshift.DeputyApplication;
import com.deputyshift.R;
import com.deputyshift.Server.Model.ShiftResponseModel;
import com.deputyshift.Server.ServerURLConstants;
import com.deputyshift.UI.Adpater.RecyclerAdapter;
import com.deputyshift.UI.Model.ShiftDetailsUIModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfShiftsActivity extends AppCompatActivity {

    @BindView(R.id.noshifts_textview)
    TextView mNoShiftsTextView;
    @BindView(R.id.shift_recycler_view)
    RecyclerView mShiftRecyclerView;

    private RecyclerAdapter mRecyclerAdapter;
    private ArrayList<ShiftDetailsUIModel> mShiftDetailsUIModelList;
    private String tag_json_obj = "json_obj_req";
    private ShiftDetailsUIModel mShiftDetailsUIModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_shifts);
        ButterKnife.bind(this);


        mShiftDetailsUIModelList = new ArrayList<ShiftDetailsUIModel>();
        mRecyclerAdapter = new RecyclerAdapter(ListOfShiftsActivity.this, R.layout.shift_row_view, mShiftDetailsUIModelList, new RecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(ShiftDetailsUIModel item, int position) {
                Intent i = new Intent(ListOfShiftsActivity.this,MapsActivity.class);
                i.putExtra("startLat",item.getShiftStartLat());
                i.putExtra("startLong",item.getShiftStartLon());
                i.putExtra("endLat",item.getShiftEndLat());
                i.putExtra("endLong",item.getShiftEndLon());
                startActivity(i);

            }
        });
        mShiftRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mShiftRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mShiftRecyclerView.setAdapter(mRecyclerAdapter);

        if (mShiftDetailsUIModelList.size() == 0) {
            mNoShiftsTextView.setVisibility(View.VISIBLE);
            mShiftRecyclerView.setVisibility(View.GONE);
        } else {
            mNoShiftsTextView.setVisibility(View.GONE);
            mShiftRecyclerView.setVisibility(View.VISIBLE);
        }

        sendRequestTOGetListOfShifts();

    }


    private void sendRequestTOGetListOfShifts() {

        StringRequest request = new StringRequest(Request.Method.GET, ServerURLConstants.getListShiftURL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        if (null != response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Gson gson = new GsonBuilder().create();

                                List<ShiftResponseModel> shifsList;
                                Type listType = new TypeToken<List<ShiftResponseModel>>() {
                                }.getType();

                                shifsList= new Gson().fromJson(jsonArray.toString(), listType);

                                showListToUi(shifsList);


                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.

                    }
                }
        ){
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

    private void showListToUi(List<ShiftResponseModel> shifsList) {


        for(final ShiftResponseModel shiftResponseModel: shifsList){
            mShiftDetailsUIModel = new ShiftDetailsUIModel();
            mShiftDetailsUIModel.setShiftId(shiftResponseModel.getId());

            mShiftDetailsUIModel.setShiftStartTime(shiftResponseModel.getStart());
            mShiftDetailsUIModel.setShiftStartLat(Double.parseDouble(shiftResponseModel.getStartLatitude()));
            mShiftDetailsUIModel.setShiftStartLon(Double.parseDouble(shiftResponseModel.getStartLongitude()));

            mShiftDetailsUIModel.setImage(shiftResponseModel.getImage());
            if(!shiftResponseModel.getEnd().isEmpty()){
                mShiftDetailsUIModel.setshiftEndTime(shiftResponseModel.getEnd());
                mShiftDetailsUIModel.setShiftEndLat(Double.parseDouble(shiftResponseModel.getEndLatitude()));
                mShiftDetailsUIModel.setShiftEndLon(Double.parseDouble(shiftResponseModel.getEndLongitude()));
            }
            mShiftDetailsUIModelList.add(mShiftDetailsUIModel);
            mRecyclerAdapter.notifyDataSetChanged();
            mNoShiftsTextView.setVisibility(View.GONE);
            mShiftRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
