package cn.sn.zwcx.welcome.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.widget.RecyclerViewParallax;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.welcome.R;
import cn.sn.zwcx.welcome.adapters.HomeAdapter;
import cn.sn.zwcx.welcome.app.App;
import cn.sn.zwcx.welcome.bean.Application;
import cn.sn.zwcx.welcome.widgets.TVRecyclerView;

public class AppsActivity extends Activity implements View.OnClickListener {
    private final String TAG = AppsActivity.class.getSimpleName();

    /** 展示所有已安装的第三方应用 */
    private TVRecyclerView appsView;

    /** 存放第三方应用的List */
    private List<Application> mApps;

    /** 展示Apps的适配器 */
    private HomeAdapter mAppsAdapter;

    /** 本应用的上下文 */
    private Context mContext;

    /** RecycleView布局管理器 */
    private StaggeredGridLayoutManager gridLayoutManager;

    /** 左右翻页按钮 */
    private Button mLeftBtn,mRightBtn;

    private int totalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        new GetAllAppTask().execute();
        initViews();

    }

    /** 初始化视图 */
    private void initViews() {
        totalWidth = getResources().getDimensionPixelSize(R.dimen.total_width);
        mApps = new ArrayList<>();
        mContext = App.me.getContext();
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        gridLayoutManager.setAutoMeasureEnabled(true);

        appsView = findViewById(R.id.apps_tvrv);
        mLeftBtn = findViewById(R.id.apps_left_btn);
        mRightBtn = findViewById(R.id.apps_right_btn);

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);

        appsView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.apps_left_btn:
                if (appsView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                    appsView.smoothScrollBy(-totalWidth,0);
                break;

            case R.id.apps_right_btn:
                if (appsView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                    appsView.smoothScrollBy(totalWidth,0);
                break;
        }
    }

    /** 获取所有已安装的应用 */
    private class GetAllAppTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
            int size = resolveInfos.size();
            for (int i = 0; i < size; i++){
                ResolveInfo resolveInfo = resolveInfos.get(i);
                Application app = new Application();
                app.icon = resolveInfo.loadIcon(pm);
                app.name = resolveInfo.loadLabel(pm).toString();
                app.packageNema = resolveInfo.activityInfo.packageName;
                app.className = resolveInfo.activityInfo.name;
                mApps.add(app);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAppsAdapter = new HomeAdapter(mContext,mApps);
            appsView.setLayoutManager(gridLayoutManager);

           appsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
               @Override
               public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                   super.onScrollStateChanged(recyclerView, newState);
               }

               @Override
               public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                   super.onScrolled(recyclerView, dx, dy);
                   if (appsView.isFirstItemVisible())
                       mLeftBtn.setVisibility(View.INVISIBLE);
                   else mLeftBtn.setVisibility(View.VISIBLE);
                   if (appsView.isLastItemVisible(3,mApps.size()))
                       mRightBtn.setVisibility(View.INVISIBLE);
                   else mRightBtn.setVisibility(View.VISIBLE);
               }
           });

           mAppsAdapter.setOnItemClickListener(new TVRecyclerView.CustomAdapter.OnItemClickListener() {
               @Override
               public void onItemClick(View view, int position) {
                   String packageNema = mApps.get(position).packageNema;
                   startActivity(getPackageManager().getLaunchIntentForPackage(packageNema));
               }

               @Override
               public void onItemLongClick(View view, int position) {

               }
           });
            appsView.setAdapter(mAppsAdapter);

        }
    }

}
