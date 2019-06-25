package com.el_asdka2.hatly;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

public class Navigation extends AppCompatActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText desc;
    EditText price;
    EditText fare;
    Button order;
    boolean b = false;
    String Latitude;
    String Longitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    FirebaseDatabase database;
    DatabaseReference myRef, ref;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ProgressDialog pd;
    String email;
    LocationManager locationManager;
    String provider;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    LinearLayout order_linear;
    boolean doubleBackToExitPressedOnce = false;
    public String snav_img, sname;
    Calendar calendar=Calendar.getInstance();
    String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        desc = findViewById(R.id.desc_request);
        price = findViewById(R.id.price_request);
        order = findViewById(R.id.order_btn);
        fare = findViewById(R.id.fare_request);
        order_linear = findViewById(R.id.order_linear);
        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Requests");
        ref = database.getReference("Users");
        // email = getIntent().getStringExtra("email");
        mdrawer = findViewById(R.id.act_main);
        mtoggle = new ActionBarDrawerToggle(this, mdrawer, R.string.Open, R.string.Close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();
        NavigationView NV = findViewById(R.id.navigation);
        order.setOnClickListener(view -> {

            try {

                if (fare.getText().toString().equals(""))
                    fare.setError("Please Enter fare of your request");
                if (desc.getText().toString().equals(""))
                    desc.setError("Please Enter your request");
                if (price.getText().toString().equals("")) {
                    price.setError("Please Enter price you want");

                } else {
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, String> map = new HashMap<>();
                    map.put("request-description", desc.getText().toString());
                    map.put("price", price.getText().toString()+" LE");
                    map.put("Latitude", Latitude);
                    map.put("Longitude", Longitude);
                    map.put("email", email);
                    map.put("id_email", id);
                    map.put("fare", fare.getText().toString()+" LE");
                    map.put("date", currentDate);

                    myRef.push().setValue(map);
                    Toast.makeText(Navigation.this, "Your order is published", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Recycler.class);
                    intent.putExtra("request-disc", desc.getText().toString());
                    intent.putExtra("price", price.getText().toString());
                    intent.putExtra("fare", fare.getText().toString());
                    startActivity(intent);
                    desc.setText("");
                    price.setText("");
                    fare.setText("");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        SetupDrawerContent(NV);

        checkLocationPermission();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        View hView = NV.inflateHeaderView(R.layout.header);
        final ImageView imgvw = hView.findViewById(R.id.nav_img);
        final TextView textname = hView.findViewById(R.id.textname);//al5mis
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                snav_img = dataSnapshot.child("imgURI").getValue(String.class);
                sname = dataSnapshot.child("UserName").getValue(String.class);//al5mis
                Glide.with(Navigation.this).load(snav_img).into(imgvw);
                textname.setText(sname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onStart() {

        super.onStart();


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1); // Update location every second


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);


        // Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();
        pd.setMessage("Getting your Location...");
        pd.show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {





        if (b==false) {

            Latitude = String.valueOf(location.getLatitude());
            Longitude = String.valueOf(location.getLongitude());
            b=true ;
            pd.dismiss();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("LOAD")
                        .setMessage("SSSSSSS")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Navigation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            }else
                finishAffinity();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, (android.location.LocationListener) Navigation.this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public void update(View view) {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = new Intent(getApplicationContext(), update_frag.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    public void SelectItemDrawer(MenuItem menuItem) {
        Fragment MyFrag = null;
        Class fragmentclass = null;
        switch (menuItem.getItemId()) {
            case R.id.Setting:
                fragmentclass = Setting_Frag.class;
                UnVisible();
                break;

            case R.id.profile:
                fragmentclass = Profile_Frag.class;
                UnVisible();

                break;

            case R.id.About:
                fragmentclass = About_Frag.class;
                UnVisible();
                break;

            case R.id.history:
                Intent ii=new Intent(getApplicationContext(),Recycler.class);
                startActivity(ii);
                return;

            case R.id.switchdelivery:
                Intent i=new Intent(getApplicationContext(),recycler_delivery.class);
                i.putExtra("Delivery_Name",sname);
                startActivity(i);

                return;

            case R.id.logout:
                ref.child(mAuth.getCurrentUser().getUid()).child("token_id").removeValue().addOnSuccessListener(aVoid -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    //finishAffinity();
                    UnVisible();
                    //break;
                });

                return;

            default:
                fragmentclass = Navigation.class;
        }

        try {
            MyFrag = (Fragment) fragmentclass.newInstance();
        } catch (Exception e) {
            e.getStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, MyFrag).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mdrawer.closeDrawers();
    }

    private void SetupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SelectItemDrawer(item);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void UnVisible() {
        //order.setVisibility(View.GONE);
        order_linear.setVisibility(View.GONE);

    }

}