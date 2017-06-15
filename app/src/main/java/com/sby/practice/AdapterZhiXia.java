package com.sby.practice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.sby.practice.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kowal on 2017/4/26.
 */

public class AdapterZhiXia extends RecyclerView.Adapter<AdapterZhiXia.MyViewHolder>
{
    private List<MKOLSearchRecord> list;
    private Context context;
    private ArrayList<MKOLUpdateElement> localElement;

    public AdapterZhiXia(Context context, List<MKOLSearchRecord> list, ArrayList<MKOLUpdateElement> elements)
    {
        this.context = context;
        this.list = list;
        this.localElement = elements;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.text_item_map_parent, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.tv_city_name.setText(list.get(position).cityName);
        holder.tv_size.setText(FormatUtils.formatDataSize(list.get(position).size));
        // 加载列表时，如果已经本地下载过，则不允许点击
        if (localElement != null)
        {
            for (int i = 0; i < localElement.size(); i++)
            {
                if (localElement.get(i).cityID == list.get(position).cityID)
                {
                    holder.tv_downAll.setClickable(false);
                    Drawable drawable = context.getResources().getDrawable(R.drawable.downloaded);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_downAll.setCompoundDrawables(drawable, null, null, null);
                }
            }
        }
        if (null != listener)
        {
            final int pos = holder.getLayoutPosition();
            holder.tv_downAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onClickDownload(holder.itemView, list.get(pos));
                    holder.tv_downAll.setClickable(false);
                    Drawable drawable = context.getResources().getDrawable(R.drawable.downloaded);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_downAll.setCompoundDrawables(drawable, null, null, null);
                    holder.tv_status.setText("(正在下载)");
                    holder.tv_status.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_city_name;
        TextView tv_status;
        TextView tv_size;
        TextView tv_downAll;
        ImageView iv_status;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_downAll = (TextView) itemView.findViewById(R.id.tv_downAll);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            iv_status = (ImageView) itemView.findViewById(R.id.iv_status);
        }
    }

    private OnClickListener listener = null;

    public interface OnClickListener
    {
        void onClickDownload(View view, MKOLSearchRecord msr);
    }

    public void setOnItemClickLitener(OnClickListener mOnItemClickLitener)
    {
        this.listener = mOnItemClickLitener;
    }

    public void updateUi()
    {
        notifyDataSetChanged();
    }

}
