package com.ylz.ehui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.SimpleArrayMap;

import java.util.Map;

/**
 * Author: yms
 * Date: 2017/8/24 11:34
 * Desc:
 */
public class SPUtils {
    private static final String DEFAULT_SP_NAME = "default_sputils";
    private static final SimpleArrayMap<String, SPUtils> spMap = new SimpleArrayMap<>();

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean isApplyMethodExist;

    private SPUtils(final String spName) {
        sp = Utils.getApp().getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sp.edit();
        isApplyMethodExist = isApplyMethodExist();
    }

    public static SPUtils getInstance(String spName) {
        if (StringUtils.isSpace(spName)) {
            spName = DEFAULT_SP_NAME;
        }

        SPUtils spUtils = spMap.get(spName);

        if (spUtils == null) {
            synchronized (spMap) {
                spUtils = new SPUtils(spName);
                spMap.put(spName, spUtils);
            }
        }

        return spUtils;
    }

    public static SPUtils getInstance() {
        return getInstance("");
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, String.valueOf(object));
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public <T extends Object> T get(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            return (T) sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(sp.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(sp.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(sp.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(sp.getLong(key, (Long) defaultValue));
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        apply();
    }

    /**
     * 清除所有数据
     */
    public void clear(Context context) {
        editor.clear();
        apply();
    }

    /**
     * 查询某个key是否已经存在
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 如果找到则使用apply执行，否则使用commit
     */
    private void apply() {
        if (isApplyMethodExist) {
            // 异步写入
            editor.apply();
            return;
        }

        //同步写入
        editor.commit();
    }

    private boolean isApplyMethodExist() {
        try {
            SharedPreferences.Editor.class.getMethod("apply");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
