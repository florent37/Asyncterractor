package com.github.florent37.asyncterractor;

import com.github.florent37.asyncterractor.annotations.Ignore;
import com.github.florent37.asyncterractor.annotations.OnThread;

@OnThread
public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    @Override
    public void bind(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    @Ignore
    public void undbind() {
        this.mainView = null;
    }

    @Override
    public void start() {
        this.mainView.displayUserName("florent");
    }

}
