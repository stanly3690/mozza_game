package com.board.stann.myapplication;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.board.stann.myapplication.db.PlayerStatisticsHelper;

import java.io.File;


public class MainActivity extends ActionBarActivity implements OnClickListener {

    boolean flag;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = true;
        player = MediaPlayer.create(MainActivity.this, R.raw.music);
        player.start();
        checkIfDbExists();
    }

    public void checkIfDbExists() {
        File database = getApplicationContext().getDatabasePath("Game.db");

        //upgradeDb();
        if (!database.exists()) {
            Log.i("Database", "Not Found");
            createDb();
        }
    }

    public void createDb() {
        PlayerStatisticsHelper helper = new PlayerStatisticsHelper(getApplicationContext());
        helper.onCreate(helper.getWritableDatabase());
    }

    public void upgradeDb() {
        PlayerStatisticsHelper helper = new PlayerStatisticsHelper(getApplicationContext());
        helper.onUpgrade(helper.getWritableDatabase(), 1, 2);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.singlebutton:
                intent = new Intent(getApplicationContext(), SavePlayer.class);
                startActivity(intent);
                break;
            case R.id.multibutton:
                intent = new Intent(getApplicationContext(), SaveMultiplayer.class);
                startActivity(intent);
                break;
            case R.id.howtoplaybutton:
                intent = new Intent(getApplicationContext(), HowToPlay.class);
                startActivity(intent);
                break;
            case R.id.statitics:
                intent = new Intent(getApplicationContext(), GameStatistics.class);
                startActivity(intent);
                break;
            case R.id.managemusic:
                manageMusic();
                break;
            case R.id.gamequit:
                onBackPressed();
                break;
        }
    }

    private void manageMusic() {

        ImageView icon = (ImageView) findViewById(R.id.managemusic);
        if (flag) {
            icon.setImageResource(R.drawable.soundoff1);
            player.pause();
            flag = false;
            return;
        }
        if (!flag) {
            icon.setImageResource(R.drawable.soundon1);
            if (player != null)
                player.start();
            flag = true;
            return;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you really want to exit?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        if (player != null)
                            player.pause();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

}
