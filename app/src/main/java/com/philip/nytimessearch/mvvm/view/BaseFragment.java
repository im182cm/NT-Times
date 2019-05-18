package com.philip.nytimessearch.mvvm.view;

import android.os.Bundle;

import com.philip.nytimessearch.mvvm.viewmodel.ViewModelUtil;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

/**
 * BaseFragment to inject ViewModel and not to call ViewModelProviders many times.
 */
public class BaseFragment<T extends ViewModel> extends DaggerFragment {
    @Inject
    T viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider.Factory viewModelFactory = ViewModelUtil.createFor(viewModel);
        ViewModelProviders.of(this, viewModelFactory).get(viewModel.getClass());
    }
}
