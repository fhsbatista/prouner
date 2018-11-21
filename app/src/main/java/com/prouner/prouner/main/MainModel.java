package com.prouner.prouner.main;

import com.prouner.prouner.R;
import com.prouner.prouner.model.Question;

public class MainModel implements MainMVP.Model {

    @Override
    public Question getQuestionAttributes() {
        Question question = new Question(
                R.raw.pronounciation,
                new String[]{"opçao 1", "opçao 2", "opçao 3", "opçao 4"}
                );

        return question;
    }
}
