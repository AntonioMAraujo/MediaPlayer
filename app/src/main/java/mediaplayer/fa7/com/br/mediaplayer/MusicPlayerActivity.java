package mediaplayer.fa7.com.br.mediaplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URI;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity implements ServiceConnection{

    private MyServicePlay.Controller controle;

    private Funcionalidade funcionalidades;

    private ListView listMusics;

    private Toolbar toolbar;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listMusics = (ListView) findViewById(R.id.list_musics);

        listMusics.setAdapter(new ArrayAdapter<String>(MusicPlayerActivity.this, android.R.layout.simple_list_item_1, obterListaMusicas()));

        listMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MusicPlayerActivity.this, "Item: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.btnPlayList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicPlayerActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<String> obterListaMusicas(){
        List<String> nomes = new ArrayList<String>();
        List<Uri> musicas = funcionalidades.getListMusic();
        for(Uri musica : musicas){
            nomes.add("Música 1");
        }
        return  nomes;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        controle = (MyServicePlay.Controller) service;
        funcionalidades = controle.getMediaPlayer();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
