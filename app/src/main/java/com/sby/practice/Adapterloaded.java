package com.sby.practice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.sby.practice.utils.FormatUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kowal on 2017/4/25.
 */

public class Adapterloaded extends RecyclerView.Adapter<Adapterloaded.MyViewHolder>
{
    private ArrayList<MKOLUpdateElement> list;
    private Context mContext;

    public Adapterloaded(Context context, ArrayList<MKOLUpdateElement> list)
    {
        this.list = list;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.text_local_item_complete, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.tv_name.setText(list.get(position).cityName);
        holder.tv_size.setText(FormatUtils.formatDataSize(list.get(position).size));
    }

    @Override
    public int getItemCount()
    {
        return list.size() == 0 ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_city_name)
        TextView tv_name;
        @BindView(R.id.tv_size)
        TextView tv_size;
        @BindView(R.id.bt_seemap)
        Button bt_seemap;
        @BindView(R.id.bt_del)
        Button bt_del;
        @BindView(R.id.ll_bottom)
        LinearLayout ll_bottom;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
