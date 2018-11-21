package com.prouner.prouner.main;

import android.content.Context;
import android.media.MediaPlayer;

import com.prouner.prouner.model.Question;

public interface MainMVP {

    interface View {
        void playSound(int idSound);
        void showAlternatives(Question question);
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

        Question getQuestionAttributes();

    }

}
