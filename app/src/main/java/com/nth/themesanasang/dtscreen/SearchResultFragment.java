package com.nth.themesanasang.dtscreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment implements OnBackPressed {

    View rootView;

    PatientAdapter adapter;
    ListView list;
    ArrayList<HashMap<String, String>> Item_List;

    private String uname, tsearch;

    public static SearchResultFragment newInstance(String username, String txtsearch) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("txtsearch", txtsearch);
        fragment.setArguments(bundle);
        return fragment;
    }

    public SearchResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search_result, container, false);

        list = (ListView)rootView.findViewById(R.id.search_patient_list);
        Item_List = new ArrayList<HashMap<String, String>>();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
            uname = getArguments().getString("username", "");
            tsearch = getArguments().getString("txtsearch", "");

            ReadDataFromDB(uname, tsearch);
        }
    }

    private void ReadDataFromDB(final String uname, final String tsearch) {

        CustomRequest jreq = new CustomRequest(Request.Method.POST, AppURLs.URL_Patient, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("username", uname);
                    Log.d("txtsearch", tsearch);

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

                        Log.d("Msg = ", jobj.getString("cid"));
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
                params.put("tag", "patient-search");
                params.put("username", uname);
                params.put("txtsearch", tsearch);
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
            tsearch = getArguments().getString("txtsearch", "");
            ReadDataFromDB(uname, tsearch);
        } else {
            uname = savedInstanceState.getString("uname");
            tsearch = savedInstanceState.getString("tsearch");
            ReadDataFromDB(uname, tsearch);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("uname", uname);
        outState.putString("tsearch", tsearch);
    }

    @Override
    public void onBackPressed(){
        getActivity().getSupportFragmentManager().popBackStack();
    }


}
