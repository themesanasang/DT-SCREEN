package com.nth.themesanasang.dtscreen;

/**
 * Created by themesanasang on 8/4/59.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import  com.mikhaellopez.circularimageview.CircularImageView;

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


        //ImageView pic_logo = (ImageView) view.findViewById(R.id.user_icon);
        CircularImageView pic_logo = (CircularImageView)view.findViewById(R.id.user_icon);
        pic_logo.setBorderColor(R.color.primary_blue);
        pic_logo.setBorderWidth(1);
        /*circularImageView.addShadow();
        circularImageView.setShadowRadius(15);
        circularImageView.setShadowColor(Color.RED);*/

        if(mData.get(position).get("pic_logo").toString() != "nodata") {
            byte[] decodedString = Base64.decode(mData.get(position).get("pic_logo").toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if(decodedString.length > 10000) {
                pic_logo.setImageBitmap(decodedByte);
            }
        }else{
            pic_logo.setImageResource(R.drawable.ic_account_circle);
        }

        TextView textView1 = (TextView)view.findViewById(R.id.p_cid);
        textView1.setText(mData.get(position).get("cid").toString());

        TextView textView2 = (TextView)view.findViewById(R.id.p_name);
        textView2.setText("ชื่อ: "+mData.get(position).get("fullname").toString());

        TextView textView3 = (TextView)view.findViewById(R.id.p_day);
        textView3.setText(mData.get(position).get("regdate").toString());

        TextView textView4 = (TextView)view.findViewById(R.id.p_by);
        textView4.setText("คัดกรองโดย: "+mData.get(position).get("screen_by").toString());

        return view;
    }
}
