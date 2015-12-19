package mediaplayer.fa7.com.br.mediaplayer;

import android.net.Uri;

import java.util.Locale;

/**
 * Created by Caio on 18/12/2015.
 */
public class Media {

    public final Long id;
    public final String name;
    public final String contentId;
    public final String provider;
    public final Uri uri;

    public Media(Long id,String name, Uri uri) {
        this(id,name, name.toLowerCase(Locale.US).replaceAll("\\s", ""), "", uri);
    }

    public Media(Long id,String name, String contentId, String provider, Uri uri) {
        this.id = id;
        this.name = name;
        this.contentId = contentId;
        this.provider = provider;
        this.uri = uri;
    }
}
