package mediaplayer.fa7.com.br.mediaplayer.player;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mediaplayer.fa7.com.br.mediaplayer.Funcionalidade;
import mediaplayer.fa7.com.br.mediaplayer.Media;
import mediaplayer.fa7.com.br.mediaplayer.MyServicePlay;
import mediaplayer.fa7.com.br.mediaplayer.R;

public class MediaListActivity extends Activity {

    private Intent serviceIntent;
    private MyServicePlay playSrv;
    private boolean isBinded;
    ListView mediaList;
    List<Media> localMedia;

    //connect to the service
    private ServiceConnection mediaConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyServicePlay.ServiceBinder binder = (MyServicePlay.ServiceBinder) service;
            //get service
            playSrv = binder.getMediaPlayer();
            //pass list
            playSrv.setList(localMedia);
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        mediaList = (ListView) findViewById(R.id.sample_list);

        try {
            localMedia = getListMusic();
            serviceIntent = new Intent(this, MyServicePlay.class);
            bindService(serviceIntent, mediaConnection, Context.BIND_AUTO_CREATE);
            startService(serviceIntent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " +
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

     @Override
    public void onStart() {
        super.onStart();
        final MediaAdapter mediaAdapter = new MediaAdapter(this);
        //carrego a lista de musicas do dispositivo

        for (int i = 0; i < localMedia.size(); i++) {
            mediaAdapter.addAll(localMedia.get(i));
        }

        mediaList.setAdapter(mediaAdapter);
        mediaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = mediaAdapter.getItem(position);
                if (item instanceof Media) {
                    onMediaSelected((Media) item);
                }
            }
        });
    }

    private void onMediaSelected(Media sample) {
//        Intent mpdIntent = new Intent(this, MusicPlayerActivity.class)
//                .setData(sample.uri)
//                .putExtra(MusicPlayerActivity.CONTENT_ID_EXTRA, sample.contentId)
//                .putExtra(MusicPlayerActivity.PROVIDER_EXTRA, sample.provider);
//        startActivity(mpdIntent);
    }

    public List<Media> getListMusic() {
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA};

        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);

        List<Media> listPlayer = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            //String provider = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.));
            Long idMusica = Long.parseLong(id);
            Uri uri = getURI(idMusica);

            listPlayer.add(new Media(idMusica, name, uri));
        }
        cursor.close();

        return listPlayer;
    }

    public Uri getURI(Long id) {
        return ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }


    private static class MediaAdapter extends ArrayAdapter<Media> {

        public MediaAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                int layoutId = getItemViewType(position) == 1 ? android.R.layout.simple_list_item_1
                        : android.R.layout.simple_list_item_1;
                //: R.layout.sample_chooser_inline_header;
                view = LayoutInflater.from(getContext()).inflate(layoutId, null, false);
            }
            Object item = getItem(position);
            String name = null;
            if (item instanceof Media) {
                name = ((Media) item).name;
            } else if (item instanceof Header) {
                name = ((Header) item).name;
            }
            ((TextView) view).setText(name);
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return (getItem(position) instanceof Media) ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    private static class Header {

        public final String name;

        public Header(String name) {
            this.name = name;
        }
    }
}
