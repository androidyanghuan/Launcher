package cn.sn.zwcx.welcome.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.welcome.R;
import cn.sn.zwcx.welcome.adapters.HomeAdapter;
import cn.sn.zwcx.welcome.app.App;
import cn.sn.zwcx.welcome.bean.Application;
import cn.sn.zwcx.welcome.widgets.DividerGridItemDecoration;
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

    /** 滚动的宽度 */
    private int totalWidth;

    /** 当前选中的提示 */
    private TextView selectedHint;

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
        selectedHint = findViewById(R.id.apps_selected_txt);

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);

        appsView.setItemAnimator(new DefaultItemAnimator());
     //   appsView.addItemDecoration(new SpaceItemDecoration(10));
     //   DividerItemDecoration did = new DividerItemDecoration(mContext,DividerItemDecoration.HORIZONTAL);
     //   did.setDrawable(getDrawable(R.drawable.custom_divider_line));
     //   appsView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.HORIZONTAL));
     //   appsView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        appsView.addItemDecoration(new DividerGridItemDecoration(mContext));
        appsView.setHasFixedSize(true);
     //   appsView.addItemDecoration(did);
        mAppsAdapter = new HomeAdapter(mContext,mApps,selectedHint);
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

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;
        private Paint mPaint;

        SpaceItemDecoration(int space) {
            this.space = space;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(getResources().getColor(android.R.color.darker_gray));
            mPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            int topBottom = 10,leftRight = 10;
            //判断总的数量是否可以整除
            int totalCount = layoutManager.getItemCount();
            int surplusCount = totalCount % layoutManager.getSpanCount();
            int childPosition = parent.getChildAdapterPosition(view);
            if (layoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                //竖直方向的
                if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                    //后面几项需要bottom
                    outRect.bottom = topBottom;
                } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                    outRect.bottom = topBottom;
                }
                if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {
                    //被整除的需要右边
                    outRect.right = leftRight;
                }
                outRect.top = topBottom;
                outRect.left = leftRight;
            } else {
                if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                    //后面几项需要右边
                    outRect.right = leftRight;
                } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                    outRect.right = leftRight;
                }
                if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {
                    //被整除的需要下边
                    outRect.bottom = topBottom;
                }
                outRect.top = topBottom;
                outRect.left = leftRight;
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int left = parent.getPaddingLeft();
            int right = parent.getMeasuredWidth() - left;
            int top,bottom;
            int vtop = parent.getPaddingTop();
            int vbottom = parent.getMeasuredHeight() - vtop;
            int vright,vleft;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++){
                View childAt = parent.getChildAt(i);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) parent.getLayoutParams();
                top = childAt.getBottom() + params.bottomMargin + 13;
                bottom = top + 10;
                c.drawRect(left,top,right,bottom,mPaint);
                Log.e(TAG,"left:" + left + " right:" + right + " top:" + top + " bottom:" + bottom);
            }


            for (int i = 0; i < count; i++){
                View childAt = parent.getChildAt(i);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) parent.getLayoutParams();
                vleft = childAt.getRight() + params.rightMargin - 18;
                vright = vleft + 10;
                c.drawRect(vleft,vtop,vright,vbottom,mPaint);
            }

        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }
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
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mAppsAdapter.notifyDataSetChanged();
        }
    }

}
