package cn.sn.zwcx.mvvm.adapters;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.io.Serializable;
import java.util.List;

import cn.sn.zwcx.mvvm.bean.gankio.GankIoItemBean;
import cn.sn.zwcx.mvvm.utils.ACache;

/**
 * Created by on 2018/5/4 9:01.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback {
    private final String TAG = RecycleItemTouchHelper.class.getSimpleName();

    private ItemTouchHelperCallback callback;

    public RecycleItemTouchHelper(ItemTouchHelperCallback itemTouchHelperCallback) {
        this.callback = itemTouchHelperCallback;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager)
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        else
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 得到拖动ViewHolder的position
        int fromPosition = viewHolder.getAdapterPosition();
        // 得到目标ViewHolder的position
        int toPosition = target.getAdapterPosition();
        callback.onMove(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        callback.onItemDelete(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
        List<GankIoItemBean> datas = (List<GankIoItemBean>) callback.getData();
        // 这里保存下拖拽后的状态
        //存储
        ACache.get(recyclerView.getContext()).put("items", (Serializable) datas,ACache.TIME_DAY * 7);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        //   return super.isLongPressDragEnabled();
        // 如果需要定义哪个item不可以拖拽需重写该函数
        return false;
    }

    public interface ItemTouchHelperCallback{
        void onItemDelete(int positon);
        void onMove(int fromPosition,int toPosition);
        List<?> getData();
    }

}
