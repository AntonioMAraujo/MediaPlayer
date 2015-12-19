package mediaplayer.fa7.com.br.mediaplayer.player;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import mediaplayer.fa7.com.br.mediaplayer.Funcionalidade;
import mediaplayer.fa7.com.br.mediaplayer.Media;
import mediaplayer.fa7.com.br.mediaplayer.MyServicePlay;
import mediaplayer.fa7.com.br.mediaplayer.R;

public class MediaListActivity extends Activity {

    private MyServicePlay controle;
    private ServiceConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        ListView sampleList = (ListView) findViewById(R.id.sample_list);
        final MediaAdapter mediaAdapter = new MediaAdapter(this);

        //carrego a lista de musicas do dispositivo
        //List<Media> musicas =


        sampleList.setAdapter(mediaAdapter);
        sampleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        Intent mpdIntent = new Intent(this, MusicPlayerActivity.class)
                .setData(sample.uri)
                .putExtra(MusicPlayerActivity.CONTENT_ID_EXTRA, sample.contentId)
                .putExtra(MusicPlayerActivity.CONTENT_TYPE_EXTRA, sample.type)
                .putExtra(MusicPlayerActivity.PROVIDER_EXTRA, sample.provider);
        startActivity(mpdIntent);
    }

    private static class  MediaAdapter extends ArrayAdapter<Object> {

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
