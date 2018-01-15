package com.ylz.ehui.ui.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.ylz.ehui.ui.mvp.presenter.BasePresenter;
import com.ylz.ehui.ui.proxy.LogicProxy;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public abstract class BaseActivity extends RxAppCompatActivity implements BaseView {

    protected BasePresenter mPresenter;
    private Unbinder bind;

    protected abstract int getLayoutResource();

    protected abstract void onInitialization(Bundle bundle);

    protected Class getLogicClazz() {
        return null;
    }

    protected void onInitData2Remote() {
        if (getLogicClazz() != null)
            mPresenter = getLogicImpl();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (getLayoutResource() == 0) {
            throw new RuntimeException("getLayoutResource()需要返回有效的layout id");
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResource());
        bind = ButterKnife.bind(this);
        this.onInitialization(savedInstanceState);
        this.onInitData2Remote();
    }

    //获得该页面的实例
    public <T> T getLogicImpl() {
        return LogicProxy.getInstance().bind(getLogicClazz(), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null && !mPresenter.isViewBind()) {
            LogicProxy.getInstance().bind(getLogicClazz(), this);
        }
    }

    protected <T> void bindToLifecycle(Observable<T> observable) {
        observable.compose(this.<T>bindToLifecycle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }

        LogicProxy.getInstance().unbind(getLogicClazz(), this);
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
