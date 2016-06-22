package com.board.stann.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SinglePlayer extends GameMechanics implements OnClickListener {

    private String playerName;
    private List<Integer> cpuValidLocations = new ArrayList<>();
    private HashMap<Integer, String> imageViewPitsByIndex = new HashMap<>();

    private int movementsNumber = 0;
    private int cpuTrayLocation = 13;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        loadInterface();
        setPlayerArea();
        turnOn(player);
        gameStart = new Date();
        delay();
        // Receive player name
        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");
        loadDataForCPU();
    }

    public void loadDataForCPU() {
        cpuValidLocations.add(7);
        cpuValidLocations.add(8);
        cpuValidLocations.add(9);
        cpuValidLocations.add(10);
        cpuValidLocations.add(11);
        cpuValidLocations.add(12);

        imageViewPitsByIndex.put(7, "up_pit1");
        imageViewPitsByIndex.put(8, "up_pit2");
        imageViewPitsByIndex.put(9, "up_pit3");
        imageViewPitsByIndex.put(10, "up_pit4");
        imageViewPitsByIndex.put(11, "up_pit5");
        imageViewPitsByIndex.put(12, "up_pit6");
    }

    @Override
    public void moveStones(final int pitId, final int selectedPit) {
        ImageView currentPit = (ImageView) findViewById(pitId);
        currentPit.setImageResource(R.drawable.emptypit);
        int selectedPitStones = Integer.parseInt(super.texts[selectedPit].getText().toString());

        if (selectedPitStones == 0) return;

        if (player == 1)
            movementsNumber = movementsNumber + 1;

        texts[selectedPit].setText("" + 0);

        int pitNo = selectedPit;

        for (int i = 0; i < selectedPitStones; i++) {
            pitNo += 1;
            if (pitNo <= 13) {
                updateStones(pitNo);

            } else {
                pitNo = 0;
                updateStones(pitNo);
            }
        }

        captureFlag = isItPossibleToCapture(selectedPit, selectedPitStones);

        checkTray(selectedPit, selectedPitStones);
        setPlayerArea();
    }

    @Override
    public void checkTray(int selectedPit, int selectedPitStones) {
        if (player == 1) {
            if (selectedPitStones + selectedPit == 6) {
                // Player 1 free turn (Human)
                turnOn(3);
            }
            else {
                // Player 2 turn (Computer)
                player = 2;
                turnOn(player);
            }
        } else if (player == 2) {
            if (selectedPitStones + selectedPit == 13) {
                // Player 2 free turn (Computer)
                turnOn(4);
            }
            else {
                // Player 1 turn (Human)
                player = 1;
                turnOn(player);
            }
        }
    }

    @Override
    public void showWinner() {
        super.showWinner();
        SavePlayer savePlayer = new SavePlayer();
        savePlayer.savePlayerStatistics(getApplicationContext(), playerName, player1Won, movementsNumber, computeElapsedTime());
    }

    /**
     * This method controls the playing order of the game
     *
     * @param turn : Integer that identifies which player is allowed to move.
     *               1 : Player 1 turn (Human)
     *               2 : Player 2 turn (Computer)
     *               3 : Player 1 free turn (Human)
     *               4 : Player 2 free turn (Computer)
     */
    @Override
    public void turnOn(int turn) {
        final ImageView turnImage = (ImageView) findViewById(R.id.showturn);
        TextView player1 = (TextView) findViewById(R.id.player1);
        TextView player2 = (TextView) findViewById(R.id.player2);

        // Player 1 turn (Human)
        if (turn == 1) {
            turnImage.setImageResource(R.drawable.turn_player);
            player1.setTextColor(Color.GREEN);
            player2.setTextColor(Color.WHITE);
        }
        // Player 2 turn (Computer)
        else if (turn == 2) {
            turnImage.setImageResource(R.drawable.turn_cpu);
            player2.setTextColor(Color.GREEN);
            player1.setTextColor(Color.WHITE);
            cpuPitSelect();
        }
        // Player 1 free turn (Human)
        else if (turn == 3) {
            turnImage.setImageResource(R.drawable.freeturn);
        }
        // Player 2 free turn (Computer)
        else if (turn == 4) {
            turnImage.setImageResource(R.drawable.freeturn);
            cpuPitSelect();
        }
        else
            showWinner();

        if (captureFlag) {
            captureFlag = false;
            turnImage.setImageResource(R.drawable.captured);
        }

        turnImage.setVisibility(View.VISIBLE);
        delay();
    }

    // Chooses a location for the computer to play
    // Returns an integer of the location
    public void cpuPitSelect() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Integer> boardStones = new ArrayList<>();
                for (int i = 0; i < texts.length; i++) {
                    int pitStones = Integer.parseInt(texts[i].getText().toString());
                    boardStones.add(pitStones);
                }

                // If any location lets us go again, return the first we find
                for (int location : cpuValidLocations) {
                    if ((location + boardStones.get(location))
                            % getBoard().length == cpuTrayLocation) {

                        int pitId = getPitViewIdByPitLocation(location);
                        moveStones(pitId, location); return;
                    }
                }

                // If any location lets us capture stones, return the one that gives us
                // the most stones
                int maxStones = 0;
                int pos = cpuValidLocations.get(0);
                for (int location : cpuValidLocations) {
                    int landingPos = (location + boardStones.get(location)) % getBoard().length;
                    if (landingPos == 13)
                        continue;
                    int oppositePos = getOppositePit(landingPos);
                    if (boardStones.get(landingPos) == 0
                            && boardStones.get(oppositePos) > maxStones
                            && oppositePos != location
                            && isItPossibleToCapture(pos, boardStones.get(pos))) {
                        maxStones = boardStones.get(oppositePos);
                        pos = location;
                    }
                }

                // We found at least one opportunity to capture Stones, let's return it
                if (maxStones > 0) {
                    //System.out.println("All your Stones belong to me!");
                    int pitId = getPitViewIdByPitLocation(pos);
                    moveStones(pitId, pos); return;
                }

                // No particular location looks advantageous. Just return a random location
                boolean randomLocationHasStones = false;
                int randomLocation = 7;

                do {
                    randomLocation = cpuValidLocations.get(new Random().nextInt(cpuValidLocations.size()));
                    randomLocationHasStones = boardStones.get(randomLocation) > 0 ? true : false;
                } while (!randomLocationHasStones);

                int pitId = getPitViewIdByPitLocation(randomLocation);
                moveStones(pitId, randomLocation); return;
            }
        }, 2000);
    }

    public int getPitViewIdByPitLocation(int pitLocation) {
        int viewId = getResources().getIdentifier(imageViewPitsByIndex.get(pitLocation), "id", getApplicationContext().getPackageName());
        return viewId;
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