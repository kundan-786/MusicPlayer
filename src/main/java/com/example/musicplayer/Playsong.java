package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
        updateSeek.interrupt();
    }
   TextView textView;
   ImageView play,next,previous;
   SeekBar seekBar;
   ArrayList<File> songs;
   String txtcontent;
   MediaPlayer mediaplayer;
   int position;
    Thread updateSeek;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_playsong);
        textView=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songList");
        txtcontent=intent.getStringExtra("curSong");
        position=intent.getIntExtra("position",0);
        textView.setText(txtcontent);
        textView.setSelected(true);
        Uri uri =Uri.parse(songs.get(position).toString());
        mediaplayer = MediaPlayer.create(this,uri);
        mediaplayer.start();
        seekBar.setMax(mediaplayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
             mediaplayer.seekTo(seekBar.getProgress());
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaplayer.isPlaying())
                { mediaplayer.pause();
                  play.setImageResource(R.drawable.play);
                }else {
                    mediaplayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.stop();
                mediaplayer.release();
                play.setImageResource(R.drawable.pause);
                if(position!=songs.size()-1)
                    position++;
                else position=0;
                Uri uri =Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                seekBar.setMax(mediaplayer.getDuration());
                txtcontent=songs.get(position).getName().toString();
                textView.setText(txtcontent);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.stop();
                mediaplayer.release();
                play.setImageResource(R.drawable.pause);
                if(position!=0)
                    position--;
                else position=songs.size()-1;
                Uri uri =Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                seekBar.setMax(mediaplayer.getDuration());
                txtcontent=songs.get(position).getName().toString();
                textView.setText(txtcontent);
            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
               int curpos=0;
               try {
                   while (curpos<mediaplayer.getDuration()){
                       curpos=mediaplayer.getCurrentPosition();
                       seekBar.setProgress(curpos);
                       sleep(800);
                   }

               }catch (Exception e){
                   e.printStackTrace();
               }
            }};updateSeek.start();




    }
}