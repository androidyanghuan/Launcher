package cn.sn.zwcx.mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.bean.douban.child.Subjects;
import cn.sn.zwcx.mvvm.databinding.FragmentTopMovieItemBinding;

/**
 * Created by on 2018/4/28 8:16.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class TopMovieAdapter extends RecyclerView.Adapter<TopMovieAdapter.TopMovieViewHolder> {

    private List<Subjects> subjects;

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public TopMovieAdapter(List<Subjects> subjects) {
        this.subjects = subjects;
    }

    @Override
    public TopMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentTopMovieItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.fragment_top_movie_item,parent,false);
        return new TopMovieViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(TopMovieViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setVariable(BR.top250Item,subjects.get(position));
        binding.executePendingBindings();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClickListener(v,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return subjects == null ? 0 : subjects.size();
    }

    public class TopMovieViewHolder extends RecyclerView.ViewHolder {
        public TopMovieViewHolder(View itemView) {
            super(itemView);
        }
    }

}
