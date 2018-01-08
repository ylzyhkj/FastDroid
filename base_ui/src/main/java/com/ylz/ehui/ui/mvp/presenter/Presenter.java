package com.ylz.ehui.ui.mvp.presenter;

public interface Presenter<V> {
    void attachView(V mvpView);
    void detachView();
}
