package mediaplayer.fa7.com.br.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Intent serviceIntent;
    private Button buttonPlayStop;
    private boolean boolMusicPlaying = false;
    private String strAudioLink = "http://www.villopim.com.br/android/Music_01.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            serviceIntent = new Intent(this, MyServicePlay.class);
            initViews();
            setListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " +
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void initViews() {
        buttonPlayStop = (Button) findViewById(R.id.ButtonPlayStop);
        //buttonPlayStop.setBackgroundResources(R.drawable.playbuttonsm);
        buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
    }

    private void setListeners() {
        buttonPlayStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonPlayStopClick();
            }
        });
    }

    private void buttonPlayStopClick() {
        if (!boolMusicPlaying) {
            buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
            playAudio();
            boolMusicPlaying = true;
        } else {
            if (boolMusicPlaying) {
                buttonPlayStop.setBackgroundResource(R.drawable.pausebuttonsm);
                stopMyPlayService();
                boolMusicPlaying = false;
            }
        }
    }

    private void playAudio() {
        serviceIntent.putExtra("sentAudioLink", strAudioLink);
        try {
            startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMyPlayService() {
        try{
            stopService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        boolMusicPlaying = false;
    }

}
