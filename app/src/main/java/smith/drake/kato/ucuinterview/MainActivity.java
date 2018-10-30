package smith.drake.kato.ucuinterview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE = 1234;
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // TODO: Set LOCATION_PROVIDER here:
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager mlocationManager;
    LocationListener mlocationListener;

    EditText mlogi, mlati;
    Button mNear, mDevice;

    private static String LONGI = "longitude";
    private static String LATI = "latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlati = findViewById(R.id.input_latitude);
        mlogi = findViewById(R.id.input_longitude);
        mNear = findViewById(R.id.near);
        mDevice = findViewById(R.id.device);


        mNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String longitude = mlogi.getText().toString();
                String latitude = mlati.getText().toString();

                if(latitude.length() < 4 ){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText("Enter Correct Latitude")
                            .show();
                }
                else if (longitude.length() < 4 ){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText("Enter Correct Longitude")
                            .show();
                }
                else if (longitude.length() > 4 && latitude.length() >4) {
                    Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                    intent.putExtra(LONGI, longitude);
                    intent.putExtra(LATI, latitude);
                    startActivity(intent);
                }
                else {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText("Enter Location Details")
                            .show();
                }
            }
        });

        mDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                mlocationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String longitude = String.valueOf(location.getLongitude());
                        String latitude = String.valueOf(location.getLatitude());
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Successful")
                                .setContentText("Location Found")
                                .show();
                        Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                        intent.putExtra(LONGI, longitude);
                        intent.putExtra(LATI, latitude);
                        startActivity(intent);
                    }
                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }
                    @Override
                    public void onProviderEnabled(String s) {

                    }
                    @Override
                    public void onProviderDisabled(String s) {
                        Log.d("App", "Not getting Location");
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Failed")
                                .setContentText("Not getting Location")
                                .show();
                    }
                };

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},  REQUEST_CODE);
                    return;
                }
                mlocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mlocationListener);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("App", "PERMISSION GRANTED");
            } else {
                Log.d("App", "PERMISSION  not GRANTED");
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Cannot get Device Location")
                        .setContentText("PERMISSION  not GRANTED")
                        .show();
            }
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mlocationManager != null) mlocationManager.removeUpdates(mlocationListener);
    }
}
