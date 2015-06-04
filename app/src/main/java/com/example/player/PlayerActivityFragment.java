package com.example.player;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener {

    private final long UPDATE_SEEKBAR = 1;
//    private int duration = 0;

    private Uri vidUri;
    private String vidAddress;

    private VideoView vidView;
    private SeekBar seekbar;

    private Button btnStart;
    private Button btnPause;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        seekbar = (SeekBar) view.findViewById(R.id.seekBar1);

        vidView = (VideoView) view.findViewById(R.id.videoView1);
        btnStart = (Button) view.findViewById(R.id.button1);
        btnPause = (Button) view.findViewById(R.id.button2);

        handler = new Handler();

        vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        vidView.setOnPreparedListener(this);

        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);

        seekbar.bringToFront();
        seekbar.invalidate();

        seekbar.setOnSeekBarChangeListener(this);

        vidView.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(true) {
                    try {
                        Thread.sleep(UPDATE_SEEKBAR);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            if (vidView != null) {
//                                duration = vidView.getDuration();
//                                if (duration != 0) {
//                                    seekbar.setMax(duration);
//                                }
                                seekbar.setProgress(vidView.getCurrentPosition());
                            }

                        }
                    });
                }
            }
        }).start();

        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        if (id == btnStart.getId()) {
            vidView.start();
        } else {
            vidView.pause();
            vidView.seekTo(vidView.getCurrentPosition());
        }
    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
        int position = arg0.getProgress();
        vidView.seekTo(position);
        if (vidView.isPlaying()) {
            vidView.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        int duration = mp.getDuration();
        seekbar.setMax(duration);

        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekbar.setSecondaryProgress(percent);
            }
        });
    }
}
