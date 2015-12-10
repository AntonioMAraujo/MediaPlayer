package mediaplayer.fa7.com.br.mediaplayer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.app.Activity;

public abstract class BaseActivity {

    private MediaBrowser mMediaBrowser;

    protected void onMediaControllerConnected() {
        //deve ser implementado pelos clientes
    }

    protected void showPlaybackControls() {

    }
    protected void hidePlaybackControls() {

    }

    protected boolean shouldShowControls() {
        //MediaController mediaController = getMediaController();
        //if (mediaController == null ||
          //      mediaController.getMetadata() == null ||
            //    mediaController.getPlaybackState() == null) {
            //return false;
        //}
        //switch (mediaController.getPlaybackState().getState()) {
          //  case PlaybackState.STATE_ERROR:
           // case PlaybackState.STATE_NONE:
            //case PlaybackState.STATE_STOPPED:
              //  return false;
            //default:
            //    return true;
        //}
        return false;
   }

    private void connectToSession(MediaSession.Token token) {
        //MediaController mediaController = new MediaController(this, token);
        //setMediaController(mediaController);
        //mediaController.registerCallback(mMediaControllerCallback);

        if (shouldShowControls()) {
            showPlaybackControls();
        } else {
            hidePlaybackControls();
        }

        onMediaControllerConnected();
    }

    private final MediaController.Callback mMediaControllerCallback =
            new MediaController.Callback() {
                @Override
                public void onPlaybackStateChanged(PlaybackState state) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        hidePlaybackControls();
                    }
                }

                @Override
                public void onMetadataChanged(MediaMetadata metadata) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        hidePlaybackControls();
                    }
                }
            };

    private final MediaBrowser.ConnectionCallback mConnectionCallback =
            new MediaBrowser.ConnectionCallback() {
                @Override
                public void onConnected() {
                    connectToSession(mMediaBrowser.getSessionToken());
                }
            };
}