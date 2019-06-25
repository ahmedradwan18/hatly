package com.el_asdka2.hatly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    ProgressDialog LoginProgressDialog;
    //  ProgressBar progressBar;
    @BindView(R.id.login_mail_edt)
    EditText mail;
    @BindView(R.id.login_pass_edt)
    EditText pass;
    @BindView(R.id.login_sign_btn)
    Button SignUp;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.rdb_as_customer)
    RadioButton custLogin;
    @BindView(R.id.rdb_as_delivery)
    RadioButton delvrLogin;
    @BindView(R.id.signup_txt)
    TextView signup_txt;
    @BindView(R.id.forgotPass)
    TextView forgotpass;
    private FirebaseAuth mAuth;
    DatabaseReference Users_Ref;

    @OnClick(R.id.signup_txt)
    public void SignUpTxt(){
        startActivity( new Intent(login.this, signup.class));
        finish();
    }

    @OnClick(R.id.forgotPass)
    public void ForgotPass(){
        startActivity(new Intent(login.this, ForgotPassword.class));
        finish();
    }

    @OnClick(R.id.login_sign_btn)
    public void Login(){

        CheckDataAndLogin();

    }

    private void CheckDataAndLogin() {
        String str_email = mail.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        if (str_email.isEmpty()){
            mail.setError("Insert Your Mail Please");
            mail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            mail.setError("Invalid Email !");
            mail.requestFocus();
            return;
        }
        if (str_pass.isEmpty()){
            pass.setError("Insert Your Password Please");
            pass.requestFocus();
            return;
        }
        if (str_pass.length() < 6){
            pass.setError("Required 6 char at least");
            pass.requestFocus();
            return;
        }
        if (!custLogin.isChecked() && !delvrLogin.isChecked()){
            Toast.makeText(getApplicationContext(),"Set Your Identifier",Toast.LENGTH_LONG).show();
            return;
        }
        LoginWithUser(mail,pass);
    }

    private void LoginWithUser(EditText mail, EditText pass) {
        LoginProgressDialog.show();
        if (custLogin.isChecked()) {
            mAuth.signInWithEmailAndPassword(mail.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(login.this, task -> {
                        if (task.isSuccessful()) {

                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            String user_id = mAuth.getCurrentUser().getUid();

                            Users_Ref.child(user_id).child("token_id").setValue(token_id).addOnSuccessListener(aVoid -> {

                                Intent intent = new Intent(login.this, Navigation.class);
                                intent.putExtra("email", mail.getText().toString());
                                LoginProgressDialog.dismiss();
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                                mail.setText("");
                                pass.setText("");
                                delvrLogin.setChecked(false);
                                custLogin.setChecked(false);
                            }).addOnFailureListener(e -> {
                                LoginProgressDialog.dismiss();
                                Toast.makeText(login.this, "Failure", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            LoginProgressDialog.dismiss();
                            Toast.makeText(login.this, "Authentication failed because " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (delvrLogin.isChecked()) {
            mAuth.signInWithEmailAndPassword(mail.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(login.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            String user_id = mAuth.getCurrentUser().getUid();

                            DatabaseReference user_data = Users_Ref.child(user_id);
                            user_data.child("token_id").setValue(token_id).addOnSuccessListener(aVoid -> {
                                Intent intent = new Intent(login.this, recycler_delivery.class);
                                intent.putExtra("email", mail.getText().toString());
                                LoginProgressDialog.dismiss();
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Welcome Delivery", Toast.LENGTH_SHORT).show();
                                mail.setText("");
                                pass.setText("");
                                delvrLogin.setChecked(false);
                                custLogin.setChecked(false);

                            }).addOnFailureListener(e -> {
                                LoginProgressDialog.dismiss();
                                Toast.makeText(login.this, "Failure", Toast.LENGTH_SHORT).show();
                            });
                        } else
                            LoginProgressDialog.dismiss();
                            Toast.makeText(login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        LoginProgressDialog = new ProgressDialog(this);
        LoginProgressDialog.setCanceledOnTouchOutside(false);
        LoginProgressDialog.setCancelable(false);
        LoginProgressDialog.setMessage("Logging To Your Account ...");

        Users_Ref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(login.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                final LocationSettingsStates state = result1
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be
                        // fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(login.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            });
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}