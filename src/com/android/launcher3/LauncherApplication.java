/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.launcher3.MTKUnreadLoader;
import com.mediatek.launcher3.ext.LauncherLog;
import com.mediatek.common.featureoption.FeatureOption;


public class LauncherApplication extends Application {
    private static final String TAG = "LauncherApplication";

    /// M: flag for starting Launcher from application
    private boolean mTotallyStart = false;

    /// M: added for unread feature.
    private MTKUnreadLoader mUnreadLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LauncherLog.DEBUG) {
            LauncherLog.d(TAG, "LauncherApplication onCreate");
        }

        LauncherAppState.setApplicationContext(this);
        LauncherAppState.getInstance().setLauncehrApplication(this);
        
        /**M: register unread broadcast.@{**/
        if (FeatureOption.MTK_LAUNCHER_UNREAD_SUPPORT) {
            mUnreadLoader = new MTKUnreadLoader(getApplicationContext());
            // Register unread change broadcast.
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.MTK_ACTION_UNREAD_CHANGED);
            registerReceiver(mUnreadLoader, filter);
        }
        /**@}**/
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        
        /**M: added for unread feature, unregister unread receiver.@{**/
        if (FeatureOption.MTK_LAUNCHER_UNREAD_SUPPORT) {
            unregisterReceiver(mUnreadLoader);
        }
        /**@}**/
        
        LauncherAppState.getInstance().onTerminate();
    }

    /// M: LauncherApplication start flag @{
    public void setTotalStartFlag() {
        mTotallyStart = true;
    }

    public void resetTotalStartFlag() {
        mTotallyStart = false;
    }

    public boolean isTotalStart() {
        return mTotallyStart;
    }
    /// M: }@
    
    /**M: Added for unread message feature.@{**/
    /**
     * M: Get unread loader, added for unread feature.
     */
    public MTKUnreadLoader getUnreadLoader() {
        return mUnreadLoader;
    }
    /**@}**/
}