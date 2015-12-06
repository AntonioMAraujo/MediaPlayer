package mediaplayer.fa7.com.br.mediaplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity  implements ServiceConnection{

    private Intent serviceIntent;
    private Button buttonPlayStop;
    private boolean boolMusicPlaying = false;
    private Button btnProximo;
    private Button btnAnterior;
    private int musicaTocando = 0;

    private MyServicePlay.Controller controle;
    private Funcionalidade funcionalidades;
    private ServiceConnection connection;


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
        btnAnterior  = (Button) findViewById(R.id.btnAnterior);
        btnProximo  = (Button) findViewById(R.id.btnProximo);
    }

    private void setListeners() {
        buttonPlayStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonPlayStopClick();
            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anterior();;
            }
        });

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proximo();
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
        //serviceIntent.putExtra("musica",musicas.get(musicaTocando));
        try {
            if(connection == null) {
                connection = this;
                bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE); // Context.BIND_AUTO_CREATE
                startService(serviceIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void proximo() {
        try {
            funcionalidades.proximo();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void anterior() {
        try {
            funcionalidades.anterior();
            } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMyPlayService() {
        try{
            if(connection != null) {
                unbindService(connection);
                connection = null;
                stopService(serviceIntent);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        boolMusicPlaying = false;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        controle = (MyServicePlay.Controller) iBinder;
        funcionalidades = controle.getMediaPlayer();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }


}
