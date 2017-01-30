package com.androidluckyguys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    Context context;Activity activity;
    Button checkPermissionStatus;
    int permissionStatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        activity = MainActivity.this;
        checkPermissionStatus = (Button)findViewById(R.id.checkPermissionStatus);
        checkPermissionStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLocationPermission(activity,context,LOCATION_PERMISSION);

            }
        });



        //checkLocationPermission(activity,context,LOCATION_PERMISSION);


    }



    private void checkLocationPermission(Activity activity, final Context context, final String Permission) {

        PermissionUtil.checkPermission(activity,context,Permission,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onPermissionAsk() {


                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Permission},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining permission and then request permission

                        showToast("Permission previously Denied.");

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Permission},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionDisabled() {

                        askUserToAllowPermissionFromSetting();

                    }
                    @Override
                    public void onPermissionGranted() {

                        showToast("Permission Granted.");
                        getGpsLocation();
                    }
                });
    }

    private void askUserToAllowPermissionFromSetting() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Permission Required:");

        // set dialog message
        alertDialogBuilder
                .setMessage("Kindly allow Permission from App Setting, without this permission app would not show maps.")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        showToast("Permission forever Disabled.");
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void getGpsLocation() {
        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(context);
        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            showToast("Gps Values are:"+GPSTracker.latitude+" , "+GPSTracker.longitude);
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                     // permission was granted, yay! Do the task you need to do.
                    getGpsLocation();


                } else {


                    showToast("Permission denied,without permission can't access maps.");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
