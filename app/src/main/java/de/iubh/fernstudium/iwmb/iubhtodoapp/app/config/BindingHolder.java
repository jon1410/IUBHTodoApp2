package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config;


import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ivanj on 17.03.2018.
 */

public class BindingHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final B binding;

    public BindingHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public B getBinding() {
        return binding;
    }
}
