package com.el_asdka2.hatly;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link About_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class About_Frag extends Fragment {
    Button contact;
    ImageView radwan, abdo, karim,img;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public About_Frag() {
        // Required empty public constructor
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

                    return true;

                }

                return false;
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment About_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static About_Frag newInstance(String param1, String param2) {
        About_Frag fragment = new About_Frag();
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
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_about, container, false);


        ImageView loc = view.findViewById(R.id.location_img);

        radwan = view.findViewById(R.id.radwan_img);
        karim = view.findViewById(R.id.karim_img);

        // Button To Go To Our Location On Google Map
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity2.class);
                startActivity(intent);
            }
        });

        // Radwan Image
        radwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());//TODO : check this
                View dialog_view = layoutInflaterAndroid.inflate(R.layout.dialog_programmer, null);
                contact = dialog_view.findViewById(R.id.dialogProgContact);

                final AlertDialog alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity())
                        .setView(dialog_view)
                        .create();

                alertDialogBuilderUserInput.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Contact With Radwan By Mail
                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"aradwan054@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                alertDialogBuilderUserInput.show();
            }
        });

        // Karim Image
        karim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());//TODO : check this
                View dialog_view = layoutInflaterAndroid.inflate(R.layout.dialog_programmer, null);
                contact = dialog_view.findViewById(R.id.dialogProgContact);
                img = dialog_view.findViewById(R.id.dialogProgImg);
                img.setImageResource(R.drawable.karim);
                final AlertDialog alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity())
                        .setView(dialog_view)
                        .create();

                alertDialogBuilderUserInput.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                // Contact With Karim By Mail
                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"karimelsayed710@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                alertDialogBuilderUserInput.show();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
