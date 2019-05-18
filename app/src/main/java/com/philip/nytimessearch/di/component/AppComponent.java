package com.philip.nytimessearch.di.component;

import com.philip.nytimessearch.NYTimesSearchApplication;
import com.philip.nytimessearch.di.module.AndroidInjectBuilder;
import com.philip.nytimessearch.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidInjectBuilder.class,
        AndroidSupportInjectionModule.class, AppModule.class})
public interface AppComponent extends AndroidInjector<NYTimesSearchApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(NYTimesSearchApplication application);

        AppComponent build();
    }
}