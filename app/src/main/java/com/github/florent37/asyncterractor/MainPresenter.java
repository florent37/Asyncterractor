package com.github.florent37.asyncterractor;

import com.github.florent37.asyncterractor.generated.OnThread;

@OnThread
public interface MainPresenter {

    void bind(MainView mainView);
    void undbind();
    void start();


}
