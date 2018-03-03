package com.example.android.scorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int scoringPoints = 1;
    int scoringGames  = 2;
    int scoringSets   = 3;

    int[] currentScoresForPlayer1 = new int[]{0, 0, 0};
    int[] savedScoresForPlayer1   = new int[]{0, 0, 0};

    int[] currentScoresForPlayer2 = new int[]{0, 0, 0};
    int[] savedScoresForPlayer2   = new int[]{0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTextViewVisibilityStatus(R.id.winner_is, View.GONE);
        setButtonEnabledStatus(R.id.player1_undo_button, false);
        setButtonEnabledStatus(R.id.player2_undo_button, false);
    }

    /**
     * Set the 'visible' property of a specific TextView
     */
    public void setTextViewVisibilityStatus(int viewId, int visibilityType) {
        TextView myView = (TextView) findViewById(viewId);
        myView.setVisibility(visibilityType);
    }

    /**
     * Set the 'enabled' property of a specific Button
     */
    public void setButtonEnabledStatus(int buttonId, boolean status) {
        Button myButton = (Button) findViewById(buttonId);
        myButton.setEnabled(status);
    }

    /**
     * Display the given score for a given Player
     */
    public void displayScoreForPlayer(int score, int playerId, int scoreType) {
        String scoreStr;
        TextView scoreView = (TextView) findViewById(playerId);

        if (scoreType == scoringPoints) {
            switch (score) {
                case 0:
                    scoreStr = "0";
                    break;
                case 1:
                    scoreStr = "15";
                    break;
                case 2:
                    scoreStr = "30";
                    break;
                case 3:
                    scoreStr = "40";
                    break;
                case 5:
                    scoreStr = "Adv.";
                    break;
                default:
                    scoreStr = "    ";
            }
            scoreView.setText(scoreStr);
        } else {
            scoreView.setText(String.valueOf(score));
        }
    }

    /**
     * Display all scores
     */
    public void displayAllScores() {
        displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, 1);
        displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, 1);
        displayScoreForPlayer(currentScoresForPlayer1[1], R.id.player1_games, 2);
        displayScoreForPlayer(currentScoresForPlayer2[1], R.id.player2_games, 2);
        displayScoreForPlayer(currentScoresForPlayer1[2], R.id.player1_sets, 3);
        displayScoreForPlayer(currentScoresForPlayer2[2], R.id.player2_sets, 3);
    }

    /**
     * Reset both players' scores
     */
    public void resetAllScores() {
        for(int i = 0; i < 3; i++) {
            currentScoresForPlayer1[i] = 0;
            currentScoresForPlayer2[i] = 0;
        }
    }

    /**
     * Restore player's previous score
     */
    public void restorePreviousScores() {
        for(int i = 0; i < 3; i++) {
            currentScoresForPlayer1[i] = savedScoresForPlayer1[i];
            currentScoresForPlayer2[i] = savedScoresForPlayer2[i];
        }
    }

    /**
     * Store player's current score
     */
    public void storeCurrentScores() {
        for(int i = 0; i < 3; i++) {
            savedScoresForPlayer1[i] = currentScoresForPlayer1[i];
            savedScoresForPlayer2[i] = currentScoresForPlayer2[i];
        }
    }

    /**
     * Remove the last point added to a player's score
     */
    public void ignoreLastPoint() {
        restorePreviousScores();
        displayAllScores();
    }

    /**
     * Keep track of 'Player 1' score
     */
    public void addPointForPlayer1(View v) {
        storeCurrentScores();
        currentScoresForPlayer1[0] = currentScoresForPlayer1[0] + 1;
        // A player needs to win at least four (4) points to win a game
        //
        // Has 'Player 1' won the game?
        if (currentScoresForPlayer1[0] < 4) {         // No, not yet
            displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
        } else {                            // Maybe
            // In addition to winning at least four (4) points to win a game, a player must
            // have won two (2) more points than their opponent
            //
            // Has 'Player 1' won the game?
            int diffInPoints = currentScoresForPlayer1[0] - currentScoresForPlayer2[0];
            switch (diffInPoints) {
                case 0:                     // No, not yet
                    currentScoresForPlayer1[0] = 3;
                    currentScoresForPlayer2[0] = currentScoresForPlayer1[0];
                    displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
                    displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
                    break;
                case 1:                     // No, not yet
                    // Is 'Player 1' leading the score in the current game?
                    if (currentScoresForPlayer1[0] > currentScoresForPlayer2[0]) {                  // Yes
                        displayScoreForPlayer(5, R.id.player1_points, scoringPoints);
                        displayScoreForPlayer(6, R.id.player2_points, scoringPoints);
                    } else {                                                    // No
                        displayScoreForPlayer(6, R.id.player1_points, scoringPoints);
                        displayScoreForPlayer(5, R.id.player2_points, scoringPoints);
                    }
                    break;
                default:                    // Yes
                    currentScoresForPlayer1[1] = currentScoresForPlayer1[1] + 1;
                    // A player needs to win at least six (6) games to win a set. In addition,
                    // they must have won two (2) more games than their opponent
                    //
                    // Has 'Player 1' won the set?
                    if ((currentScoresForPlayer1[1] >= 6) && ((currentScoresForPlayer1[1] - currentScoresForPlayer2[1]) >= 2)) { // Yes
                        currentScoresForPlayer1[2] = currentScoresForPlayer1[2] + 1;
                        currentScoresForPlayer1[0] = 0;
                        currentScoresForPlayer2[0] = 0;
                        currentScoresForPlayer1[1] = 0;
                        currentScoresForPlayer2[1] = 0;
                        displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer1[1], R.id.player1_games, scoringGames);
                        displayScoreForPlayer(currentScoresForPlayer2[1], R.id.player2_games, scoringGames);
                        displayScoreForPlayer(currentScoresForPlayer1[2], R.id.player1_sets, scoringSets);
                        // Has 'Player 1' won the match?
                        if (currentScoresForPlayer1[2] == 2) {
                            TextView viewWinner = (TextView) findViewById(R.id.winner_is);
                            viewWinner.setText("Winner is: Player 1");
                            viewWinner.setVisibility(View.VISIBLE);
                            setButtonEnabledStatus(R.id.player1_add_button, false);
                            setButtonEnabledStatus(R.id.player2_add_button, false);
                        }
                    } else {                                                                    // No
                        currentScoresForPlayer1[0] = 0;
                        currentScoresForPlayer2[0] = 0;
                        displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer1[1], R.id.player1_games, scoringGames);
                    }
                    break;
            }
        }
        setButtonEnabledStatus(R.id.player1_undo_button, true);
        setButtonEnabledStatus(R.id.player2_undo_button, false);
    }

    /**
     * Remove the last point added to 'Player 1' score
     */
    public void removePointFromPlayer1(View v) {
        if (currentScoresForPlayer1[2] == 2) {
            setTextViewVisibilityStatus(R.id.winner_is, View.GONE);
            setButtonEnabledStatus(R.id.player1_add_button, true);
            setButtonEnabledStatus(R.id.player2_add_button, true);
        }
        setButtonEnabledStatus(R.id.player1_undo_button, false);
        ignoreLastPoint();
    }

    /**
     * Keep track of 'Player 2' score
     */
    public void addPointForPlayer2(View v) {
        storeCurrentScores();
        currentScoresForPlayer2[0] = currentScoresForPlayer2[0] + 1;
        // A player needs to win at least four (4) points to win a game
        //
        // Has 'Player 2' won the game?
        if (currentScoresForPlayer2[0] < 4) {         // No, not yet
            displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
        } else {                            // Maybe
            // In addition to winning at least four (4) points to win a game, a player must
            // have won two (2) more points than their opponent
            //
            // Has 'Player 2' won the game?
            int diffInPoints = currentScoresForPlayer2[0] - currentScoresForPlayer1[0];
            switch (diffInPoints) {
                case 0:                     // No, not yet
                    currentScoresForPlayer2[0] = 3;
                    currentScoresForPlayer1[0] = currentScoresForPlayer2[0];
                    displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
                    displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
                    break;
                case 1:                     // No, not yet
                    // Is 'Player 2' leading the score in the current game?
                    if (currentScoresForPlayer2[0] > currentScoresForPlayer1[0]) {                  // Yes
                        displayScoreForPlayer(5, R.id.player2_points, scoringPoints);
                        displayScoreForPlayer(6, R.id.player1_points, scoringPoints);
                    } else {                                                    // No
                        displayScoreForPlayer(6, R.id.player2_points, scoringPoints);
                        displayScoreForPlayer(5, R.id.player1_points, scoringPoints);
                    }
                    break;
                default:                    // Yes
                    currentScoresForPlayer2[1] = currentScoresForPlayer2[1] + 1;
                    // A player needs to win at least six (6) games to win a set. In addition,
                    // they must have won two (2) more games than their opponent
                    //
                    // Has 'Player 2' won the set?
                    if ((currentScoresForPlayer2[1] >= 6) && ((currentScoresForPlayer2[1] - currentScoresForPlayer1[1]) >= 2)) { // Yes
                        currentScoresForPlayer2[2] = currentScoresForPlayer2[2] + 1;
                        currentScoresForPlayer2[0] = 0;
                        currentScoresForPlayer1[0] = 0;
                        currentScoresForPlayer2[1] = 0;
                        currentScoresForPlayer1[1] = 0;
                        displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer1[1], R.id.player1_games, scoringGames);
                        displayScoreForPlayer(currentScoresForPlayer2[1], R.id.player2_games, scoringGames);
                        displayScoreForPlayer(currentScoresForPlayer2[2], R.id.player2_sets, scoringSets);
                        // Has 'Player 2' won the match?
                        if (currentScoresForPlayer2[2] == 2) {
                            TextView viewWinner = (TextView) findViewById(R.id.winner_is);
                            viewWinner.setText("Winner is: Player 2");
                            viewWinner.setVisibility(View.VISIBLE);
                            setButtonEnabledStatus(R.id.player1_add_button, false);
                            setButtonEnabledStatus(R.id.player2_add_button, false);
                        }
                    } else {                                                                    // No
                        currentScoresForPlayer2[0] = 0;
                        currentScoresForPlayer1[0] = 0;
                        displayScoreForPlayer(currentScoresForPlayer2[0], R.id.player2_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer1[0], R.id.player1_points, scoringPoints);
                        displayScoreForPlayer(currentScoresForPlayer2[1], R.id.player2_games, scoringGames);
                    }
                    break;
            }
        }
        setButtonEnabledStatus(R.id.player1_undo_button, false);
        setButtonEnabledStatus(R.id.player2_undo_button, true);
    }

    /**
     * Remove the last point added to 'Player 2' score
     */
    public void removePointFromPlayer2(View v) {
        if (currentScoresForPlayer2[2] == 2) {
            setTextViewVisibilityStatus(R.id.winner_is, View.GONE);
            setButtonEnabledStatus(R.id.player1_add_button, true);
            setButtonEnabledStatus(R.id.player2_add_button, true);
        }
        setButtonEnabledStatus(R.id.player2_undo_button, false);
        ignoreLastPoint();
    }

    /**
     * Start a new game
     */
    public void startNewGame(View v) {
        resetAllScores();
        displayAllScores();
        setTextViewVisibilityStatus(R.id.winner_is, View.GONE);
        setButtonEnabledStatus(R.id.player1_add_button, true);
        setButtonEnabledStatus(R.id.player2_add_button, true);
        setButtonEnabledStatus(R.id.player1_undo_button, false);
        setButtonEnabledStatus(R.id.player2_undo_button, false);
    }
}
