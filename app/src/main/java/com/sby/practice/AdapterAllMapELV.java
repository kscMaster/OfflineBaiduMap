package com.sby.practice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.sby.practice.utils.FormatUtils;

import java.util.ArrayList;

import static com.sby.practice.R.id.iv_download;

/**
 * Created by kowal on 2017/4/26.
 */

public class AdapterAllMapELV extends BaseExpandableListAdapter
{

    private ArrayList<MKOLSearchRecord> allCitys;
    private Context mContext;
    private ArrayList<MKOLUpdateElement> localElement;

    public AdapterAllMapELV(Context context, ArrayList<MKOLSearchRecord> list, ArrayList<MKOLUpdateElement> elements)
    {
        this.mContext = context;
        this.allCitys = list;
        this.localElement = elements;
    }

    @Override
    public int getGroupCount()
    {
        return allCitys.size();
    }


    @Override
    public Object getGroup(int groupPosition)
    {
        return allCitys.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View view = null;
        GroupHolder groupholder = null;
        if (convertView != null)
        {
            view = convertView;
            groupholder = (GroupHolder) view.getTag();
        } else
        {
            view = View.inflate(mContext, R.layout.text_item_map_parent, null);
            groupholder = new GroupHolder();
            groupholder.tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
            groupholder.tv_downAll = (TextView) view.findViewById(R.id.tv_downAll);
            groupholder.tv_size = (TextView) view.findViewById(R.id.tv_size);
            groupholder.iv_status = (ImageView) view.findViewById(R.id.iv_status);
            view.setTag(groupholder);
        }
        if (allCitys.get(groupPosition).cityType == 1)
        {
            groupholder.iv_status.setVisibility(View.VISIBLE);
        } else
        {
            groupholder.iv_status.setVisibility(View.INVISIBLE);
        }
        groupholder.tv_city_name.setText(allCitys.get(groupPosition).cityName);
        groupholder.tv_size.setText(FormatUtils.formatDataSize(allCitys.get(groupPosition).size));

        // 如果已经下载，按钮不可点击
        if (localElement != null)
        {
            for (int i = 0; i < localElement.size(); i++)
            {
                if (localElement.get(i).cityID == allCitys.get(groupPosition).cityID)
                {
                    groupholder.tv_downAll.setClickable(false);
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.downloaded);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    groupholder.tv_downAll.setCompoundDrawables(drawable, null, null, null);
                }
            }
        }
        final GroupHolder finalGroupholder = groupholder;
        groupholder.tv_downAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != groupListener)
                {
                    groupListener.groupDown(v, groupPosition, allCitys.get(groupPosition));
                    finalGroupholder.tv_downAll.setClickable(false);
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.downloaded);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    finalGroupholder.tv_downAll.setCompoundDrawables(drawable, null, null, null);
                }
            }
        });
        return view;
    }

    private OnGroupDown groupListener = null;

    public interface OnGroupDown
    {
        void groupDown(View view, int groupPosition, MKOLSearchRecord msr);
    }

    public void setOnGroupDown(OnGroupDown onGroupDown)
    {
        this.groupListener = onGroupDown;
    }

    class GroupHolder
    {
        TextView tv_city_name;
        TextView tv_downAll;
        TextView tv_size;
        ImageView iv_status;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return allCitys.get(groupPosition).childCities == null ? 0 : allCitys.get(groupPosition).childCities.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        View view = null;
        ChildHolder childholder = null;
        if (convertView != null)
        {
            view = convertView;
            childholder = (ChildHolder) view.getTag();
        } else
        {
            view = View.inflate(mContext, R.layout.text_item_map_child, null);
            childholder = new ChildHolder();
            childholder.tv_city = (TextView) view.findViewById(R.id.tv_city);
            childholder.tv_status = (TextView) view.findViewById(R.id.tv_status);
            childholder.tv_size = (TextView) view.findViewById(R.id.tv_size);
            childholder.iv_download = (ImageView) view.findViewById(iv_download);
            view.setTag(childholder);
        }
        childholder.tv_city.setText(allCitys.get(groupPosition).childCities.get(childPosition).cityName);
        childholder.tv_size.setText(FormatUtils.formatDataSize(allCitys.get(groupPosition).childCities.get(childPosition).size));

        // 如果已经下载，按钮不可点击
        if (localElement != null)
        {
            for (int i = 0; i < localElement.size(); i++)
            {
                if (localElement.get(i).cityID == allCitys.get(groupPosition).cityID)
                {
                    childholder.iv_download.setClickable(false);
                    childholder.iv_download.setImageResource(R.drawable.downloaded);
                }
            }
        }
        final ChildHolder finalChildholder = childholder;
        childholder.iv_download.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != childListener)
                {
                    childListener.childDown(v, groupPosition, childPosition, allCitys.get(groupPosition).childCities.get(childPosition));
                    finalChildholder.iv_download.setImageResource(R.drawable.downloaded);
                }
            }
        });

        return view;
    }

    class ChildHolder
    {
        TextView tv_city;
        TextView tv_status;
        TextView tv_size;
        ImageView iv_download;
    }

    private OnChildDown childListener = null;

    public interface OnChildDown
    {
        void childDown(View view, int groupPosition, int childPosition, MKOLSearchRecord msr);
    }

    public void setOnClildDown(OnChildDown onChildDown)
    {
        this.childListener = onChildDown;
    }


    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
