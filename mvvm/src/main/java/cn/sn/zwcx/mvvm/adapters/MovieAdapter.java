package cn.sn.zwcx.mvvm.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.animation.ScaleInAnimation;

import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.bean.douban.child.Subjects;

/**
 * Created by on 2018/4/13 16:01.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class MovieAdapter extends BaseQuickAdapter<Subjects,BaseViewHolder> {


    public MovieAdapter(int layoutResId, @Nullable List<Subjects> data) {
        super(layoutResId, data);
        init();
    }

    @Override
    protected void convert(BaseViewHolder helper, Subjects item) {
        helper.setText(R.id.fragment_movie_item_title,item.getTitle())
                .setText(R.id.fragment_movie_item_director,item.getDirectors().get(0).getName())
                .setText(R.id.fragment_movie_item_protagonist,item.getProtagonist())
                .setText(R.id.fragment_movie_item_type, item.getGenre())
                .setText(R.id.fragment_movie_item_rating,item.getRatingStr());
        Glide.with(mContext)
                .load(item.getImages().getMedium())
                .crossFade(300)
                .placeholder(R.drawable.img_default_movie)
                .into((ImageView) helper.getView(R.id.fragment_movie_item_icon));
    }

    private void init() {
        openLoadAnimation(new ScaleInAnimation(0.8f));
        isFirstOnly(false);
    }

}
