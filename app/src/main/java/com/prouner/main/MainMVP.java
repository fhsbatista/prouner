package com.prouner.main;

import android.content.Context;

import com.prouner.model.Question;

public interface MainMVP {

    interface View {
        void playSound(byte[] audio);
        void showAlternatives(Question question);
        Context getContext();
        void showCorrectOptionMessage();
        void showErrorWhenDownloadingQuestionsMessage();
        void newTest();
        void showLoadingContent();
        void hideLoadingContent();


    }

    interface Presenter{
        void setView(View view);
        void viewOnPlayButtonClicked();
        void viewOnSoundPlayingCompleted();
        void viewOnOptionClicked(String optionText);
    }

    interface Model {

        // TODO below I'm using a class which implements the Model interface, is it a good practice?
        void setOnQuestionRequestListener(MainModel.OnQuestionRequestListener onQuestionRequestListener);
        void getQuestionAttributesArray();

    }

}
