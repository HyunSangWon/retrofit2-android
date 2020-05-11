package com.qr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.qr.app.R;
import com.qr.app.vo.ListVO;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private List<ListVO.Data> list;
    private Button listBtn;

    public ListAdapter(ArrayList<ListVO.Data> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, parent, false);

        TextView t_name = (TextView) convertView.findViewById(R.id.textName);
        TextView t_addr = (TextView) convertView.findViewById(R.id.textLocation);
        TextView t_cnt = (TextView) convertView.findViewById(R.id.textCnt);

        String name = list.get(position).name;
        String addr = list.get(position).addr;
        String cnt = list.get(position).cnt;

        t_name.setText(name); //업체 이름
        t_addr.setText(addr); //업체 주소
        t_cnt.setText(cnt); //담보물 수량

 /*
        Event !!
        listBtn = (Button) convertView.findViewById(R.id.listBtn);
        listBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("pos ======>  ", Integer.toString(pos));
            }
        });*/

        return convertView;
    }
}