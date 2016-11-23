package com.github.florent37.asyncterractor;

import com.github.florent37.asyncterractor.annotations.OnUiThread;

@OnUiThread
public interface MainView {
    void displayUserName(String userName);
}
