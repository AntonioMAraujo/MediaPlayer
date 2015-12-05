package mediaplayer.fa7.com.br.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 28/11/2015.
 */
public class MyServicePlay extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener, Funcionalidade {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Controller controle = new Controller();
    private List<Integer> musicas = new ArrayList<>();
    private int musica = 0;


    public void onCreate() {
        try {
            Log.i("SCRIPT", "onCreate()");
            musicas = listRaw();
            musica = 0;
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.reset();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public class Controller extends Binder {
        public Funcionalidade getMediaPlayer() {
            return MyServicePlay.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return controle;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SCRIPT","onStartCommand");
        mediaPlayer.reset();
        iniciarPlayer();
        return (super.onStartCommand(intent, flags, startId));
    }

    public void iniciarPlayer() {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), musicas.get(musica));
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopMedia();
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int pergunta, int i1) {

        switch (pergunta) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    public void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stopMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }


    public List<Integer> listRaw() throws IllegalAccessException {
        Field[] fields = R.raw.class.getFields();
        List<Integer> idsMusicas = new ArrayList<>();

        for (int count = 0; count < fields.length; count++) {
            idsMusicas.add(fields[count].getInt(fields[count]));
        }

        return idsMusicas;
    }

    @Override
    public void proximo() {
        //Log.i("APP", "proximo()");
        stopMedia();
        if (!mediaPlayer.isPlaying()) {
            //Log.i("APP", "Valor Musica: "+ musica);
            musica++;
            //Log.i("APP", "Valor Musica: "+ musica);
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), musicas.get(musica));
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void anterior() {
        stopMedia();
        if (!mediaPlayer.isPlaying()) {
            musica--;
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), musicas.get(musica));
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pausar() {

    }

    @Override
    public int getSizeListMusic() {
        return musicas.size();
    }

    @Override
    public int getIdentificadorMusica() {
        return musica;
    }
}
