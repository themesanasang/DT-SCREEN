package com.nth.themesanasang.dtscreen;

/**
 * Created by themesanasang on 8/4/59.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nth.themesanasang.dtscreen.R;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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




        if(mData.get(position).get("pic_logo").toString() != "nodata") {
            byte[] decodedString = Base64.decode(mData.get(position).get("pic_logo").toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            ImageView pic_logo = (ImageView) view.findViewById(R.id.user_icon);

            Log.d("Msg = ", String.valueOf(decodedString.length));

            if(decodedString.length > 10000) {
                pic_logo.setImageBitmap(decodedByte);
            }
        }

        TextView textView1 = (TextView)view.findViewById(R.id.p_cid);
        textView1.setText("รหัสบัตรประชาชน: "+mData.get(position).get("cid").toString());
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