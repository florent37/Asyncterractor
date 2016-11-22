package com.github.florent37.asyncterractor.generated;

import android.os.Handler;
import android.os.Looper;

import com.github.florent37.asyncterractor.MainPresenter;
import com.github.florent37.asyncterractor.MainView;

import java.lang.ref.WeakReference;

public class Asyncterractor$MainPresenter implements MainPresenter{

    private final WeakReference<MainPresenter> reference;
    private final Handler handler;

    public Asyncterractor$MainPresenter(MainPresenter mainPresenter) {
        reference = new WeakReference<>(mainPresenter);
        handler = new Handler();
    }

    public void bind(final MainView mainView) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                final MainPresenter mainPresenter = reference.get();
                if (mainPresenter != null) {
                    final MainView view = Asyncterractor.of(mainView); //because MainView contains @OnUiThread
                    mainPresenter.bind(view);
                }
            }
        });
    }

    public void undbind() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                final MainPresenter mainPresenter = reference.get();
                if (mainPresenter != null) {
                    mainPresenter.undbind();
                }
            }
        });
    }

    public void start() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                final MainPresenter mainPresenter = reference.get();
                if (mainPresenter != null) {
                    mainPresenter.start();
                }
            }
        });
    }

}
