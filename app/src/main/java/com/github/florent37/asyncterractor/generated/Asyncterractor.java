package com.github.florent37.asyncterractor.generated;

import com.github.florent37.asyncterractor.MainActivity;
import com.github.florent37.asyncterractor.MainPresenter;
import com.github.florent37.asyncterractor.MainPresenterImpl;
import com.github.florent37.asyncterractor.MainView;

public class Asyncterractor {
    public static MainPresenter of(MainPresenterImpl mainPresenter) {
        return new Asyncterractor$MainPresenter(mainPresenter);
    }

    public static MainView of(MainView mainView) {
        return new Asyncterractor$MainView(mainView);
    }
}
