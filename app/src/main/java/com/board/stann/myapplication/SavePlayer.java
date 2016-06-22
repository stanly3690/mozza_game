package com.board.stann.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.board.stann.myapplication.db.Player;
import com.board.stann.myapplication.db.PlayerStatisticsHelper;

import java.sql.SQLDataException;


public class SavePlayer extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_player);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        EditText playerNameView = (EditText) findViewById(R.id.player_name_et);
        String playerName;
        if (playerNameView.getText() != null) {
            playerName = playerNameView.getText().toString();
        }
        else {
            return;
        }

        Intent intent = new Intent(getApplicationContext(),SinglePlayer.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
    }

    public void savePlayerStatistics(Context context, String playerName, boolean playerWon, int movementsNum, String elapsedTime) {
        PlayerStatisticsHelper helper = new PlayerStatisticsHelper(context);
        boolean playerExists = helper.checkIfPlayerExists(helper, playerName);

        int wins = playerWon == true ? 1 : 0;
        int losses = playerWon == true ? 0 : 1;

        if (playerExists) {
            try {
                Player player = helper.getPlayerStatisticsByName(helper, playerName);
                long previousWins = player.getWins();
                long updatedWins = previousWins + wins;
                long previousLosses = player.getLosses();
                long updatedLosses = previousLosses + losses;
                helper.updatePlayerStatistics(helper, playerName, updatedWins, updatedLosses, movementsNum, elapsedTime);
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        } else {
            helper.insertPlayerStatistics(helper, playerName, wins, losses, movementsNum, elapsedTime);
        }

        helper.readData(helper);
    }
}