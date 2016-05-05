package com.nth.themesanasang.dtscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientFragment extends Fragment implements OnBackPressed {

    private String username;
    private String uname;

    View rootView;

    PatientAdapter adapter;
    ListView list;
    ArrayList<HashMap<String, String>> Item_List;
    ListAdapter adapterAll;


    public static PatientFragment newInstance(String username) {
        PatientFragment fragment = new PatientFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PatientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_patient, container, false);

        list = (ListView)rootView.findViewById(R.id.patient_list);
        Item_List = new ArrayList<HashMap<String, String>>();

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long arg3) {

                Log.d("Msg Patient Item ","click:"+String.valueOf(position));

                String cid = ((TextView)view.findViewById(R.id.p_cid)).getText().toString();
                String day = ((TextView)view.findViewById(R.id.p_day)).getText().toString();

                PatientDetailFragment fragment2 = PatientDetailFragment.newInstance(uname,cid,day);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return rootView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            uname = username; //getArguments().getString("username", "");
            ReadDataFromDB(uname);
        }
    }

    private void ReadDataFromDB(final String uname) {

        CustomRequest jreq = new CustomRequest(Method.POST, AppURLs.URL_Patient, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("username", uname);

                    JSONArray ja = response.getJSONArray("orders");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jobj = ja.getJSONObject(i);
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put("cid", jobj.getString("cid"));
                        item.put("fullname", jobj.getString("fullname"));
                        item.put("pic_logo", jobj.getString("pic_logo"));
                        item.put("screen_by", jobj.getString("screen_by"));
                        item.put("regdate", jobj.getString("regdate"));

                        Item_List.add(item);
                    }

                    adapter = new PatientAdapter(getActivity(), Item_List);
                    list.setAdapter(adapter);

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
                params.put("tag", "patient");
                params.put("username", uname);
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


}
