package com.philip.nytimessearch.di.module;

import com.philip.nytimessearch.mvvm.view.DetailActivity;
import com.philip.nytimessearch.mvvm.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AndroidInjectBuilder {
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity bindDetailActivity();
}
