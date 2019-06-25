package com.el_asdka2.hatly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_Frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    String suserName;
    String semail;
    String sphone;
    String saddress;
    String snational;
    String simage;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public Profile_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_Frag newInstance(String param1, String param2) {
        Profile_Frag fragment = new Profile_Frag();
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
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getActivity(), Navigation.class);
                    startActivity(intent);
                    ((Activity)context).finish();

                    return true;

                }

                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        context = container.getContext();


        LayoutInflater lf = getActivity().getLayoutInflater();

        View view = lf.inflate(R.layout.fragment_profile, container, false);
        //Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
        final TextView email = view.findViewById(R.id.profile_user_email);
        final TextView address = view.findViewById(R.id.profile_user_address);
        final TextView phone = view.findViewById(R.id.profile_user_phone);
        final TextView national = view.findViewById(R.id.profile_user_national_id);
        final TextView name = view.findViewById(R.id.profile_user_name);
        final CircleImageView image =  view.findViewById(R.id.profile_user_img);
        FirebaseDatabase database;
        FirebaseAuth mAuth;
        DatabaseReference myRef;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = database.getReference("Users");


        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                suserName = dataSnapshot.child("UserName").getValue(String.class);
                semail = dataSnapshot.child("e-mail").getValue(String.class);
                sphone = dataSnapshot.child("Phone").getValue(String.class);
                saddress = dataSnapshot.child("address").getValue(String.class);
                snational = dataSnapshot.child("national-number").getValue(String.class);
                simage = dataSnapshot.child("imgURI").getValue(String.class);

                name.setText(suserName);
                address.setText(saddress);
                phone.setText(sphone);
                national.setText(snational);
                email.setText(semail);
                Glide.with(context.getApplicationContext()).load(simage).apply(new RequestOptions().override(250, 250)).into(image);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // Inflate the layout for this fragment

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
}
