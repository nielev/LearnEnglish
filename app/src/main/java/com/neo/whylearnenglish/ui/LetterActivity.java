package com.neo.whylearnenglish.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.adapter.AcceptationAdapter;
import com.neo.whylearnenglish.adapter.SentenceAdapter;
import com.neo.whylearnenglish.base.BaseActivity;
import com.neo.whylearnenglish.bean.Letter;
import com.neo.whylearnenglish.databinding.ActivityLetterBinding;
import com.neo.whylearnenglish.utils.LogUtil;

import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 单词界面
 * Created by Neo on 2016/12/16.
 */

public class LetterActivity extends BaseActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "LetterActivity";

    @BindView(R.id.iv_pronunciation_en)
    ImageView mIv_pronunciation_en;
    @BindView(R.id.iv_pronunciation_am)
    ImageView mIv_pronunciation_am;
    @BindView(R.id.rv_pos_desc)
    RecyclerView mRv_pos_desc;
    @BindView(R.id.rv_sentence)
    RecyclerView mRv_sentence;

    private Letter letter;
    private MediaPlayer mediaPlayer;
    private AnimationDrawable animationDrawable;

    @Override
    protected void initView() {
        ActivityLetterBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_letter);
        Intent intent = getIntent();
        letter = (Letter) intent.getSerializableExtra("letter");
        if(null == letter){
            letter = new Letter();
        }
        binding.setLetter(letter);

        ButterKnife.bind(this);
        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void initListener() {
        mIv_pronunciation_en.setOnClickListener(this);
        mIv_pronunciation_am.setOnClickListener(this);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
    }

    @Override
    protected void initData() {
        mRv_pos_desc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRv_pos_desc.setAdapter(new AcceptationAdapter(letter.posList));
        mRv_sentence.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRv_sentence.setAdapter(new SentenceAdapter(letter.sentenceList));
        setColor(this, 0xffe9e4d9);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pronunciation_en:

                if(null != letter.pronunciationList && letter.pronunciationList.size() > 0)
                    letterPronPlay(letter.pronunciationList.get(0).pron, mIv_pronunciation_en);
                break;
            case R.id.iv_pronunciation_am:
                if(null != letter.pronunciationList && letter.pronunciationList.size() > 1)
                    letterPronPlay(letter.pronunciationList.get(1).pron, mIv_pronunciation_am);
                break;
        }
    }
    private ImageView mCurrentIV;
    private void letterPronPlay(String url, ImageView iv) {
        iv.setImageResource(R.drawable.anim_voice_play);
        mCurrentIV = iv;
        animationDrawable = (AnimationDrawable) iv.getDrawable();
        animationDrawable.start();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.i(TAG, "media prepare finish");
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        animationDrawable.stop();
        mCurrentIV.setImageResource(R.drawable.selector_voice_play);
        LogUtil.i(TAG, "media play completion");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                LogUtil.i(TAG, "media play error:MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                LogUtil.i(TAG, "media play error:MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                LogUtil.i(TAG, "media play error:MEDIA_ERROR_UNKNOWN");
                break;
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //当MediaPlayer正在缓冲时，将调用活动的onBufferingUpdate方法
        LogUtil.i(TAG,"buffering update:" + percent);
    }

    @Override
    protected void onDestroy() {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
