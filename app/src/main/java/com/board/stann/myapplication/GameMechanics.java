package com.board.stann.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * This class contains methods for the interaction with the elements of the Mossa game.
 */
public class GameMechanics extends Activity  implements View.OnClickListener {

    int player = 1;
    int movementsNumberP1 = 0;
    int movementsNumberP2 = 0;
    boolean player1Won;
    boolean captureFlag;
    Date gameStart = new Date();

    TextView[] texts;
    ImageView[] pits;
    ImageView[] upPits;
    ImageView[] downPits;
    ImageView back = (ImageView) findViewById(R.id.playagain);

    private static int[] board = new int[14];

    private int stones[] = {R.drawable.emptypit, R.drawable.stone1, R.drawable.stones2, R.drawable.stones3, R.drawable.stones4, R.drawable.stones5, R.drawable.stones6, R.drawable.stones7, R.drawable.stones8, R.drawable.stones9, R.drawable.stones10};

    int trayStonesDown[] = {R.drawable.tray, R.drawable.tray1, R.drawable.tray2, R.drawable.tray3, R.drawable.tray4, R.drawable.tray5, R.drawable.tray6, R.drawable.tray7,
            R.drawable.tray8, R.drawable.tray9, R.drawable.tray10, R.drawable.tray11, R.drawable.tray12, R.drawable.tray13, R.drawable.tray14, R.drawable.tray15,
            R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16,
            R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16,
            R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16};

    int trayStonesUp[] = {R.drawable.tray, R.drawable.tray1, R.drawable.tray2, R.drawable.tray3, R.drawable.tray4, R.drawable.tray5, R.drawable.tray6,
            R.drawable.tray7, R.drawable.tray8, R.drawable.tray9, R.drawable.tray10, R.drawable.tray11, R.drawable.tray12, R.drawable.tray13, R.drawable.tray14, R.drawable.tray15,
            R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16,
            R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16,
            R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16, R.drawable.tray16};

    int selectedDownPit[] = {0, 1, 2, 3, 4, 5};
    int selectedUpPit[] = {7, 8, 9, 10, 11, 12};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        ImageView resume = (ImageView) findViewById(R.id.resume);
        ImageView pause = (ImageView) findViewById(R.id.gameController);

        switch (v.getId()) {
            case R.id.down_pit1:
                moveStones(v.getId(), selectedDownPit[0]);
                break;
            case R.id.down_pit2:
                moveStones(v.getId(), selectedDownPit[1]);
                break;
            case R.id.down_pit3:
                moveStones(v.getId(), selectedDownPit[2]);
                break;
            case R.id.down_pit4:
                moveStones(v.getId(), selectedDownPit[3]);
                break;
            case R.id.down_pit5:
                moveStones(v.getId(), selectedDownPit[4]);
                break;
            case R.id.down_pit6:
                moveStones(v.getId(), selectedDownPit[5]);
                break;
            case R.id.up_pit1:
                moveStones(v.getId(), selectedUpPit[0]);
                break;
            case R.id.up_pit2:
                moveStones(v.getId(), selectedUpPit[1]);
                break;
            case R.id.up_pit3:
                moveStones(v.getId(), selectedUpPit[2]);
                break;
            case R.id.up_pit4:
                moveStones(v.getId(), selectedUpPit[3]);
                break;
            case R.id.up_pit5:
                moveStones(v.getId(), selectedUpPit[4]);
                break;
            case R.id.up_pit6:
                moveStones(v.getId(), selectedUpPit[5]);
                break;
            case R.id.gameController:
                resume.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                for (int i = 0; i < pits.length; i++)
                    pits[i].setEnabled(false);
                break;
            case R.id.resume:
                resume.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                setPlayerArea();
                break;
          case R.id.playagain:
                back.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

            case R.id.gamequit:
                gameQuit(v);

            default:
                break;
        }
    }

    public void moveStones(final int pitId, final int selectedPit) {
        ImageView currentPit = (ImageView) findViewById(pitId);
        currentPit.setImageResource(R.drawable.emptypit);
        int selectedPitStones = Integer.parseInt(texts[selectedPit].getText().toString());
        texts[selectedPit].setText("" + 0);

        if (selectedPitStones == 0) return;

        if (player == 1)
            movementsNumberP1 = movementsNumberP1 + 1;
        else
            movementsNumberP2 = movementsNumberP2 + 1;

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

    public void updateStones(int pitNo) {
        int updatedStones;
        if (pitNo == 6 && player == 2)
            pitNo += 1;
        if (pitNo == 13 && player == 1)
            pitNo = 0;

        updatedStones = Integer.parseInt(texts[pitNo].getText().toString()) + 1;

        texts[pitNo].setText("" + updatedStones);

        if (pitNo == 6) {
            pits[pitNo].setImageResource(trayStonesDown[updatedStones]);
        } else if (pitNo == 13) {
            pits[pitNo].setImageResource(trayStonesUp[updatedStones]);
        } else {
            if (updatedStones >= stones.length)
                pits[pitNo].setImageResource(stones[stones.length - 1]);
            else
                pits[pitNo].setImageResource(stones[updatedStones]);
        }

        MediaPlayer mp;
        mp = MediaPlayer.create(GameMechanics.this, R.raw.stonesound);
        mp.start();
    }

    public void checkTray(int selectedPit, int selectedPitStones) {
        if (player == 1) {
            if (selectedPitStones + selectedPit == 6)
                turnOn(3);
            else {
                player = 2;
                turnOn(player);
            }
        } else if (player == 2) {
            if (selectedPitStones + selectedPit == 13)
                turnOn(3);
            else {
                player = 1;
                turnOn(player);
            }
        }
    }

    public boolean isItPossibleToCapture(int selectedPit, int selectedPitStones) {
        int lastPit = selectedPit + selectedPitStones;

        if (lastPit >= pits.length)
            lastPit = lastPit - pits.length;

        int LastPitStones = Integer.parseInt(texts[lastPit].getText().toString());
        int oppositePit = (pits.length - 1) - (lastPit + 1);

        if (LastPitStones == 1 && ((lastPit < 6 && player == 1) || (lastPit > 6 && lastPit < 13 && player == 2))) {
            int oppositePitStones = (Integer.parseInt(texts[oppositePit].getText().toString()));
            if (oppositePitStones > 0) {
                capture(lastPit, oppositePitStones, oppositePit);
                return true;
            }
        }
        return false;
    }

    public void capture(int lastPit, int oppositePitStones, int oppositePit) {
        int downTray = Integer.parseInt(texts[6].getText().toString()) + oppositePitStones + 1;
        int upTray = Integer.parseInt(texts[13].getText().toString()) + oppositePitStones + 1;

        pits[oppositePit].setImageResource(R.drawable.emptypit);
        texts[oppositePit].setText("" + 0);
        pits[lastPit].setImageResource(R.drawable.emptypit);
        texts[lastPit].setText("" + 0);

        if (player == 1) {
            texts[6].setText("" + downTray);
            pits[6].setImageResource(trayStonesDown[downTray]);
        } else {
            texts[13].setText("" + upTray);
            pits[13].setImageResource(trayStonesUp[upTray]);
        }

        if (isGameOver()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showWinner();
                }
            }, 1000);
        }
    }

    /**
     * This method controls the playing order of the game
     *
     * @param turn : Integer that identifies which player is allowed to move.
     *               1 : Player 1 turn (Human)
     *               2 : Player 2 turn (Computer)
     *               3 : Player 1 free turn (Human)
     */
    public void turnOn(int turn) {
        ImageView turnImage = (ImageView) findViewById(R.id.showturn);
        TextView player1 = (TextView) findViewById(R.id.player1);
        TextView player2 = (TextView) findViewById(R.id.player2);

        if (turn == 1) {
            turnImage.setImageResource(R.drawable.turn_player1);
            player1.setTextColor(Color.GREEN);
            player2.setTextColor(Color.WHITE);
        } else if (turn == 2) {
            turnImage.setImageResource(R.drawable.turn_player2);
            player2.setTextColor(Color.GREEN);
            player1.setTextColor(Color.WHITE);
        } else if (turn == 3)
            turnImage.setImageResource(R.drawable.freeturn);

        else
            showWinner();

        if (captureFlag) {
            captureFlag = false;
            turnImage.setImageResource(R.drawable.captured);
        }

        turnImage.setVisibility(View.VISIBLE);
        delay();
    }

    public void setPlayerArea() {
        if (player == 1 && !isGameOver()) {
            for (int i = 0; i < upPits.length; i++) {
                upPits[i].setEnabled(false);
                downPits[i].setEnabled(true);
            }

        } else if (player == 2 && !isGameOver()) {

            for (int i = 0; i < downPits.length; i++) {
                downPits[i].setEnabled(false);
                upPits[i].setEnabled(true);
            }
        } else
            showWinner();
    }

    public void showWinner() {
        ImageView gameOver = (ImageView) findViewById(R.id.gameover);
        TextView score = (TextView) findViewById(R.id.score);
        ImageView playAgain = (ImageView) findViewById(R.id.playagain);
        ImageView gameQuit = (ImageView) findViewById(R.id.gamequit);
        int totalPlayer1 = 0;
        int totalPlayer2 = 0;

        for (int i = 0; i < 6; i++) {
            totalPlayer1 += Integer.parseInt(texts[i].getText().toString());
            texts[i].setText("" + 0);
            pits[i].setImageResource(R.drawable.emptypit);

        }
        totalPlayer1 += Integer.parseInt(texts[6].getText().toString());
        texts[6].setText("" + totalPlayer1);
        pits[6].setImageResource(trayStonesDown[totalPlayer1]);

        for (int i = 7; i < 13; i++) {
            totalPlayer2 += Integer.parseInt(texts[i].getText().toString());
            pits[i].setImageResource(R.drawable.emptypit);
            texts[i].setText("" + 0);

        }
        totalPlayer2 += Integer.parseInt(texts[13].getText().toString());
        texts[13].setText("" + totalPlayer2);
        pits[13].setImageResource(trayStonesUp[totalPlayer2]);

        if (totalPlayer1 > totalPlayer2) {
            gameOver.setImageResource(R.drawable.gameover1);
            score.setText("" + totalPlayer1);
            player1Won = true;
        } else if (totalPlayer1 < totalPlayer2) {
            gameOver.setImageResource(R.drawable.gameover2);
            score.setText("" + totalPlayer2);
            player1Won = false;
        } else {
            gameOver.setImageResource(R.drawable.gamedraw);
            player1Won = movementsNumberP1 < movementsNumberP2 ? true : false;
        }

        gameOver.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        playAgain.setVisibility(View.VISIBLE);
        gameQuit.setVisibility(View.VISIBLE);
    }

    public boolean isGameOver() {
        int count = 0;
        if (player == 1) {
            for (int i = 0; i < 6; i++) {
                count += Integer.parseInt(texts[i].getText().toString());
            }
            return count == 0;
        }

        if (player == 2) {
            for (int i = 7; i < 13; i++) {
                count += Integer.parseInt(texts[i].getText().toString());
            }
            return count == 0;
        }
        return false;
    }

    public void delay() {
        Timer myTimer = new Timer();
        MyTimerTask mTask = new MyTimerTask();
        myTimer.schedule(mTask, 1000);
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    turnOff();
                }
            });
        }
    }

    public void turnOff() {
        ImageView turnImage = (ImageView) findViewById(R.id.showturn);
        turnImage.setVisibility(View.INVISIBLE);
    }



    public void gameQuit(View v) {
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
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public static int[] getBoard() {
        return board;
    }

    /**
     * Returns index of the opposite position on the board
     */
    public int getOppositePit(int pos) {
        return getBoard().length - 2 - pos;
    }

    public String computeElapsedTime() {
        Date gameEnd = new Date();

        long duration  = gameEnd.getTime() - gameStart.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        String elapsedTime = diffInHours + ":" + diffInMinutes + ":" + diffInSeconds;
        return elapsedTime;
    }
}