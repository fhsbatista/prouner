package com.prouner.prouner.main;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prouner.prouner.R;

public class MainActivity extends AppCompatActivity implements MainMVP.View{

    private MainMVP.Presenter mPresenter;

    private Button mPlayButton;
    private Button mOptionButton1;
    private Button mOptionButton2;
    private Button mOptionButton3;
    private Button mOptionButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUI();

        mPresenter = new MainPresenter();
        mPresenter.setView(this);


    }

    private void loadUI() {

        mPlayButton = findViewById(R.id.bt_play);
        mOptionButton1 = findViewById(R.id.bt_option_1);
        mOptionButton2 = findViewById(R.id.bt_option_2);
        mOptionButton3 = findViewById(R.id.bt_option_3);
        mOptionButton4 = findViewById(R.id.bt_option_4);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.viewOnPlayButtonClicked();
            }
        });
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void playSound(int idSound) {
        togglePlayButton();
        MediaPlayer mp = MediaPlayer.create(this, idSound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                togglePlayButton();
                mPlayButton.setText(R.string.play_again);
                mPresenter.viewOnSoundPlayingCompleted();
            }
        });
        mp.start();
    }

    @Override
    public void newTest() {
        mPlayButton.setText(R.string.play_test);
        mOptionButton1.setVisibility(View.GONE);
        mOptionButton2.setVisibility(View.GONE);
        mOptionButton3.setVisibility(View.GONE);
        mOptionButton4.setVisibility(View.GONE);
    }

    @Override
    public void showAlternatives(String... options) {

        mOptionButton1.setText(options[0]);
        mOptionButton2.setText(options[1]);
        mOptionButton3.setText(options[2]);
        mOptionButton4.setText(options[3]);

        mOptionButton1.setVisibility(View.VISIBLE);
        mOptionButton2.setVisibility(View.VISIBLE);
        mOptionButton3.setVisibility(View.VISIBLE);
        mOptionButton4.setVisibility(View.VISIBLE);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.viewOnOptionClicked(((Button)v).getText().toString());
            }
        };
        mOptionButton1.setOnClickListener(onClickListener);
        mOptionButton2.setOnClickListener(onClickListener);
        mOptionButton3.setOnClickListener(onClickListener);
        mOptionButton4.setOnClickListener(onClickListener);

    }



    public void togglePlayButton() {
        if(mPlayButton.isEnabled()){
            mPlayButton.setEnabled(false);
        } else{
            mPlayButton.setEnabled(true);
        }
    }

    @Override
    public void showCorrectOptionMessage() {
        Toast.makeText(this, "CorrectAnswer", Toast.LENGTH_SHORT).show();
    }
}
