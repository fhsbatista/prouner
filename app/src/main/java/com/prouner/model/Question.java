package com.prouner.model;

import java.util.Random;

/**
 * Object that represents the question which will be displayed to the user.
 */
public class Question {

    /**
     * An array of bytes that stores an audio, which represents the audio that will be played to the user.
     */
    private byte[] audio;

    /**
     * <p>An array of strings that stores each option for a question.</p>
     * <p>It has to always represents only 4 positions since the user will always have only 4 options.</p>
     * <p>In the very first moment when it is downloaded, the index 0 will always store the correct
     * option, but it will be randomized later, and the index that holds the correct option will be
     * kept in the field <b>indexCorrectanswer</b>. </b></p></o>
     */
    private String[] options;

    /**
     * <p>Field that stores an integer, which the index of the option's array, which contains the correct option for the current question.</p>
     */
    private int indexCorrectAnswer;

    /**
     * <p>This constructor will initialize both audio's bytes and option's array.</p>
     * <p>Also randomize the options.</p>
     * @param audio Array of bytes, which must stores the question's audio.
     * @param options Array of strings, which must stores the question's text.
     */
    public Question(byte[] audio, String[] options) {
        this.audio = audio;
        this.options = options;
        randomizeOptions();
    }

    /**
     * <p>Randomizes the options of the current question.</p>
     * <p>By default, the correction option is always that one which is
     * stored in the index 0 of the option's string array. This method will shuffle
     * shuffle the indexes, so that the user will always see the correct option in a different position.</p>
     */
    private void randomizeOptions() {
        //Defines which option number will contain the correct answer
        Random random = new Random();
        indexCorrectAnswer = random.nextInt(4);

        //Manipulate the String array in order to make the correct answer stay in the correct array position
        String aux = options[indexCorrectAnswer]; // Save the string which is currently in the correct position
        options[indexCorrectAnswer] = options[0]; // Put in the correct position, the correct answer (text), the correct answer is always in the position 0
        options[0] = aux; // Put in the position 0 the answer that previously was in the correct position
    }

    /**
     * Returns a string which contains the text of the correct answer.
     * @return String that contains the correct answer's text.
     */
    private String getCorrectAnswer(){
        return options[indexCorrectAnswer];
    }

    /**
     * Verifies if the answer received matches the correct answer.
     * @param answer String that must contains the user's answer.
     * @return True if the string received matches the user's answer, otherwise it returns false.
     */
    public boolean isAnswerCorrect(String answer){
        return answer.equals(getCorrectAnswer());
    }


    public byte[] getAudioBytes() {
        return audio;
    }

    public String[] getOptions() {
        return options;
    }
}
