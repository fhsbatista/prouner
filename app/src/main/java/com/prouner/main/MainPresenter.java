package com.prouner.main;

import android.util.Log;

import com.prouner.model.Question;

import java.util.List;

public class MainPresenter implements MainMVP.Presenter {

    public static final String TAG = MainPresenter.class.getSimpleName();

    private MainMVP.View mView;
    private MainMVP.Model mModel;

    private boolean isTestRunning = false;
    private int midSound;
    private List<Question> mQuestionList;

    public MainPresenter() {
        mModel = new MainModel();
    }

    @Override
    public void setView(MainMVP.View view) {
        mView = view;
        mView.newTest();
        isTestRunning = false;

        requestQuestionList();
    }

    private void requestQuestionList() {
        mView.showLoadingContent();
        mModel.setOnQuestionRequestListener(new MainModel.OnQuestionRequestListener() {
            @Override
            public void onSuccess(List<Question> questionsList) {
                mQuestionList = questionsList;
                mView.hideLoadingContent();
            }

            @Override
            public void onError() {
                Log.d(TAG, "Error when downloading questions");
            }
        });
        mModel.getQuestionAttributesArray();
    }

    @Override
    public void viewOnPlayButtonClicked() {
        mView.playSound(mQuestionList.get(0).getAudioBytes());
    }

    @Override
    public void viewOnSoundPlayingCompleted() {

        //Verifies if the user has just started a new test, if so, the presenter must get the options and send to the view show them, if not, nothing has to be done
        if (!isTestRunning) {
            mView.showAlternatives(mQuestionList.get(0));
            isTestRunning = true;
        }
    }

    @Override
    public void viewOnOptionClicked(String optionText) {
        if (isOptionClickedCorrect(optionText)) {
            mView.showCorrectOptionMessage();
            mQuestionList.remove(0);
            if(mQuestionList.size() == 0) {
                requestQuestionList();
            }
            startNewTest();
        }
    }

    private void startNewTest() {
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

    private boolean isOptionClickedCorrect(String userAnswer) {
        if (mQuestionList.get(0).isAnswerCorrect(userAnswer)) {
            return true;
        } else {
            return false;
        }
    }
}
