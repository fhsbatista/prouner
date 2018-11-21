package com.prouner.prouner.main;

import com.prouner.prouner.R;

public class MainModel implements MainMVP.Model {

    @Override
    public Object[] getQuestionAttributes() {
        Object[] questionAttributes = new Object[2];
        questionAttributes[0] = new String[]{"opçao 1", "opçao 2", "opçao 3", "opçao 4"};
        questionAttributes[1] = R.raw.pronounciation;
        return questionAttributes;
    }
}
