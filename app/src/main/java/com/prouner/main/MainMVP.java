package com.prouner.main;

import android.content.Context;

import com.prouner.model.Question;

public interface MainMVP {

    interface View {

        /**
         * Executes the sound of the question.
         * @param audio Array of bytes which contains the sound that has to be played to the user.
         */
        void playSound(byte[] audio);

        /**
         * Must be executed when the answer options has to be displayed to the use.
         * @param question Object which contains the four answer options.
         */
        void showAlternatives(Question question);

        /**
         * Will provide the context from the view.
         * @return Context object
         */
        Context getContext();

        /**
         * Can be used to handle a notification from the presenter,
         * which means that the user has informed that correct answer.
         */
        void showCorrectOptionMessage();

        /**
         * This method can be used to handle some error that may
         * happen when the data is being downloaded, so that some
         * error message can be shown for the user.
         */
        void showErrorWhenDownloadingQuestionsMessage();

        /**
         * This method is provided so that the view can be able
         * to organize its ui elements for a new test,
         * such as: change their visibility, text and so on.
         */
        void updateUIForNewTest();

        /**
         * This method is provided so that the view can be able to a UI element representing that the data is being loaded, a "Loading" text or a "Progress Bar" can be shown for instance.
         */
        void showLoadingContent();

        /**
         * This method is provided so that the view can be able to hide any UI element used to represent a data loading. This way the important data can be shown for the user.
         */
        void hideLoadingContent();


    }

    interface Presenter{
        /**
         * Attach the view into the presenter so that both view and presenter can interact each other
         * @param view View which is implementing the MainMVP.View interface.
         */
        void setView(View view);

        /**
         * Notifies the Presenter that the user pressed the button to play the question's sound.
         */
        void viewOnPlayButtonClicked();

        /**
         * Notifies the Presenter that the sound playing has been completed, so that the presenter can handle it
         * and perform another action, such as show the answer options for the user.
         */
        void viewSoundQuestionPlayed();

        /**
         * Notifies the Presenter that the user has pressed an answer option
         * @param optionText Contains the text of the option which the user has chosen.
         *                   <p>It will be used to compare
         *                   the user's answers with the correct answer so that the presenter can know whether                             the user has made the correct choose.</p>
         */
        void viewOnOptionClicked(String optionText);
    }

    interface Model {

        // TODO below I'm using a class which implements the Model interface, is it a good practice?
        void setOnQuestionRequestListener(MainModel.OnQuestionRequestListener onQuestionRequestListener);

        /**
         * Requests to Model grab questions from its source.
         * <p>Later the model will send the result using the callback "OnQuestionRequestListener".</p>
         */
        void getQuestionAttributesArray();

    }

}
