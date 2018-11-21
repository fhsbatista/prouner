package com.prouner.prouner.main;

public class MainPresenter implements MainMVP.Presenter {

    private MainMVP.View mView;

    @Override
    public void setView(MainMVP.View view) {
        mView = view;
    }

    @Override
    public void viewOnButtonClicked() {

    }
}
