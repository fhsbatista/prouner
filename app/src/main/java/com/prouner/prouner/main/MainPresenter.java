package com.prouner.prouner.main;

import android.media.MediaPlayer;

import com.prouner.prouner.R;

public class MainPresenter implements MainMVP.Presenter {

    private MainMVP.View mView;
    private String mCorrectOptionText;
    private boolean isTestRunning = false;
    private int idSound;

    @Override
    public void setView(MainMVP.View view) {
        mView = view;
        mView.newTest();
        isTestRunning = false;
    }

    @Override
    public void viewOnPlayButtonClicked() {

        //Verifies if the click happend when the question hasn't been finished, if so, a new sound can't be provided, the current sound needs to be played instead
        if(!isTestRunning){
            idSound = R.raw.pronounciation;
            mView.playSound(idSound);
        } else{
            mView.playSound(idSound);
        }

    }

    @Override
    public void viewOnSoundPlayingCompleted() {

        mView.showAlternatives(getOptions());
    }

    @Override
    public void viewOnOptionClicked(String optionText) {
        if(isOptionClickedCorrect(optionText)){
            mView.showCorrectOptionMessage();
            startNewTest();
        }
    }

    private void startNewTest(){
        mView.newTest();
        isTestRunning = false;
    }

    private String[] getOptions(){
        String[] options = {"opçao 1", "opçao 2", "opçao 3", "opçao 4"};
        mCorrectOptionText = options[0];

        return options;
    }

    private boolean isOptionClickedCorrect(String optionText){
        if(optionText.equals(mCorrectOptionText)){
            return true;
        } else{
            return false;
        }
    }
}
