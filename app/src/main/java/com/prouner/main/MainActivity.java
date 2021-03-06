package com.prouner.main;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private MediaPlayer mMediaPlayer = new MediaPlayer();



    //Constants
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int PLAYING_SOUND_DELAY = 1500;


    //UI Elements
    private ProgressBar mLoadingProgressBar;
    private ImageButton mPlayButton;
    private Button mOptionButton1;
    private Button mOptionButton2;
    private Button mOptionButton3;
    private Button mOptionButton4;
    private ImageView mImageViewNoNetwork;
    private TextView mTextViewNoNetwork;

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
        //Checks if the an audio is being played, if so, the media player is reseted so it can play the
        //the audio from its beginning
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(Util.getFileFromByteArray(audio, this).getFD());
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Reset the mediaplayer object so that it can receive a new datasource when the
                    //play button get hitted again
                    mMediaPlayer.reset();
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
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
        Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIncorrectOptionMessage() {
        Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void showNoNetworkConnectionMessage() {
        mTextViewNoNetwork.setVisibility(View.VISIBLE);
        mImageViewNoNetwork.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.GONE);
        mPlayButton.setVisibility(View.GONE);
        mOptionButton1.setVisibility(View.GONE);
        mOptionButton2.setVisibility(View.GONE);
        mOptionButton3.setVisibility(View.GONE);
        mOptionButton4.setVisibility(View.GONE);
    }

    @Override
    public void hideNoNetWorkConnectionMessage() {
        mTextViewNoNetwork.setVisibility(View.GONE);
        mImageViewNoNetwork.setVisibility(View.GONE);
    }

    //Bind the UI elements
    private void bindUI() {


        mLoadingProgressBar = findViewById(R.id.pb_loading);
        mPlayButton = findViewById(R.id.bt_play);
        mOptionButton1 = findViewById(R.id.bt_option_1);
        mOptionButton2 = findViewById(R.id.bt_option_2);
        mOptionButton3 = findViewById(R.id.bt_option_3);
        mOptionButton4 = findViewById(R.id.bt_option_4);
        mImageViewNoNetwork = findViewById(R.id.iv_nonetwork);
        mTextViewNoNetwork = findViewById(R.id.tv_nonetwork);




        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.viewOnPlayButtonClicked();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();

    }
}
