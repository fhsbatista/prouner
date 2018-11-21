package com.prouner.prouner.main;

public interface MainMVP {

    interface View {
//        void playSound();
//        void showAlternatives();
    }

    interface Presenter{
        void setView(View view);
        void viewOnButtonClicked();
    }

    interface Model {

    }

}
