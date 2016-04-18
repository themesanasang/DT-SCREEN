package com.nth.themesanasang.dtscreen;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nth.themesanasang.dtscreen.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFragment extends Fragment implements OnBackPressed {

    private String username;
    private String uname;

    private ProgressDialog progressDialog;

    View rootView;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private String pic_logo, pic_s_1, pic_s_2, pic_s_3;

    private Uri fileUri, fileUri2, fileUri3, fileUri4;
    private ImageView logo_patient, btnCapturePicture, btnCapturePicture2, btnCapturePicture3, pic_1, pic_2, pic_3;
    Button btnSaveScreen;

    public static ScreenFragment newInstance(String username) {
        ScreenFragment fragment = new ScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_screen, container, false);

        logo_patient = (ImageView) rootView.findViewById(R.id.logo_patient);
        logo_patient.setOnClickListener(new logo_patient());

        btnCapturePicture = (ImageView) rootView.findViewById(R.id.btnCapturePicture);
        btnCapturePicture.setOnClickListener(new btnCapturePicture());

        btnCapturePicture2 = (ImageView) rootView.findViewById(R.id.btnCapturePicture2);
        btnCapturePicture2.setOnClickListener(new btnCapturePicture2());

        btnCapturePicture3 = (ImageView) rootView.findViewById(R.id.btnCapturePicture3);
        btnCapturePicture3.setOnClickListener(new btnCapturePicture3());


        pic_1 = (ImageView) rootView.findViewById(R.id.pic_1);
        pic_2 = (ImageView) rootView.findViewById(R.id.pic_2);
        pic_3 = (ImageView) rootView.findViewById(R.id.pic_3);

        btnSaveScreen = (Button) rootView.findViewById(R.id.btnSaveScreen);
        btnSaveScreen.setOnClickListener(new btnSaveScreen());

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

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uname = getArguments().getString("username", "");
    }

    class logo_patient implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Go to function CapturePicture");
            // capture picture
            CapturePicture("1");
        }

    }

    class btnCapturePicture implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Go to function CapturePicture");
            // capture picture
            CapturePicture("2");
        }

    }

    class btnCapturePicture2 implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Go to function CapturePicture");
            // capture picture
            CapturePicture("3");
        }

    }

    class btnCapturePicture3 implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Go to function CapturePicture");
            // capture picture
            CapturePicture("4");
        }

    }

    class btnSaveScreen implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Post Data To Server");


            TextView txt_cid = (TextView) rootView.findViewById(R.id.s_cid);
            TextView txt_fullname = (TextView) rootView.findViewById(R.id.s_fullname);
            TextView txt_address = (TextView) rootView.findViewById(R.id.s_address);

            ContentResolver musicResolver = getActivity().getContentResolver();

            Bitmap bitmap1 = null;
            Bitmap bitmap2 = null;
            Bitmap bitmap3 = null;
            Bitmap bitmap4 = null;

            pic_logo = "";
            pic_s_1 = "";
            pic_s_2 = "";
            pic_s_3 = "";

            try {
                if(fileUri == null){
                    pic_logo = "";
                }else{
                    bitmap1 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri));
                    pic_logo = getStringImage(bitmap1);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                if(fileUri2 == null){
                    pic_s_1 = "";
                }else{
                    bitmap2 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri2));
                    pic_s_1 = getStringImage(bitmap2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                if(fileUri3 == null){
                    pic_s_2 = "";
                }else{
                    bitmap3 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri3));
                    pic_s_2 = getStringImage(bitmap3);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                if(fileUri4 == null){
                    pic_s_2 = "";
                }else{
                    bitmap4 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri4));
                    pic_s_2 = getStringImage(bitmap4);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            final String create_by = uname;
            final String cid = txt_cid.getText().toString();
            final String fullname = txt_fullname.getText().toString();
            final String address = txt_address.getText().toString();

            String tag_string_req = "req_screen";

            progressDialog.setMessage("กำลังบันทึกข้อมูลคัดกรอง ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppURLs.URL_Screen, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String keyError = jObj.getString("error");

                        if (keyError == "false") {

                            //String dd = jObj.getString("dd");

                            Toast.makeText(getActivity(),
                                    "okkkkk", Toast.LENGTH_LONG).show();

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
                    params.put("tag", "screen");
                    params.put("create_by", create_by);
                    params.put("cid", cid);
                    params.put("fullname", fullname);
                    params.put("address", address);
                    params.put("pic_logo", pic_logo);
                    params.put("pic_1", pic_s_1);
                    params.put("pic_2", pic_s_2);
                    params.put("pic_3", pic_s_3);

                    return params;
                }

            };

            // Adding request to  queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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

    private void CapturePicture(String chk){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(chk == "1"){
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }else if(chk == "2"){
            fileUri2 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri2);
        }else if(chk == "3"){
            fileUri3 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri3);
        }else{
            fileUri4 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri4);
        }

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                // successfully captured the image
                /*Toast.makeText(getActivity(),
                        "OK image capture", Toast.LENGTH_SHORT)
                        .show();*/

                logo_patient.setImageURI(fileUri);
                pic_1.setImageURI(fileUri2);
                pic_2.setImageURI(fileUri3);
                pic_3.setImageURI(fileUri4);

            } else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getActivity(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                //launchUploadActivity(false);

            } else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getActivity(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getActivity(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
               // Log.d(TAG, "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            uname = getArguments().getString("username", "");
        } else {
            uname = savedInstanceState.getString("uname");
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
