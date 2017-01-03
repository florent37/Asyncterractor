package com.github.florent37.asyncterractor;
import com.github.florent37.asyncterractor.annotations.Ignore;
import com.github.florent37.asyncterractor.annotations.OnThread;


public interface MainPresenter {

    void bind(MainView mainView);
    void undbind();
    void start();


}
