package com.nth.themesanasang.dtscreen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Bind;

import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by themesanasang on 22/1/59.
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Session session;


    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        session = new Session(LoginActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        _loginButton.setTypeface(typeface);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = _usernameText.getText().toString();
                String password = _passwordText.getText().toString();

                if (username.trim().length() > 0 && password.trim().length() > 0){
                    checkLogin(username, password);
                }else{
                    Snackbar.make(v, "กรุณากรอกชื่อผู้ใช้งานหรือรหัสผ่าน!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void checkLogin(final String username, final String password){
        String tag_string_req = "req_login";

        progressDialog.setMessage("กำลังเข้าสู่ระบบ ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL_Login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String keyError = jObj.getString("error");


                    if (keyError == "false") {

                        String userId = jObj.getString("id");
                        String name = jObj.getString("name");
                        String uname = jObj.getString("username");

                        session.setLogin(true);
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);

                        intent.putExtra("user_id", userId);
                        intent.putExtra("name", name);
                        intent.putExtra("username", uname);

                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
