package edu.upenn.cis573.travelingsalesman;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class GameActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);
        Intent intent = getIntent();
        int numLocations = intent.getIntExtra("NUMBER_OF_LOCATIONS", 0);
        GameView gameView = (GameView)findViewById(R.id.gameView);
        gameView.setNumLocations(numLocations);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    /*
    This method is called when the user chooses something in the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_clear) {
            GameView gv = (GameView)findViewById(R.id.gameView);
            gv.clearSegments();
            //gv.segments.clear();
            gv.invalidate();
            return true;
        }
        else if (id == R.id.menu_quit) {
            finish();
            return true;
        } else if (id == R.id.menu_undo) {
            GameView gv = (GameView)findViewById(R.id.gameView);
            int segmentsSize = gv.getSegmentsSize();
            if (segmentsSize > 0) {
                gv.removeSegment(segmentsSize - 1);
            } else {
                Toast.makeText(gv.getContext(), "There's nothing to undo.", Toast.LENGTH_LONG).show();
            }
            gv.invalidate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
