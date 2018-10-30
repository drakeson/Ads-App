package smith.drake.kato.ucuinterview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import smith.drake.kato.ucuinterview.Adapter.LocationAdapter;
import smith.drake.kato.ucuinterview.App.AppController;
import smith.drake.kato.ucuinterview.Model.Address;

import static smith.drake.kato.ucuinterview.App.AppURLs.FEED;

public class FeedActivity extends AppCompatActivity {

    private static final String TAG = FeedActivity.class.getSimpleName();
    EditText mMsg;
    Button mSend;
    ListView listView;
    private LocationAdapter listAdapter;
    private List<Address> feedItems = new ArrayList<Address>();
    private ShimmerFrameLayout mShimmerViewContainer;
    private static String LONGI = "longitude";
    private static String LATI = "latitude";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mMsg = findViewById(R.id.input_msg);
        mSend = findViewById(R.id.send);
        listView = findViewById(R.id.list);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);


        feedItems = new ArrayList<Address>();
        listAdapter = new LocationAdapter(this,feedItems);
        listView.setAdapter(listAdapter);

        Intent i = getIntent();
        String longitude = i.getStringExtra(LONGI);
        String latitude = i.getStringExtra(LATI);

        JsonArrayRequest movieReq = new JsonArrayRequest(FEED + longitude + "/" + latitude,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Tag", response.toString());

                        if (response.toString().equals("[]")){
                            new SweetAlertDialog(FeedActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Failed")
                                    .setContentText("That location is not in our database \n Try Again!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();

                        } else {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    mShimmerViewContainer.stopShimmerAnimation();
                                    mShimmerViewContainer.setVisibility(View.GONE);

                                    JSONObject obj = response.getJSONObject(i);
                                    Address item = new Address();
                                    item.setTitle(obj.getString("address"));
                                    item.setDesc(obj.getString("distance"));

                                    feedItems.add(item);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            listAdapter.notifyDataSetChanged();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                new SweetAlertDialog(FeedActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Failed")
                        .setContentText("That location is not in our database \n Try Again!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(FeedActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);




        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mMsg.length() < 3 ){
                    new SweetAlertDialog(FeedActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText("Enter Correct Advertisement Message")
                            .show();
                }
                else {
                    new SweetAlertDialog(FeedActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful")
                            .setContentText("Your broadcast has been sent")
                            .show();
                }

            }
        });
    }
}
