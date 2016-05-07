package com.nth.themesanasang.dtscreen;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nth.themesanasang.dtscreen.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements OnBackPressed {

    private String username;
    private String uname;

    private ProgressDialog progressDialog;
    View rootView;

    Button btnSaveProfile;
    TextView pr_fullname, pr_username, pr_password, pr_address;

    public static ProfileFragment newInstance(String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        btnSaveProfile = (Button) rootView.findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setOnClickListener(new saveProfile());

        pr_fullname = (TextView) rootView.findViewById(R.id.pro_fullname);
        pr_username = (TextView) rootView.findViewById(R.id.pro_username);
        pr_password = (TextView) rootView.findViewById(R.id.pro_password);
        pr_address  = (TextView) rootView.findViewById(R.id.pro_address);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uname = getArguments().getString("username", "");
        ReadDataFromDB(uname);
    }


    class saveProfile implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Go to function saveProfileUser");

            String name = pr_fullname.getText().toString();
            String pass = pr_password.getText().toString();
            String address = pr_address.getText().toString();

            if (name.trim().length() > 0 && address.trim().length() > 0){
                saveProfileUser(uname, name, pass, address);
            }else{
                Snackbar.make(v, "กรุณากรอกข้อมูล!", Snackbar.LENGTH_LONG)
                        .show();
            }
        }

    }


    public  void saveProfileUser(final String uname, final String name, final String pass, final String address){

        progressDialog.setMessage("กำลังแก้ไขข้อมูล ...");
        showDialog();

        String tag_string_req = "req_profile_edit";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL_Profile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String keyError = jObj.getString("error");

                    if (keyError == "false") {

                        Toast.makeText(getActivity(),
                                "แก้ไขข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();

                        PatientFragment fragment = PatientFragment.newInstance(uname);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flContent, fragment, "Patient");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "profile-edit");
                params.put("username", uname);
                params.put("name", name);
                params.put("password", pass);
                params.put("address", address);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        hideDialog();
    }


    private void ReadDataFromDB(final String uname) {

        Log.d("Username = ", uname);

        StringRequest jreq = new StringRequest(Request.Method.POST, AppURLs.URL_Profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String keyError = jObj.getString("error");

                    if (keyError == "false") {

                        //Log.d("cid=", jObj.getString("cid"));
                        String txtname = jObj.getString("name");
                        String txtuname = jObj.getString("username");
                        String txtaddress = jObj.getString("address");

                        pr_fullname.setText(txtname);
                        pr_username.setText(txtuname);
                        pr_address.setText(txtaddress);

                    }else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "profile");
                params.put("username", uname);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jreq, "json_obj_req_profile");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            uname = getArguments().getString("username", "");
            ReadDataFromDB(uname);
        } else {
            uname = savedInstanceState.getString("uname");
            ReadDataFromDB(uname);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("uname", uname);
    }


    @Override
    public void onBackPressed(){
        getActivity().getSupportFragmentManager().popBackStack();
    }


    public void onStop() {
        super.onStop();
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
