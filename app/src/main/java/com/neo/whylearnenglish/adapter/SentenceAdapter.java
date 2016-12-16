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
 * Created by Neo on 2016/12/16.
 */

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.BindingViewHolder> {
    private List<Letter.Sentence> list;
    public SentenceAdapter(List<Letter.Sentence> list) {
        this.list = list;
    }

    @Override
    public SentenceAdapter.BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_layout_sentence,parent,false);
        SentenceAdapter.BindingViewHolder holder = new SentenceAdapter.BindingViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(SentenceAdapter.BindingViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.sentence, list.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BindingViewHolder extends RecyclerView.ViewHolder  {
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
