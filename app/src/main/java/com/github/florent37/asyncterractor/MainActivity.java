package com.github.florent37.asyncterractor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = Asyncterractor.of(new MainPresenterImpl());
        mainPresenter.bind(this);

        mainPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.undbind();
    }

    @Override
    public void displayUserName(String userName) {
        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMessage(String message) {

    }
}
