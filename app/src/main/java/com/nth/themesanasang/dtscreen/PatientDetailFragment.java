package com.nth.themesanasang.dtscreen;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientDetailFragment extends Fragment implements OnBackPressed  {

    private ProgressDialog progressDialog;

    private TextView cid, fullname, address;
    Button btnedit, btndelete;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private String pic_logo, pic_s_1, pic_s_2, pic_s_3;

    private Uri fileUri, fileUri2, fileUri3, fileUri4;
    private ImageView btnCapturePicture, btnCapturePicture2, btnCapturePicture3, pic_1, pic_2, pic_3;

    private CircularImageView logo_patient;

    private String username, uname, pcid, pday;
    View rootView;


    public static PatientDetailFragment newInstance(String username, String cid, String day) {
        PatientDetailFragment fragment = new PatientDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("cid", cid);
        bundle.putString("day", day);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PatientDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_patient_detail, container, false);

        cid = (TextView)rootView.findViewById(R.id.s_cid_edit);
        fullname = (TextView)rootView.findViewById(R.id.s_fullname_edit);
        address = (TextView)rootView.findViewById(R.id.s_address_edit);

        //logo_patient = (ImageView) rootView.findViewById(R.id.logo_patient);
        logo_patient = (CircularImageView)rootView.findViewById(R.id.logo_patient_edit);
        //logo_patient.setOnClickListener(new logo_patient());

        btnCapturePicture = (ImageView) rootView.findViewById(R.id.btnCapturePicture_edit);
       // btnCapturePicture.setOnClickListener(new btnCapturePicture());

        btnCapturePicture2 = (ImageView) rootView.findViewById(R.id.btnCapturePicture2_edit);
        //btnCapturePicture2.setOnClickListener(new btnCapturePicture2());

        btnCapturePicture3 = (ImageView) rootView.findViewById(R.id.btnCapturePicture3_edit);
        //btnCapturePicture3.setOnClickListener(new btnCapturePicture3());

        pic_1 = (ImageView) rootView.findViewById(R.id.pic_1);
        pic_2 = (ImageView) rootView.findViewById(R.id.pic_2);
        pic_3 = (ImageView) rootView.findViewById(R.id.pic_3);

        btnedit = (Button) rootView.findViewById(R.id.btnEditScreen);
        //btnedit.setOnClickListener(new btnSaveScreen());

        btndelete = (Button) rootView.findViewById(R.id.btnDeleteScreen);
        //btnSaveScreen.setOnClickListener(new btnSaveScreen());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            onStop();
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            uname = getArguments().getString("username", "");
            pcid = getArguments().getString("cid", "");
            pday = getArguments().getString("day", "");
            ReadDataFromDB(uname, pcid,pday);
        }
    }


    private void ReadDataFromDB(final String uname, final String pcid, final String pday) {
        StringRequest jreq = new StringRequest(Request.Method.POST, AppURLs.URL_Patient, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String keyError = jObj.getString("error");

                    if (keyError == "false") {

                        //Log.d("cid=", jObj.getString("cid"));
                        String id_edit = jObj.getString("id");
                        String cid_edit = jObj.getString("cid");
                        String fullname_edit = jObj.getString("fullname");
                        String address_edit = jObj.getString("address");
                        String pic_logo_edit = jObj.getString("pic_logo");
                        String pic_1_edit = jObj.getString("pic_1");
                        String pic_2_edit = jObj.getString("pic_2");
                        String pic_3_edit = jObj.getString("pic_3");

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
                params.put("tag", "detail-patient");
                params.put("username", uname);
                params.put("cid", pcid);
                params.put("pday", pday);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jreq, "json_obj_req");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            uname = getArguments().getString("username", "");
            pcid = getArguments().getString("cid", "");
            pday = getArguments().getString("day", "");
            ReadDataFromDB(uname,pcid,pday);
        } else {
            uname = savedInstanceState.getString("uname");
            pcid = savedInstanceState.getString("pcid");
            pday = savedInstanceState.getString("pday");
            ReadDataFromDB(uname,pcid,pday);
        }
    }


    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("uname", uname);
        outState.putString("pcid", pcid);
        outState.putString("pday", pday);
    }

    @Override
    public void onBackPressed(){
        getActivity().getSupportFragmentManager().popBackStack();
    }


}
