package com.board.stann.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class MultiPlayer extends GameMechanics implements OnClickListener {

    private String playerName1;
    private String playerName2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutliplayer);
        loadInterface();
        setPlayerArea();
        turnOn(player);
        gameStart = new Date();
        // Receive players names
        Intent intent = getIntent();
        playerName1 = intent.getStringExtra("playerName1");
        playerName2 = intent.getStringExtra("playerName2");
        delay();
    }

    @Override
    public void showWinner() {

        super.showWinner();
        SaveMultiplayer saveMultiplayer = new SaveMultiplayer();
        saveMultiplayer.savePlayersStatistics(getApplicationContext(), playerName1, playerName2, player1Won, movementsNumberP1, movementsNumberP2, computeElapsedTime());
    }


    public void loadInterface() {
        TextView text1 = (TextView) findViewById(R.id.down_label1);
        TextView text2 = (TextView) findViewById(R.id.down_label2);
        TextView text3 = (TextView) findViewById(R.id.down_label3);
        TextView text4 = (TextView) findViewById(R.id.down_label4);
        TextView text5 = (TextView) findViewById(R.id.down_label5);
        TextView text6 = (TextView) findViewById(R.id.down_label6);
        TextView text7 = (TextView) findViewById(R.id.trayrighttext);
        TextView text8 = (TextView) findViewById(R.id.up_label1);
        TextView text9 = (TextView) findViewById(R.id.up_label2);
        TextView text10 = (TextView) findViewById(R.id.up_label3);
        TextView text11 = (TextView) findViewById(R.id.up_label4);
        TextView text12 = (TextView) findViewById(R.id.up_label5);
        TextView text13 = (TextView) findViewById(R.id.up_label6);
        TextView text14 = (TextView) findViewById(R.id.traylefttext);
        ImageView pit1 = (ImageView) findViewById(R.id.down_pit1);
        ImageView pit2 = (ImageView) findViewById(R.id.down_pit2);
        ImageView pit3 = (ImageView) findViewById(R.id.down_pit3);
        ImageView pit4 = (ImageView) findViewById(R.id.down_pit4);
        ImageView pit5 = (ImageView) findViewById(R.id.down_pit5);
        ImageView pit6 = (ImageView) findViewById(R.id.down_pit6);
        ImageView pit7 = (ImageView) findViewById(R.id.trayright);
        ImageView pit8 = (ImageView) findViewById(R.id.up_pit1);
        ImageView pit9 = (ImageView) findViewById(R.id.up_pit2);
        ImageView pit10 = (ImageView) findViewById(R.id.up_pit3);
        ImageView pit11 = (ImageView) findViewById(R.id.up_pit4);
        ImageView pit12 = (ImageView) findViewById(R.id.up_pit5);
        ImageView pit13 = (ImageView) findViewById(R.id.up_pit6);
        ImageView pit14 = (ImageView) findViewById(R.id.trayleft);

        super.texts = new TextView[]{text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13, text14};
        super.pits = new ImageView[]{pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13, pit14};
        super.upPits = new ImageView[]{pit8, pit9, pit10, pit11, pit12, pit13};
        super.downPits = new ImageView[]{pit1, pit2, pit3, pit4, pit5, pit6};
    }
}

