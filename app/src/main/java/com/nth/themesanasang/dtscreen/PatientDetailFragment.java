package com.nth.themesanasang.dtscreen;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientDetailFragment extends Fragment implements OnBackPressed  {

    private ProgressDialog progressDialog;

    private TextView cid, fullname, age, address;
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

    private String username, uname, pcid, pday, id_edit;
    View rootView;
    EditText txtCode;


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
        age = (TextView)rootView.findViewById(R.id.s_age_edit);
        address = (TextView)rootView.findViewById(R.id.s_address_edit);

        logo_patient = (CircularImageView)rootView.findViewById(R.id.logo_patient_edit);
        logo_patient.setOnClickListener(new logo_patient());

        btnCapturePicture = (ImageView) rootView.findViewById(R.id.btnCapturePicture_edit);
        btnCapturePicture.setOnClickListener(new btnCapturePicture());

        btnCapturePicture2 = (ImageView) rootView.findViewById(R.id.btnCapturePicture2_edit);
        btnCapturePicture2.setOnClickListener(new btnCapturePicture2());

        btnCapturePicture3 = (ImageView) rootView.findViewById(R.id.btnCapturePicture3_edit);
        btnCapturePicture3.setOnClickListener(new btnCapturePicture3());

        pic_1 = (ImageView) rootView.findViewById(R.id.pic_1_edit);
        pic_2 = (ImageView) rootView.findViewById(R.id.pic_2_edit);
        pic_3 = (ImageView) rootView.findViewById(R.id.pic_3_edit);

        btnedit = (Button) rootView.findViewById(R.id.btnEditScreen);
        btnedit.setOnClickListener(new btnEditScreen());

        btndelete = (Button) rootView.findViewById(R.id.btnDeleteScreen);
        btndelete.setOnClickListener(new btnDeleteScreen());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        txtCode = (EditText) rootView.findViewById(R.id.s_cid_edit);
        txtCode.addTextChangedListener( new TextWatcher() {
            boolean isEdiging;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }


            @Override
            public void afterTextChanged(Editable s) {
                if(isEdiging) return;
                isEdiging = true;
                // removing old dashes
                StringBuilder sb = new StringBuilder();
                sb.append(s.toString().replace("-", ""));

                if (sb.length()> 2)
                    sb.insert(1, "-");
                if (sb.length()> 6)
                    sb.insert(6, "-");
                if (sb.length()> 12)
                    sb.insert(12, "-");
                if (sb.length()> 15)
                    sb.insert(15, "-");
                if(sb.length()> 17)
                    sb.delete(17, sb.length());

                s.replace(0, s.length(), sb.toString());
                isEdiging = false;
            }
        });

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

    class btnDeleteScreen implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Delete Data Screen ID=" + id_edit);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setTitle("คัดกรอง")
                    .setMessage("คุณต้องการลบข้อมูลคัดกรอง?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteScreen();
                        }
                    })
                    .setNegativeButton("ไม่", null)						//Do nothing on no
                    .show();
        }
    }

    public void deleteScreen()
    {
        String tag_string_req = "req_screen_delete";

            progressDialog.setMessage("กำลังลบข้อมูลคัดกรอง ...");
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

                            PatientFragment fragment = PatientFragment.newInstance(uname);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction =        fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContent, fragment);
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
                    params.put("tag", "screen-delete");
                    params.put("id_edit", id_edit);

                    return params;
                }

            };

            // Adding request to  queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



    class btnEditScreen implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("Tag ", "Edit Data Screen ID="+id_edit);

            final TextView txt_cid = (TextView) rootView.findViewById(R.id.s_cid_edit);
            final TextView txt_fullname = (TextView) rootView.findViewById(R.id.s_fullname_edit);
            final TextView txt_age = (TextView) rootView.findViewById(R.id.s_age_edit);
            final TextView txt_address = (TextView) rootView.findViewById(R.id.s_address_edit);

            ContentResolver musicResolver = getActivity().getContentResolver();

            Bitmap bitmap1 = null;
            Bitmap bitmap2 = null;
            Bitmap bitmap3 = null;
            Bitmap bitmap4 = null;

            pic_logo = "";
            pic_s_1 = "";
            pic_s_2 = "";
            pic_s_3 = "";

            if(fileUri == null){
                pic_logo = "";
            }else{
                bitmap1 = getBitmap(fileUri);
                pic_logo = getStringImage(bitmap1);
            }

            if(fileUri2 == null){
                pic_s_1 = "";
            }else{
                //bitmap2 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri2));

                bitmap2 = getBitmap(fileUri2);
                pic_s_1 = getStringImage(bitmap2);
            }

            if(fileUri3 == null){
                pic_s_2 = "";
            }else{
                //bitmap3 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri3));

                bitmap3 = getBitmap(fileUri3);
                pic_s_2 = getStringImage(bitmap3);
            }

            if(fileUri4 == null){
                pic_s_3 = "";
            }else{
                //bitmap4 = BitmapFactory.decodeStream(musicResolver.openInputStream(fileUri4));

                bitmap4 = getBitmap(fileUri4);
                pic_s_3 = getStringImage(bitmap4);
            }


            final String create_by = uname;
            final String cid = txt_cid.getText().toString();
            final String fullname = txt_fullname.getText().toString();
            final String age = txt_age.getText().toString();
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

                            PatientFragment fragment = PatientFragment.newInstance(uname);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction =        fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContent, fragment);
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
                    params.put("tag", "screen-edit");
                    params.put("id_edit", id_edit);
                    params.put("cid", cid);
                    params.put("fullname", fullname);
                    params.put("age", age);
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


    private void ReadDataFromDB(final String uname, final String pcid, final String pday) {
        StringRequest jreq = new StringRequest(Request.Method.POST, AppURLs.URL_Patient, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String keyError = jObj.getString("error");

                    if (keyError == "false") {

                        //Log.d("cid=", jObj.getString("cid"));
                        id_edit = jObj.getString("id");
                        String cid_edit = jObj.getString("cid");
                        String fullname_edit = jObj.getString("fullname");
                        String age_edit = jObj.getString("age");
                        String address_edit = jObj.getString("address");
                        String pic_logo_edit = jObj.getString("pic_logo");
                        String pic_1_edit = jObj.getString("pic_1");
                        String pic_2_edit = jObj.getString("pic_2");
                        String pic_3_edit = jObj.getString("pic_3");

                        //logo_patient
                        byte[] decodedString1 = Base64.decode(pic_logo_edit, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        if(decodedString1.length > 10000) {
                            logo_patient.setImageBitmap(decodedByte1);
                            logo_patient.setBorderColor(R.color.iron);
                            logo_patient.setBorderWidth(1);
                        }

                        cid.setText(cid_edit);
                        fullname.setText(fullname_edit);
                        age.setText(age_edit);
                        address.setText(address_edit);

                        //pic_1
                        byte[] decodedString2 = Base64.decode(pic_1_edit, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        if(decodedString2.length > 10000) {
                            pic_1.setImageBitmap(decodedByte2);
                        }

                        //pic_2
                        byte[] decodedString3 = Base64.decode(pic_2_edit, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        if(decodedString3.length > 10000) {
                            pic_2.setImageBitmap(decodedByte3);
                        }

                        //pic_3
                        byte[] decodedString4 = Base64.decode(pic_3_edit, Base64.DEFAULT);
                        Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                        if(decodedString4.length > 10000) {
                            pic_3.setImageBitmap(decodedByte4);
                        }

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


    private Bitmap getBitmap(Uri path) {

        Uri uri = path;
        InputStream in = null;
        try {
            ContentResolver mContentResolver = getActivity().getContentResolver();
            final int IMAGE_MAX_SIZE = 500000000;
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b = null;
            in = mContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            return b;
        } catch (IOException e) {
            return null;
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

                logo_patient.setImageURI(fileUri);
                logo_patient.setBorderColor(R.color.iron);
                logo_patient.setBorderWidth(1);

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


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
