package com.prouner.prouner.model;

public class Question {

    private int idAudioResource;
    private String[] options = new String[4];

    public Question(int idAudioResource, String[] options) {
        this.idAudioResource = idAudioResource;
        this.options = options;
    }

    




    public int getIdAudioResource() {
        return idAudioResource;
    }

    public String[] getOptions() {
        return options;
    }
}
