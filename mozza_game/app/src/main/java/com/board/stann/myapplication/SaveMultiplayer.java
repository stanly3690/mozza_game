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


public class SaveMultiplayer extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_multiplayer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_multiplayer, menu);
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
        EditText player1NameView = (EditText) findViewById(R.id.player1_name_et);
        EditText player2NameView = (EditText) findViewById(R.id.player2_name_et);

        String playerName1;
        String playerName2;
        if (player1NameView.getText() != null && player2NameView.getText() != null) {
            playerName1 = player1NameView.getText().toString();
            playerName2 = player2NameView.getText().toString();
        }
        else {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), MultiPlayer.class);
        intent.putExtra("playerName1", playerName1);
        intent.putExtra("playerName2", playerName2);
        startActivity(intent);
    }

    public void savePlayersStatistics(Context context, String playerName1, String playerName2, boolean player1Won, int movementsNumP1, int movementsNumP2, String elapsedTime) {
        PlayerStatisticsHelper helper = new PlayerStatisticsHelper(context);
        boolean player1Exists = helper.checkIfPlayerExists(helper, playerName1);
        boolean player2Exists = helper.checkIfPlayerExists(helper, playerName2);

        int winsP1 = player1Won == true ? 1 : 0;
        int lossesP1 = player1Won == true ? 0 : 1;
        int winsP2 = !player1Won == true ? 1 : 0;
        int lossesP2 = !player1Won == true ? 0 : 1;

        if (player1Exists) {
            try {
                Player player = helper.getPlayerStatisticsByName(helper, playerName1);
                long previousWins = player.getWins();
                long updatedWins = previousWins + winsP1;
                long previousLosses = player.getLosses();
                long updatedLosses = previousLosses + lossesP1;
                helper.updatePlayerStatistics(helper, playerName1, updatedWins, updatedLosses, movementsNumP1, elapsedTime);
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        } else {
            helper.insertPlayerStatistics(helper, playerName1, winsP1, lossesP1, movementsNumP1, elapsedTime);
        }

        if (player2Exists) {
            try {
                Player player = helper.getPlayerStatisticsByName(helper, playerName2);
                long previousWins = player.getWins();
                long updatedWins = previousWins + winsP2;
                long previousLosses = player.getLosses();
                long updatedLosses = previousLosses + lossesP2;
                helper.updatePlayerStatistics(helper, playerName2, updatedWins, updatedLosses, movementsNumP2, elapsedTime);
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        } else {
            helper.insertPlayerStatistics(helper, playerName2, winsP2, lossesP2, movementsNumP2, elapsedTime);
        }

        helper.readData(helper);
    }
}
