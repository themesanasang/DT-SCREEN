package com.example.themesanasang.scandocnth;

/**
 * Created by themesanasang on 8/4/59.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PatientAdapter extends BaseAdapter {
    Context mContext;
    private List<? extends Map<String, ?>> mData;

    public PatientAdapter(Context context, List<? extends Map<String, ?>> data) {
        this.mContext= context;
        mData = data;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.list_patient_item, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.p_cid);
        textView1.setText("CID: "+mData.get(position).get("cid").toString());
        //textView1.setText("CID: "+mData.get(position).get("cid").toString());

        TextView textView2 = (TextView)view.findViewById(R.id.p_name);
        textView2.setText("ชื่อ: "+mData.get(position).get("fullname").toString());

        TextView textView3 = (TextView)view.findViewById(R.id.p_day);
        textView3.setText("วันที่คัดกรอง: "+mData.get(position).get("regdate").toString());

        TextView textView4 = (TextView)view.findViewById(R.id.p_by);
        textView4.setText("คัดกรองโดย: "+mData.get(position).get("screen_by").toString());

        return view;
    }
}
