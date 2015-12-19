package mediaplayer.fa7.com.br.mediaplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 28/11/2015.
 */
public class MyServicePlay extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener,Funcionalidade{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Media> listPlayer = new ArrayList<Media>();
    private final IBinder serviceBinder= new ServiceBinder();
    private int musica = 0;
    private boolean serviceStarted = false;
    private boolean servicePausado = false;
    private boolean backgrounded;

    public void onCreate() {
        Log.i("SCRIPT", "onCreate()");
        serviceStarted = true;
        listPlayer = getListMusic();
        musica = 0;
        iniciarPlayer();
    }

    public void iniciarPlayer() {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setOnSeekCompleteListener(this);
                mediaPlayer.setOnInfoListener(this);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    //pass song list
    public void setList(List<Media> medias){
        listPlayer=medias;
    }


    public class ServiceBinder extends Binder {
        public MyServicePlay getMediaPlayer() {
            return MyServicePlay.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public boolean onUnbind(Intent intent){
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
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
            servicePausado = false;
        }
    }

    @Override
    public List<Media> getListMusic() {
        return null;
    }

    public void stopMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void proximo() {
        stopMedia();
        if (!mediaPlayer.isPlaying()) {
            musica++;
            try {
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), listPlayer.get(musica).uri);
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
                mediaPlayer = MediaPlayer.create(getApplicationContext(), listPlayer.get(musica).uri);
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getURI(Long id) {
        return ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }


    @Override
    public void pausar() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            servicePausado = true;
        }
    }

    @Override
    public int getSizeListMusic() {
        return listPlayer.size();
    }

    @Override
    public int getIdentificadorMusica() {
        return musica;
    }

    public class MusicBinder extends Binder {
        MyServicePlay getService() {
            return MyServicePlay.this;
        }
    }

}
