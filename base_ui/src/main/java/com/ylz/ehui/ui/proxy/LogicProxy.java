package com.ylz.ehui.ui.proxy;

import com.ylz.ehui.ui.mvp.presenter.BasePresenter;
import com.ylz.ehui.ui.mvp.view.BaseView;
import com.ylz.ehui.ui.annotation.Implement;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class LogicProxy {
    private static final LogicProxy mInstance = new LogicProxy();

    public static LogicProxy getInstance() {
        return mInstance;
    }

    private LogicProxy() {
        mImplements = new HashMap<>();
    }

    private Map<Class, Object> mImplements;

    public void init(Class... clss) {
        for (Class cls : clss) {
            if (cls.isAnnotationPresent(Implement.class)) {
                for (Annotation ann : cls.getDeclaredAnnotations()) {
                    if (ann instanceof Implement) {
                        try {
                            mImplements.put(cls, ((Implement) ann).value().newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 初始化presenter add map
    public <T> T bind(Class clzz, BaseView var1) {
        if (!mImplements.containsKey(clzz)) {
            init(clzz);
        }
        BasePresenter presenter = ((BasePresenter) mImplements.get(clzz));
        if (var1 != presenter.getView()) {
            if (presenter.getView() != null) {
                presenter.detachView();
            }
            presenter.attachView(var1);
        }
        return (T) presenter;
    }

    // 解除绑定 移除map
    public void unbind(Class clzz, BaseView var1) {
        if (mImplements.containsKey(clzz)) {
            BasePresenter presenter = ((BasePresenter) mImplements.get(clzz));
            if (var1 != presenter.getView()) {
                if (presenter.getView() != null)
                    presenter.detachView();
                mImplements.remove(clzz);
            }

        }
    }
}