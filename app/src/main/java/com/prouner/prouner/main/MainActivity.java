package com.prouner.prouner.main;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter();
        mPresenter.setView(this);

        loadUI();
        setOnClickListeners();
    }

    private void loadUI() {

        mPlayButton = findViewById(R.id.bt_play);
    }


    private void setOnClickListeners() {
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.pronounciation);
                mp.start();
                mPlayButton.setEnabled(false);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        mPlayButton.setEnabled(true);
                    }
                });
            }
        });
    }



}
