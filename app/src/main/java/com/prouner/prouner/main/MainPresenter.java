package com.prouner.prouner.main;

import com.prouner.prouner.R;
import com.prouner.prouner.model.Question;

import java.util.ArrayList;
import java.util.Random;

public class MainPresenter implements MainMVP.Presenter {

    private MainMVP.View mView;
    private MainMVP.Model mModel;

    private String mCorrectOptionText;
    private boolean isTestRunning = false;
    private int midSound;
    private Question mQuestion;

    public MainPresenter() {
        mModel = new MainModel();
    }

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
            //Get the options
            mQuestion = mModel.getQuestionAttributes();
            mView.playSound(mQuestion.getIdAudioResource());
        } else{
            mView.playSound(mQuestion.getIdAudioResource());
        }

    }

    @Override
    public void viewOnSoundPlayingCompleted() {

        //Verifies if the user has just started a new test, if so, the presenter must get the options and send to the view show them, if not, nothing has to be done
        if(!isTestRunning){
            mView.showAlternatives(mQuestion);
            isTestRunning = true;
        }
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

//    private String[] getOptions(){
//
//        //Defines which option number will contain the correct answer
//        Random random = new Random();
//        int indexCorrectOption = random.nextInt(4);
//
//
//        //Manipulate the String array in order to make the correct answer stay in the correct array position
//        String aux = mOptionsText[indexCorrectOption]; // Save the string which is currently in the correct position
//        mOptionsText[indexCorrectOption] = mOptionsText[0]; // Put in the correct position, the correct answer (text), the correct answer is always in the position 0
//        mOptionsText[0] = aux; // Put in the position 0 the answer that previously was in the correct position
//
//        //Initialize the member variable with the text of the correct answer, this will be used later to verify whether the user has answered correctly
//        mCorrectOptionText = mOptionsText[indexCorrectOption];
//
//        return mOptionsText;
//    }

    private boolean isOptionClickedCorrect(String userAnswer){
        if(mQuestion.isAnswerCorrect(userAnswer)){
            return true;
        } else{
            return false;
        }
    }
}
