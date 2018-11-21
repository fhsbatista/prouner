package com.prouner.prouner.main;

import android.content.Context;
import android.media.MediaPlayer;

public interface MainMVP {

    interface View {
        void playSound(int idSound);
        void showAlternatives(String... options);
        Context getContext();
        void showCorrectOptionMessage();
        void newTest();

    }

    interface Presenter{
        void setView(View view);
        void viewOnPlayButtonClicked();
        void viewOnSoundPlayingCompleted();
        void viewOnOptionClicked(String optionText);
    }

    interface Model {

    }

}
