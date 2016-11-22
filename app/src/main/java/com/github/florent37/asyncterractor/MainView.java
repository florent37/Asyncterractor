package com.github.florent37.asyncterractor;

import com.github.florent37.asyncterractor.generated.OnUiThread;

@OnUiThread
public interface MainView {
    void displayUserName(String userName);
}
