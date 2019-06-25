package com.el_asdka2.hatly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class signup extends AppCompatActivity{
    @BindView(R.id.profile_img)
    ImageView image_customer;
    @BindView(R.id.sign_email_edt)
    EditText mail;
    @BindView(R.id.sign_pass_edt)
    EditText pass;
    @BindView(R.id.sign_user_edt)
    EditText username;
    @BindView(R.id.sign_phone_edit)
    EditText mobile;
    @BindView(R.id.sign_idcard_edt)
    EditText nationalNum;
    @BindView(R.id.sign_address_edt)
    EditText address;
    @BindView(R.id.SignUp_btn)
    Button signUp;
    @BindView(R.id.signin_txt)
    TextView login_txt;
    @BindView(R.id.rdb_male)
    RadioButton male;
    @BindView(R.id.rdb_female)
    RadioButton female;
    FirebaseDatabase database;
    DatabaseReference UsersRef;
    boolean gabalsora;
    private FirebaseAuth mAuth;
    private static final int GALLERY_INTENT = 1;
    private StorageReference mStorageRef;
    private Uri imgUri;
    boolean c;
    ProgressDialog SignUpProgressDialog;
    Map<String, String> UsersMap = new HashMap<>();

    @OnClick(R.id.signin_txt)
    public void SignIn(){
        Intent intent = new Intent(signup.this, login.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.SignUp_btn)
    public void SignUp(){
        try {

            CheckDataAndCreateNewUser();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error Sign Up " + e.getMessage(), Toast.LENGTH_LONG).show();

        }


    }

    private void CreateNewUser(EditText mail, EditText pass) {
        SignUpProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(mail.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(signup.this, task -> {


                    if (!task.isSuccessful()) {
                        Toast.makeText(signup.this, "Authentication failed." + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        SignUpProgressDialog.dismiss();
                    } else {
                        FirebaseUser user = mAuth.getCurrentUser();

                        String token_id =  FirebaseInstanceId.getInstance().getToken();

                        mStorageRef.child("ProfileImages/" + mAuth.getCurrentUser().getUid())
                                .putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(signup.this, "uploaded", Toast.LENGTH_SHORT).show();

                            UsersMap.put("e-mail", mail.getText().toString());
                            UsersMap.put("Password", pass.getText().toString());
                            UsersMap.put("Phone", mobile.getText().toString());
                            UsersMap.put("UserName", username.getText().toString());
                            UsersMap.put("national-number", nationalNum.getText().toString());
                            UsersMap.put("address", address.getText().toString());
                            UsersMap.put("token_id", token_id);
                            UsersMap.put("imgURI", taskSnapshot.getDownloadUrl().toString());

                            UsersRef.child(user.getUid()).setValue(UsersMap).addOnCompleteListener(task1 -> {

                                Toast.makeText(signup.this, "Account Created", Toast.LENGTH_SHORT).show();
                                SignUpProgressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), login.class);
                                intent.putExtra("Email",mail.getText().toString());
                                startActivity(intent);
                                finish();

                                mail.setText("");
                                pass.setText("");
                                mobile.setText("");
                                username.setText("");
                                address.setText("");
                                nationalNum.setText("");

                            });

                        }).addOnFailureListener(e -> Toast.makeText(signup.this, "exception is " + e.toString(), Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void CheckDataAndCreateNewUser() {

        String str_name = username.getText().toString().trim();
        String str_email = mail.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_address = address.getText().toString().trim();
        String str_phone = mobile.getText().toString().trim();
        String str_nationalID = nationalNum.getText().toString().trim();
        try {
            if (!gabalsora){
                Toast.makeText(getApplicationContext(),"Select You Profile Pic Please",Toast.LENGTH_LONG).show();
                return;
            }
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
            if (str_name.isEmpty()){
                username.setError("Insert Your Name Please");
                username.requestFocus();
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
            if (str_phone.isEmpty()){
                mobile.setError("Insert Your Mobile Phone Please");
                mobile.requestFocus();
                return;
            }
            if (!str_phone.startsWith("01")){
                mobile.setError("Mobile Phone Must Start With 01 ");
                mobile.requestFocus();
                return;
            }
            if (str_phone.length() < 11 ){
                mobile.setError("Invalid Mobile Number");
                mobile.requestFocus();
                return;
            }
            if(str_nationalID.isEmpty()){
                nationalNum.setError("Please Insert Your National Number");
                nationalNum.requestFocus();
                return;
            }
            if (str_nationalID.length() < 14){
                nationalNum.setError("Invalid National Number");
                nationalNum.requestFocus();
                return;
            }
            if (str_address.isEmpty()){
                address.setError("Insert Your Address Please");
                address.requestFocus();
                return;
            }

            if (!male.isChecked() && !female.isChecked()){
                Toast.makeText(getApplicationContext(),"You Must Select Male Or Female At Least !",Toast.LENGTH_LONG).show();
                return;
            }
            if (male.isChecked()) {
                UsersMap.put("Gender", "male");
            }
            if (female.isChecked()) {
                UsersMap.put("Gender", "female");
            }
            CreateNewUser(mail,pass);


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Catch Error"+"\n"+ e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mail.clearFocus();

        SignUpProgressDialog = new ProgressDialog(this);
        SignUpProgressDialog.setCanceledOnTouchOutside(false);
        SignUpProgressDialog.setCancelable(false);
        SignUpProgressDialog.setMessage("Creating Your Account ...");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        UsersRef = database.getReference("Users");

        gabalsora = false;

        signUp.setEnabled(true);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imgUri = data.getData();

            Toast.makeText(this, imgUri.toString(), Toast.LENGTH_LONG).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                image_customer.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            gabalsora = true;

        }
    }


    public void uploadProfile(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), GALLERY_INTENT);


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}