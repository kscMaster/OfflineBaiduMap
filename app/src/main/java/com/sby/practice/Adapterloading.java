package com.sby.practice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下载状态
 * UNDEFINED 	0
 * DOWNLOADING 	1
 * WAITING 	2
 * SUSPENDED 	3
 * FINISHED 	4
 * eOLDSMd5Error 	5
 * eOLDSNetError 	6
 * eOLDSIOError 	7
 * eOLDSWifiError 	8
 * eOLDSFormatError 	9
 * eOLDSInstalling 	10
 * Created by kowal on 2017/4/25.
 */

public class Adapterloading extends RecyclerView.Adapter<Adapterloading.MyViewHolder>
{

    private ArrayList<MKOLUpdateElement> list;
    private Context mContext;

    private MKOLUpdateElement e;

    private StartDownload startListener = null;
    private StopDownload stopListener = null;
    private DelDownload delListener = null;
    private UpdateDownload updateListener = null;

    public interface StartDownload
    {
        void start(int cityId, int position);
    }

    public interface StopDownload
    {
        void stop(int cityId, int position, boolean flag);
    }

    public interface DelDownload
    {
        void del(int cityId, int position);
    }

    public interface UpdateDownload
    {
        void update(int cityId, int position);
    }

    public void onStartClick(StartDownload listener)
    {
        this.startListener = listener;
    }

    public void onStopClick(StopDownload listener)
    {
        this.stopListener = listener;
    }

    public void onDelClick(DelDownload delListener)
    {
        this.delListener = delListener;
    }

    /**
     * 删除
     *
     * @param position
     */
    public void removeData(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void onUpdateClick(UpdateDownload listener)
    {
        this.updateListener = listener;
    }

    public void onUpdateUi(int position, MKOLUpdateElement update)
    {
        list.set(position, update);
        notifyItemChanged(position);
    }

    /**
     * 适配器重载
     */
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_downStatus)
        TextView tv_downStatus;
        @BindView(R.id.pb_bar)
        ProgressBar pb_bar;
        @BindView(R.id.tv_size)
        TextView tv_size;
        @BindView(R.id.iv_pic)
        ImageView iv_pic;
        @BindView(R.id.bt_start)
        Button bt_start;
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

    public Adapterloading(Context context, ArrayList<MKOLUpdateElement> list)
    {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.text_local_item_downing, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        e = list.get(position);
        holder.tv_name.setText(e.cityName);
//        switchStatus(holder, e.status, position, e);
        switch (e.status)
        {
            case 1: // 下载中
                holder.tv_downStatus.setText("(正在下载)");
                holder.tv_downStatus.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.bt_start.setText("暂停");
                holder.pb_bar.setMax(e.serversize);
                holder.pb_bar.setProgress(e.size);
                holder.tv_size.setText(e.ratio + "%");
                holder.ll_bottom.setVisibility(View.VISIBLE);
                holder.bt_start.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (stopListener != null)
                        {
                            Log.e("aaa","点到暂停");
                            stopListener.stop(e.cityID, position, true);
                        }
                    }
                });
                break;
            case 2: // 等待
                holder.tv_downStatus.setText("(正在等待)");
                holder.tv_downStatus.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
                holder.bt_start.setText("开始下载");
                holder.tv_size.setText(e.ratio + "%");
                holder.bt_start.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if ( startListener != null)
                        {
                          Log.e("aaa","点到开始");
                            startListener.start(position, e.cityID);
                        }
                    }
                });
                break;
            case 3: // 暂停
                holder.tv_downStatus.setText("(暂停中)");
                holder.tv_downStatus.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
                holder.bt_start.setText("开始下载");
                holder.tv_size.setText(e.ratio + "%");
                holder.bt_start.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if ( startListener != null)
                        {
                            Log.e("aaa","点到开始");
                            startListener.start(position, e.cityID);
                        }
                    }
                });
                break;
            case 4: // 下载完成
                holder.tv_downStatus.setText("(下载完成)");
                holder.tv_downStatus.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                holder.bt_start.setText("查看地图");
                holder.tv_size.setText(e.ratio + "%");
                holder.bt_start.setClickable(false);
                break;
            case 10: // 安装完成
                holder.tv_downStatus.setText("(安装完成)");
                holder.tv_downStatus.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                holder.bt_start.setText("查看地图");
                holder.tv_size.setText(e.ratio + "%");
                holder.bt_start.setClickable(false);
                break;
            default: // 下载出错
                holder.tv_downStatus.setText("(下载出错)");
                holder.tv_downStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.pb_bar.setVisibility(View.INVISIBLE);
                holder.tv_size.setText(e.ratio + "%");
                holder.bt_start.setText("重新下载");
                holder.bt_start.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if ( startListener != null)
                        {
//                            ToastUtil.toast("点到开始");
                            startListener.start(position, e.cityID);
                        }
                    }
                });
                break;
        }
        // 按钮隐藏显示切换
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int status = holder.ll_bottom.getVisibility();
                if (status == View.VISIBLE)
                {
                    holder.ll_bottom.setVisibility(View.GONE);
                    holder.iv_pic.setImageResource(R.mipmap.ic_more);
                } else
                {
                    holder.ll_bottom.setVisibility(View.VISIBLE);
                    holder.iv_pic.setImageResource(R.mipmap.calendar);
                }
            }
        });
        // 删除
        holder.bt_del.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( delListener != null)
                {
                    delListener.del(e.cityID, position);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list == null ? 0 : list.size();
    }


}
