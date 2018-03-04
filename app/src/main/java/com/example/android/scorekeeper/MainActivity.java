package com.example.android.scorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView winnerView;
    Button resetAllScores;
    Button addPlayer1Button;
    Button addPlayer2Button;
    Button undoPlayer1Button;
    Button undoPlayer2Button;
    TextView scoreSetsPlayer1View;
    TextView scoreSetsPlayer2View;
    TextView scoreGamesPlayer1View;
    TextView scoreGamesPlayer2View;
    TextView scorePointsPlayer1View;
    TextView scorePointsPlayer2View;

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

        winnerView              = (TextView) findViewById(R.id.winner_is);
        resetAllScores          = (Button) findViewById(R.id.reset_button);
        addPlayer1Button        = (Button) findViewById(R.id.player1_add_button);
        addPlayer2Button        = (Button) findViewById(R.id.player2_add_button);
        undoPlayer1Button       = (Button) findViewById(R.id.player1_undo_button);
        undoPlayer2Button       = (Button) findViewById(R.id.player2_undo_button);
        scoreSetsPlayer1View    = (TextView) findViewById(R.id.player1_sets);
        scoreSetsPlayer2View    = (TextView) findViewById(R.id.player2_sets);
        scoreGamesPlayer1View   = (TextView) findViewById(R.id.player1_games);
        scoreGamesPlayer2View   = (TextView) findViewById(R.id.player2_games);
        scorePointsPlayer1View  = (TextView) findViewById(R.id.player1_points);
        scorePointsPlayer2View  = (TextView) findViewById(R.id.player2_points);

        setTextViewVisibilityStatus(winnerView, View.GONE);
        setButtonEnabledStatus(undoPlayer1Button, false);
        setButtonEnabledStatus(undoPlayer2Button, false);
    }

    /**
     * Set the 'visible' property of a specific TextView
     */
    public void setTextViewVisibilityStatus(View v, int visibilityType) {
        v.setVisibility(visibilityType);
    }

    /**
     * Set the 'enabled' property of a specific Button
     */
    public void setButtonEnabledStatus(View v, boolean status) {
        v.setEnabled(status);
    }

    /**
     * Display the given score for a given Player
     */
    public void displayScoreForPlayer(TextView v, int score, int scoreType) {
        String scoreStr;

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
            v.setText(scoreStr);
        } else {
            v.setText(String.valueOf(score));
        }
    }

    /**
     * Display all scores
     */
    public void displayAllScores() {
        displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], 1);
        displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], 1);
        displayScoreForPlayer(scoreGamesPlayer1View, currentScoresForPlayer1[1], 2);
        displayScoreForPlayer(scoreGamesPlayer2View, currentScoresForPlayer2[1], 2);
        displayScoreForPlayer(scoreSetsPlayer1View, currentScoresForPlayer1[2], 3);
        displayScoreForPlayer(scoreSetsPlayer2View, currentScoresForPlayer2[2], 3);
    }

    /**
     * Reset both players' scores
     */
    public void resetAllPlayersScores() {
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
            displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
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
                    displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
                    displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
                    break;
                case 1:                     // No, not yet
                    // Is 'Player 1' leading the score in the current game?
                    if (currentScoresForPlayer1[0] > currentScoresForPlayer2[0]) {                  // Yes
                        displayScoreForPlayer(scorePointsPlayer1View, 5, scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer2View, 6, scoringPoints);
                    } else {                                                    // No
                        displayScoreForPlayer(scorePointsPlayer1View, 6, scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer2View, 5, scoringPoints);
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
                        displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
                        displayScoreForPlayer(scoreGamesPlayer1View, currentScoresForPlayer1[1], scoringGames);
                        displayScoreForPlayer(scoreGamesPlayer2View, currentScoresForPlayer2[1], scoringGames);
                        displayScoreForPlayer(scoreSetsPlayer1View, currentScoresForPlayer1[2], scoringSets);
                        // Has 'Player 1' won the match?
                        if (currentScoresForPlayer1[2] == 2) {
                            winnerView.setText("Winner is: Player 1");
                            winnerView.setVisibility(View.VISIBLE);
                            setButtonEnabledStatus(addPlayer1Button, false);
                            setButtonEnabledStatus(addPlayer1Button, false);
                        }
                    } else {                                                                    // No
                        currentScoresForPlayer1[0] = 0;
                        currentScoresForPlayer2[0] = 0;
                        displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
                        displayScoreForPlayer(scoreGamesPlayer1View, currentScoresForPlayer1[1], scoringGames);
                    }
                    break;
            }
        }
        setButtonEnabledStatus(undoPlayer1Button, true);
        setButtonEnabledStatus(undoPlayer2Button, false);
    }

    /**
     * Remove the last point added to 'Player 1' score
     */
    public void removePointFromPlayer1(View v) {
        if (currentScoresForPlayer1[2] == 2) {
            setTextViewVisibilityStatus(winnerView, View.GONE);
            setButtonEnabledStatus(addPlayer1Button, true);
            setButtonEnabledStatus(addPlayer2Button, true);
        }
        setButtonEnabledStatus(undoPlayer1Button, false);
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
            displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
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
                    displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
                    displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
                    break;
                case 1:                     // No, not yet
                    // Is 'Player 2' leading the score in the current game?
                    if (currentScoresForPlayer2[0] > currentScoresForPlayer1[0]) {                  // Yes
                        displayScoreForPlayer(scorePointsPlayer2View, 5, scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer1View, 6, scoringPoints);
                    } else {                                                    // No
                        displayScoreForPlayer(scorePointsPlayer2View, 56, scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer1View, 55, scoringPoints);
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
                        displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
                        displayScoreForPlayer(scoreGamesPlayer2View, currentScoresForPlayer2[1], scoringGames);
                        displayScoreForPlayer(scoreGamesPlayer1View, currentScoresForPlayer1[1], scoringGames);
                        displayScoreForPlayer(scoreSetsPlayer2View, currentScoresForPlayer2[2], scoringSets);
                        // Has 'Player 2' won the match?
                        if (currentScoresForPlayer2[2] == 2) {
                            winnerView.setText("Winner is: Player 2");
                            winnerView.setVisibility(View.VISIBLE);
                            setButtonEnabledStatus(addPlayer1Button, false);
                            setButtonEnabledStatus(addPlayer2Button, false);
                        }
                    } else {                                                                    // No
                        currentScoresForPlayer2[0] = 0;
                        currentScoresForPlayer1[0] = 0;
                        displayScoreForPlayer(scorePointsPlayer2View, currentScoresForPlayer2[0], scoringPoints);
                        displayScoreForPlayer(scorePointsPlayer1View, currentScoresForPlayer1[0], scoringPoints);
                        displayScoreForPlayer(scoreGamesPlayer2View, currentScoresForPlayer2[1], scoringGames);
                    }
                    break;
            }
        }
        setButtonEnabledStatus(undoPlayer1Button, false);
        setButtonEnabledStatus(undoPlayer2Button, true);
    }

    /**
     * Remove the last point added to 'Player 2' score
     */
    public void removePointFromPlayer2(View v) {
        if (currentScoresForPlayer2[2] == 2) {
            setTextViewVisibilityStatus(winnerView, View.GONE);
            setButtonEnabledStatus(addPlayer1Button, true);
            setButtonEnabledStatus(addPlayer2Button, true);
        }
        setButtonEnabledStatus(undoPlayer2Button, false);
        ignoreLastPoint();
    }

    /**
     * Start a new game
     */
    public void startNewGame(View v) {
        resetAllPlayersScores();
        displayAllScores();
        setTextViewVisibilityStatus(winnerView, View.GONE);
        setButtonEnabledStatus(addPlayer1Button, true);
        setButtonEnabledStatus(addPlayer2Button, true);
        setButtonEnabledStatus(undoPlayer1Button, false);
        setButtonEnabledStatus(undoPlayer2Button, false);
    }
}
