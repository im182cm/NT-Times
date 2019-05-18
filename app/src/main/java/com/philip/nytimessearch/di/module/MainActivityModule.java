package com.philip.nytimessearch.di.module;

import com.philip.nytimessearch.mvvm.view.ListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract ListFragment bindListFragment();
}
