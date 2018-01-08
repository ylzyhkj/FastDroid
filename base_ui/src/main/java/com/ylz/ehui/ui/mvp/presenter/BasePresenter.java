package com.ylz.ehui.ui.mvp.presenter;

import com.ylz.ehui.ui.mvp.view.BaseView;

public class BasePresenter<T extends BaseView> implements Presenter<T> {

    private T mView;

    @Override
    public void attachView(T mvpView) {
        this.mView = mvpView;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    public boolean isViewBind() {
        return mView != null;
    }


    public T getView() {
        return mView;
    }

}
