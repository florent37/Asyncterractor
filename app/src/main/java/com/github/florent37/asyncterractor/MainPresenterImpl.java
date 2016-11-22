package com.github.florent37.asyncterractor;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    @Override
    public void bind(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void undbind() {
        this.mainView = null;
    }

    @Override
    public void start() {
        this.mainView.displayUserName("florent");
    }

}
