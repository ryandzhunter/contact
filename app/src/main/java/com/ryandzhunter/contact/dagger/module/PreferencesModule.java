package com.ryandzhunter.contact.dagger.module;

import android.content.Context;

import com.ryandzhunter.contact.util.Preferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ryandzhunter on 8/2/17.
 */

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    public Preferences providePreferences(Context context){
        return new Preferences(context);
    }
}
