package com.prouner.main;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.prouner.model.Question;
import com.prouner.util.ConnectivityInfoReceiver;
import com.prouner.util.Util;

import java.util.Collections;
import java.util.List;

public class MainPresenter implements MainMVP.Presenter {

    private MainMVP.View mView;
    private MainMVP.Model mModel;

    //Constants
    private static final String TAG = MainPresenter.class.getSimpleName();

    /**
     * <p>This boolean variable is used to make the presenter able to know
     * if the user is currently in a test.</p>
     * <p>It is necessary when the user hits the <b>Play Button</b>,so that the presenter will know if it is necessary to make the view show the options.</p>
     * <p>When the <b>Play Button</b> is hitted, the question's sound will always be played, and the presenter can also request the view to display the answer's options or not. That depends on if the user has hitted the "Play Button" by the first time or not, if it is the first time, the answer's options has to be displayed, otherwise nothing further must happen.</p>
     */
    private boolean isTestRunning = false;

    /**
     * <p>Field which will store a list of questions.</p>
     * <p>The presenter will always send to the view the question that's in
     * the index 0 in this list. And each time the user get a correct answer,
     * the current question in the position 0 will be removed, so that the user
     * can get a new question.</p>
     */
    private List<Question> mQuestionList;

    MainPresenter() {
        mModel = new MainModel();
    }

    @Override
    public void setView(MainMVP.View view) {
        mView = view;
        prepareQuestions();
    }

    @Override
    public void viewOnPlayButtonClicked() {
        mView.playSound(mQuestionList.get(0).getAudioBytes());
    }

    @Override
    public void viewSoundQuestionPlayed() {

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
            } else{
                Collections.shuffle(mQuestionList);
            }
            startNewTest();
        } else{
            mView.showIncorrectOptionMessage();
        }
    }

    @Override
    public void onNetworkConnectionRestored(Context context, ConnectivityInfoReceiver receiver) {
        mView.hideNoNetWorkConnectionMessage();
        prepareQuestions();
        context.unregisterReceiver(receiver);
    }

    /**
     * <p>Notifies the view that the data is being loaded,
     * so that the view can perform actions on the UI to let
     * the user know that the data is being loaded.</p>
     * <p>Also requests the model to download the questions, and implements a callback and attaches it in the Model in order to be notified when the data get downloaded.
     */
    private void requestQuestionList() {
        if(Util.isConnectedToNetwork(mView.getContext())){
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
                    mView.showErrorWhenDownloadingQuestionsMessage();
                }
            });
            mModel.getQuestionAttributesArray();
        } else{
            mView.showNoNetworkConnectionMessage();
            Context context = mView.getContext();
            context.registerReceiver(new ConnectivityInfoReceiver(this, context),
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }

    /**
     * <p>Notifies the view that a new test will be provided,
     * so that the view can update its UI changing text's buttons and removing the answer's options.</p>
     */
    private void startNewTest() {
        mView.updateUIForNewTest();
        isTestRunning = false;
    }

    /**
     * Checks if the user's answer is correct. Necessary to define if a new test has to be started.
     * @param userAnswer String of the user's answer.
     * @return <p><b>True</b> if the user's answer is the correct.</p>
     *         <p><b>False</b> otherwise</p>.
     */
    private boolean isOptionClickedCorrect(String userAnswer) {
        return mQuestionList.get(0).isAnswerCorrect(userAnswer);
    }

    /**
     * This method notifies the view to update its UI for a new tests rounds.
     * And also request a new set of questions for the tests.
     */
    private void prepareQuestions() {
        mView.updateUIForNewTest();
        isTestRunning = false;
        requestQuestionList();
    }
}
