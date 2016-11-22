package com.github.florent37.asyncterractor.generated;

import android.os.Handler;
import android.os.Looper;

import com.github.florent37.asyncterractor.MainPresenter;
import com.github.florent37.asyncterractor.MainView;

import java.lang.ref.WeakReference;

public class Asyncterractor$MainView implements MainView {

    private final WeakReference<MainView> reference;
    private final Handler handler;

    public Asyncterractor$MainView(MainView mainView) {
        reference = new WeakReference<>(mainView);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void displayUserName(final String userName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                final MainView mainPresenter = reference.get();
                if (mainPresenter != null) {
                    mainPresenter.displayUserName(userName);
                }
            }
        });
    }
}
