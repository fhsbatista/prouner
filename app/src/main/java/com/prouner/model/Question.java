package com.prouner.model;

import java.util.Random;

public class Question {

    private byte[] audio;
    private String[] options;
    private int indexCorrectAnswer;

    public Question(byte[] audio, String[] options) {
        this.audio = audio;
        this.options = options;
        randomizeOptions();


    }

    private void randomizeOptions() {
        //Defines which option number will contain the correct answer
        Random random = new Random();
        indexCorrectAnswer = random.nextInt(4);

        //Manipulate the String array in order to make the correct answer stay in the correct array position
        String aux = options[indexCorrectAnswer]; // Save the string which is currently in the correct position
        options[indexCorrectAnswer] = options[0]; // Put in the correct position, the correct answer (text), the correct answer is always in the position 0
        options[0] = aux; // Put in the position 0 the answer that previously was in the correct position
    }

    private String getCorrectAnswer(){
        return options[indexCorrectAnswer];
    }

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
