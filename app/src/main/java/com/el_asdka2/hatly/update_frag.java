package com.el_asdka2.hatly;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link update_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link update_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GALLERY_INTENT = 1;
    boolean IsPhotoExist;
    private Uri ImgUri;
    ProgressDialog UpdateProgressDialog;
    Context context ;
    String suserName ;
    String sphone ;
    String saddress ;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    StorageReference mStorage;
    String simage ;

    Button update;
    EditText name , phone , address;
    ImageView image;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public update_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static update_frag newInstance(String param1, String param2) {
        update_frag fragment = new update_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        IsPhotoExist = false;

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_update_frag, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("ProfileImages");
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = database.getReference("Users");

        name = view.findViewById(R.id.profile_update_user_name);
        phone = view.findViewById(R.id.profile_update_user_phone);
        address = view.findViewById(R.id.profile_update_user_address);
        update = view.findViewById(R.id.profile_update_user_data_btn);
        image = view.findViewById(R.id.profile_update_profile_img);

        UpdateProgressDialog = new ProgressDialog(context);
        UpdateProgressDialog.setCanceledOnTouchOutside(false);
        UpdateProgressDialog.setCancelable(false);
        UpdateProgressDialog.setMessage("Updating Your Data ...");

        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                suserName = dataSnapshot.child("UserName").getValue(String.class);
                sphone = dataSnapshot.child("Phone").getValue(String.class);
                saddress = dataSnapshot.child("address").getValue(String.class);
                simage =  dataSnapshot.child("imgURI").getValue(String.class);

                name.setText(suserName);
                address.setText(saddress);
                phone.setText(sphone);
                Glide.with(context.getApplicationContext()).load(simage).into(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        image.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "select"), GALLERY_INTENT);
        });
        update.setOnClickListener(view1 -> {
            String str_name = name.getText().toString();
            String str_phone = phone.getText().toString();
            String str_address = address.getText().toString();

            if (TextUtils.isEmpty(str_name)){
             //   Toast.makeText(context.getApplicationContext(), "Set your name please", Toast.LENGTH_SHORT).show();
                name.setError("Set your name please");
                name.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_phone)){
                //   Toast.makeText(context.getApplicationContext(), "Set your name please", Toast.LENGTH_SHORT).show();
                phone.setError("Set your phone please");
                phone.requestFocus();
                return;
            }
            if (str_phone.length() < 11){
                phone.setError("Invalid Phone Number !");
                phone.requestFocus();
                return;
            }
            if (str_phone.length() > 11){
                phone.setError("Invalid Phone Number !");
                phone.requestFocus();
                return;
            }
            if (!(str_phone.startsWith("01"))){
                phone.setError("Invalid Phone Number !");
                phone.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_address)){
                //   Toast.makeText(context.getApplicationContext(), "Set your name please", Toast.LENGTH_SHORT).show();
                address.setError("Set your address please");
                address.requestFocus();
                return;
            }

            String id1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UpdateProgressDialog.show();
            if (!IsPhotoExist) {
                myRef.child(id1).child("UserName").setValue(name.getText().toString());
                myRef.child(id1).child("Phone").setValue(phone.getText().toString());
                myRef.child(id1).child("address").setValue(address.getText().toString()).addOnCompleteListener(task -> {
                    UpdateProgressDialog.dismiss();
                    Toast.makeText(context.getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    UpdateProgressDialog.dismiss();
                    Toast.makeText(context.getApplicationContext(),"Failed Update",Toast.LENGTH_LONG).show();
                });
            }
            if (IsPhotoExist){
                mStorage.child(id1).delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        mStorage.child(id1).putFile(ImgUri).addOnSuccessListener(taskSnapshot -> {
                            myRef.child(id1).child("UserName").setValue(name.getText().toString());
                            myRef.child(id1).child("Phone").setValue(phone.getText().toString());
                            myRef.child(id1).child("address").setValue(address.getText().toString());
                            myRef.child(id1).child("imgURI").setValue(taskSnapshot.getDownloadUrl().toString())
                                    .addOnCompleteListener(task1 -> {
                                        UpdateProgressDialog.dismiss();
                                        Toast.makeText(context.getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();

                                    }).addOnFailureListener(e -> {
                                        UpdateProgressDialog.dismiss();
                                        Toast.makeText(context.getApplicationContext(),"Failed Update Data",Toast.LENGTH_LONG).show();
                                    });

                        });
                    }else {
                        UpdateProgressDialog.dismiss();
                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                Intent intent = new Intent(getActivity() , Navigation.class);
                startActivity(intent);
                ((Activity)context).finish();

                return true;

            }

            return false;
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            ImgUri = data.getData();

            Toast.makeText(context.getApplicationContext(), ImgUri.toString(), Toast.LENGTH_LONG).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), ImgUri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            IsPhotoExist = true;
        }
    }
}
