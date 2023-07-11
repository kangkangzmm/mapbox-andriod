/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
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
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.yinglan.scrolllayout.demo.farsunset.cim.sdk.android;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

class CIMCacheManager {

    public static final String KEY_UID = "KEY_UID";

    public static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";

    public static final String KEY_MANUAL_STOP = "KEY_MANUAL_STOP";

    public static final String KEY_CIM_DESTROYED = "KEY_CIM_DESTROYED";

    public static final String KEY_CIM_SERVER_HOST = "KEY_CIM_SERVER_HOST";

    public static final String KEY_CIM_SERVER_PORT = "KEY_CIM_SERVER_PORT";

    public static final String KEY_CIM_CONNECTION_STATE = "KEY_CIM_CONNECTION_STATE";


    public static final String KEY_NTC_CHANNEL_NAME = "KEY_NTC_CHANNEL_NAME";

    public static final String KEY_NTC_CHANNEL_MESSAGE = "KEY_NTC_CHANNEL_MESSAGE";

    public static final String KEY_NTC_CHANNEL_ICON = "KEY_NTC_CHANNEL_ICON";


    public static final String CONTENT_URI = "content://%s.cim.provider";

    static final String COLUMN_KEY  = "key";

    static final String COLUMN_VALUE  = "value";



    public static void remove(Context context, String key) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(Uri.parse(String.format(CONTENT_URI, context.getPackageName())), key, null);
    }

    public static void putString(Context context, String key, String value) {

        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_VALUE, value);
        resolver.insert(Uri.parse(String.format(CONTENT_URI, context.getPackageName())), values);

    }

    public static String getString(Context context, String key) {
        String value;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(String.format(CONTENT_URI, context.getPackageName())), new String[]{key}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(0);
        }else {
            value = null;
        }
        closeQuietly(cursor);
        return value;
    }

    private static void closeQuietly(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ignore) {
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        putString(context, key, Boolean.toString(value));
    }

    public static boolean getBoolean(Context context, String key) {
        String value = getString(context, key);
        return Boolean.parseBoolean(value);
    }

    public static void putInt(Context context, String key, int value) {
        putString(context, key, String.valueOf(value));
    }

    public static int getInt(Context context, String key) {
        String value = getString(context, key);
        return value == null ? 0 : Integer.parseInt(value);
    }
}
