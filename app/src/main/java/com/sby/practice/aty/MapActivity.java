package com.sby.practice.aty;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.sby.practice.AdapterAllMapELV;
import com.sby.practice.AdapterZhiXia;
import com.sby.practice.Adapterloaded;
import com.sby.practice.Adapterloading;
import com.sby.practice.CustomExpandableListView;
import com.sby.practice.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends AppCompatActivity implements MKOfflineMapListener
{
    @BindView(R.id.bt_download)
    Button bt_download; // 下载管理
    @BindView(R.id.bt_city)
    Button bt_city; //城市列表
    @BindView(R.id.ll_loading)
    LinearLayout ll_loading;
    @BindView(R.id.ll_list)
    LinearLayout ll_list;
    @BindView(R.id.rv_downloading)
    RecyclerView rv_downloading;
    @BindView(R.id.rv_complate)
    RecyclerView rv_complate;
    @BindView(R.id.rv_zhixia)
    RecyclerView rv_zhixia;
    @BindView(R.id.rv_cites)
    CustomExpandableListView rv_cites;

    Activity activity = MapActivity.this;

    private MKOfflineMap mOffline = null;
    /**
     * 城市列表
     */
    private AdapterZhiXia zXAdapter; // 直辖
    private ArrayList<MKOLSearchRecord> zhiXiaCitys; // 直辖市
    private AdapterAllMapELV allAdapter; // 所有城市
    private ArrayList<MKOLSearchRecord> citys; // 小城市
    /**
     * 下载管理
     */
    private Adapterloaded loadedAdapter; // 已下载
    private Adapterloading loadingAdapter; // 正在下载
    private MKOLUpdateElement update;
    private ArrayList<MKOLUpdateElement> loadMapList;
    private ArrayList<MKOLUpdateElement> loadingMapList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        initWidget();
        setLoading();
        setAction();
    }

    /**
     * 所有城市列表、下载管理列表控件的初始化
     */
    private void initWidget()
    {
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        // 下载管理列表
        rv_downloading.setLayoutManager(new LinearLayoutManager(activity));
        rv_downloading.setNestedScrollingEnabled(false);
        rv_complate.setLayoutManager(new LinearLayoutManager(activity));
        rv_complate.setNestedScrollingEnabled(false);
        // 获取所有本地的离线地图
        ArrayList<MKOLUpdateElement> localElement = mOffline.getAllUpdateInfo();
        // 城市列表
        ArrayList<MKOLSearchRecord> allCitys = mOffline.getOfflineCityList(); // 所有城市
        zhiXiaCitys = new ArrayList<>(); // 直辖市
        citys = new ArrayList<>(); // 省
        if (allCitys != null)
        {
            for (MKOLSearchRecord mr : allCitys)
            {
                if (mr.cityType == 1) // 0:全国；1：省份；2：城市,如果是省份，可以通过childCities得到子城市列表
                {
                    citys.add(mr);
                } else
                {
                    zhiXiaCitys.add(mr);
                }
            }
            rv_cites.setGroupIndicator(null);
            allAdapter = new AdapterAllMapELV(activity, citys, localElement);
            rv_cites.setAdapter(allAdapter);
            Drawable drawable = getResources().getDrawable(R.color.gray);
            rv_cites.setChildDivider(drawable);

            rv_zhixia.setLayoutManager(new LinearLayoutManager(activity));
            rv_zhixia.setNestedScrollingEnabled(false);
            zXAdapter = new AdapterZhiXia(activity, zhiXiaCitys, localElement);
            rv_zhixia.setAdapter(zXAdapter);
        }
    }

    /**
     * 下载中和已下载的赋值
     */
    public void setLoading()
    {
        ArrayList<MKOLUpdateElement> AllMapList = mOffline.getAllUpdateInfo(); // 所有需要下载或更新的地图节点
        loadMapList = new ArrayList<>(); //已下载的离线地图信息列表
        loadingMapList = new ArrayList<>(); //正在下载的离线地图信息列表
        if (AllMapList != null)
        {
            for (int i = 0; i < AllMapList.size(); i++)
            {
                MKOLUpdateElement element = AllMapList.get(i);
                if (element.status == 4 || element.status == 10 || element.ratio == 100) // 已经下载完成
                {
                    loadMapList.add(element); // 未完成下载的
                } else
                {
                    loadingMapList.add(element);
                }
            }
            loadingAdapter = new Adapterloading(activity, loadingMapList);
            rv_downloading.setAdapter(loadingAdapter);
            loadingAdapter.notifyDataSetChanged();

            loadedAdapter = new Adapterloaded(activity, loadMapList);
            rv_complate.setAdapter(loadedAdapter);
            loadedAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 为适配器的按钮添加点击事件
     */
    private void setAction()
    {
        // 直辖市下载
        zXAdapter.setOnItemClickLitener(new AdapterZhiXia.OnClickListener()
        {
            @Override
            public void onClickDownload(View view, MKOLSearchRecord msr)
            {
                mOffline.start(msr.cityID);
//                ToastUtil.toast("已添加到下载任务" + msr.cityName);
                setLoading();
            }
        });
        // 父节点下载全部
        allAdapter.setOnGroupDown(new AdapterAllMapELV.OnGroupDown()
        {
            @Override
            public void groupDown(View view, int groupPosition, MKOLSearchRecord msr)
            {
                mOffline.start(msr.cityID);
//                ToastUtil.toast("已添加到下载任务" + msr.cityName);
                setLoading();
//                allAdapter.onGroupExpanded();
            }
        });
        // 子节点下载
        allAdapter.setOnClildDown(new AdapterAllMapELV.OnChildDown()
        {
            @Override
            public void childDown(View view, int groupPosition, int childPosition, MKOLSearchRecord msr)
            {
                mOffline.start(msr.cityID);
//                ToastUtil.toast("已添加到下载任务" + msr.cityName);
                setLoading();
            }
        });
//        // 删除
//        loadingAdapter.onDelClick(new Adapterloading.DelDownload()
//        {
//            @Override
//            public void del(int cityId, int position)
//            {
//                mOffline.remove(cityId);
//                setLoading();
//            }
//        });
//        // 开始
//        loadingAdapter.onStartClick(new Adapterloading.StartDownload()
//        {
//            @Override
//            public void start(int position, int cityId)
//            {
//                mOffline.start(cityId);
//                loadingAdapter.notifyItemChanged(position);
//            }
//        });
        // 暂停
//        loadingAdapter.onStopClick(new Adapterloading.StopDownload()
//        {
//            @Override
//            public void stop(int cityId, int position, boolean flag)
//            {
//                Log.e("pos：" + position, "-----------citiy:" + cityId);
//                mOffline.pause(cityId);
//                if (flag)
//                {
//                    loadingAdapter.notifyItemChanged(position);
//                }
//            }
//        });
    }

    /**
     * 下载管理与城市列表的切换
     *
     * @param view
     */
    @OnClick({R.id.bt_download, R.id.bt_city})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.bt_download:
                ll_list.setVisibility(View.GONE);
                ll_loading.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_city:
                ll_loading.setVisibility(View.GONE);
                ll_list.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onGetOfflineMapState(int type, int state)
    {
        switch (type)
        {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
            {
                loadingAdapter.notifyDataSetChanged();
                update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update.status == 10 || update.status == 4 || update.ratio == 100 || update.serversize == update.size) // 安装完成
                {
                    setLoading();
                } else
                {
                    loadingAdapter.onUpdateUi(type, update);
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mOffline.destroy();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}
