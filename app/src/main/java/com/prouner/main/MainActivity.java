package com.prouner.main;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.prouner.R;
import com.prouner.model.Question;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MainMVP.View {

    private MainMVP.Presenter mPresenter;

    private ProgressBar mLoadingProgressBar;
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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void playSound(byte[] audio) {
        togglePlayButton();
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(getFileFromByteArray(audio).getFD());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.viewOnSoundPlayingCompleted();
                    }
                }, 3000);
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                togglePlayButton();
                mPlayButton.setText(R.string.play_again);
            }
        });

        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
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
    public void showAlternatives(Question question) {
        String[] questionOptions = question.getOptions();
        mOptionButton1.setText(questionOptions[0]);
        mOptionButton2.setText(questionOptions[1]);
        mOptionButton3.setText(questionOptions[2]);
        mOptionButton4.setText(questionOptions[3]);

        mOptionButton1.setVisibility(View.VISIBLE);
        mOptionButton2.setVisibility(View.VISIBLE);
        mOptionButton3.setVisibility(View.VISIBLE);
        mOptionButton4.setVisibility(View.VISIBLE);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.viewOnOptionClicked(((Button) v).getText().toString());
            }
        };
        mOptionButton1.setOnClickListener(onClickListener);
        mOptionButton2.setOnClickListener(onClickListener);
        mOptionButton3.setOnClickListener(onClickListener);
        mOptionButton4.setOnClickListener(onClickListener);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public void showCorrectOptionMessage() {
        Toast.makeText(this, "CorrectAnswer", Toast.LENGTH_SHORT).show();
    }

    private void loadUI() {

        mLoadingProgressBar = findViewById(R.id.pb_loading);
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

    private void togglePlayButton() {
        if (mPlayButton.isEnabled()) {
            mPlayButton.setEnabled(false);
        } else {
            mPlayButton.setEnabled(true);
        }
    }

    private FileInputStream getFileFromByteArray(byte[] bytes) {

        try {
            File temp = File.createTempFile("audio", "mp3", getCacheDir());
            temp.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write(bytes);
            fos.close();

            FileInputStream fis = new FileInputStream(temp);

            return fis;


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public void hideLoadingContent() {
        mLoadingProgressBar.setVisibility(View.GONE);
        mPlayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingContent() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mPlayButton.setVisibility(View.GONE);
    }

}
