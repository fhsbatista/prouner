package com.prouner.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prouner.R;
import com.prouner.model.Question;
import com.prouner.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MainMVP.View {

    private MainMVP.Presenter mPresenter;

    //Constants
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int PLAYING_SOUND_DELAY = 3000;


    //UI Elements
    private ProgressBar mLoadingProgressBar;
    private ImageButton mPlayButton;
    private Button mOptionButton1;
    private Button mOptionButton2;
    private Button mOptionButton3;
    private Button mOptionButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind UI elements
        bindUI();

        //Initalize the presenter
        mPresenter = new MainPresenter();
        mPresenter.setView(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void playSound(byte[] audio) {
        //Makes the "play btn_shape_round" unable so that the user has to wait for the sound gets finished to hit the btn_shape_round again
        togglePlayButton();
        //Executes the sound received
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(Util.getFileFromByteArray(audio, this).getFD());
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.viewSoundQuestionPlayed();
                        }
                    }, PLAYING_SOUND_DELAY);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Makes the "Play btn_shape_round" able to be hitted again, and sets a new text for it like "Replay"
                    mp.release();
                    togglePlayButton();
//                    mPlayButton.setText(R.string.play_again);
                }
            });

            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d(TAG , "Media Player : onErrorListener - " + "Error when playing the sound");
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Error when playing the sound");
        }



    }

    @Override
    public void updateUIForNewTest() {
//        mPlayButton.setText(R.string.play_test);
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

    @Override
    public void showErrorWhenDownloadingQuestionsMessage() {
        Toast.makeText(this, "Problems when downloading the questions", Toast.LENGTH_SHORT).show();
    }

    //Bind the UI elements
    private void bindUI() {


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

    //Enables the play btn_shape_round if it is unable and unables it if it is unable
    private void togglePlayButton() {
        if (mPlayButton.isEnabled()) {
            mPlayButton.setEnabled(false);
        } else {
            mPlayButton.setEnabled(true);
        }
    }

}
