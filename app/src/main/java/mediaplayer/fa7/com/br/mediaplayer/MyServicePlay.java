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
    private List<Uri> listPlayer = new ArrayList<Uri>();
    private int musica = 0;

    public void onCreate() {
        Log.i("SCRIPT", "onCreate()");
        listPlayer = obterAudioCelular();
        musica = 0;
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.reset();
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
        Log.i("SCRIPT", "onStartCommand");
        mediaPlayer.reset();
        iniciarPlayer();
        return (super.onStartCommand(intent, flags, startId));
    }

    public void iniciarPlayer() {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), listPlayer.get(musica));
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

    @Override
    public void proximo() {
        stopMedia();
        if (!mediaPlayer.isPlaying()) {
            musica++;
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), listPlayer.get(musica));
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
                mediaPlayer = MediaPlayer.create(getApplicationContext(), listPlayer.get(musica));
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }


    public List<Uri> obterAudioCelular() {
        String[] projection = new String[]{MediaStore.Audio.Media._ID};

        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);

        listPlayer = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            Long idMusica = Long.parseLong(id);
            listPlayer.add(getURI(idMusica));
        }
        cursor.close();

        return listPlayer;
    }


    public Uri getURI(Long id) {
        return ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
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
