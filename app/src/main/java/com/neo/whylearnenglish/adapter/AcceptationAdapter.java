package com.neo.whylearnenglish.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.bean.Letter;

import java.util.List;

/**
 * 词类意义adapter
 * Created by Neo on 2016/12/16.
 */

public class AcceptationAdapter extends RecyclerView.Adapter<AcceptationAdapter.BindingViewHolder>{

    private List<Letter.POS> list;

    public AcceptationAdapter(List<Letter.POS> list) {
        this.list = list;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_layout_acceptation,parent,false);
        BindingViewHolder holder = new BindingViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        Letter.POS pos = list.get(position);
        holder.getBinding().setVariable(BR.pos, pos);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BindingViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }
        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
