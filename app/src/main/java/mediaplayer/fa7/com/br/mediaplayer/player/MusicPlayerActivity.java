package mediaplayer.fa7.com.br.mediaplayer.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;

import mediaplayer.fa7.com.br.mediaplayer.R;

public class MusicPlayerActivity extends Activity implements View.OnClickListener   {

    private Context mContext;

    //Layouts.
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawerParentLayout;
    private RelativeLayout mCurrentQueueLayout;

    //Song info/seekbar elements.
    private SeekBar mSeekbar;
    private ProgressBar mStreamingProgressBar;

    //Playback Controls.
    private RelativeLayout mControlsLayoutHeaderParent;
    private RelativeLayout mControlsLayoutHeader;
    private ImageButton mPlayPauseButton;
    private RelativeLayout mPlayPauseButtonBackground;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private ImageButton mShuffleButton;
    private ImageButton mRepeatButton;

    //Seekbar indicator.
    private RelativeLayout mSeekbarIndicatorLayoutParent;
    private RelativeLayout mSeekbarIndicatorLayout;
    private TextView mSeekbarIndicatorText;

    //Seekbar strobe effect.
    private AlphaAnimation mSeekbarStrobeAnim;
    private static final int SEEKBAR_STROBE_ANIM_REPEAT = Animation.INFINITE;



    //Handler object.
    private Handler mHandler = new Handler();

    //Differentiates between a user's scroll input and a programmatic scroll.
    private boolean USER_SCROLL = true;

    //HashMap that passes on song information to the "Download from Cloud" dialog.
    private HashMap<String, String> metadata;

    //Interface instance and flags.
    //private NowPlayingActivityListener mNowPlayingActivityListener;

    public static final String START_SERVICE = "StartService";
    private boolean mIsCreating = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //audioCapabilitiesReceiver.unregister();
        //releasePlayer();
    }

    // OnClickListener methods

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            preparePlayer(true);
        } else {

        }
    }

    // Permission management methods



    private void preparePlayer(boolean playWhenReady) {

    }


    // User controls

//    private boolean haveTracks(int type) {
//        return controller != null && controller.getTrackCount(type) > 0;
//    }


    private boolean onTrackItemClick(MenuItem item, int type) {
      //type  if (controller == null || item.getGroupId() != MENU_GROUP_TRACKS) {
      //      return false;
      //  }false
        //controller.setSelectedTrack(type, item.getItemId() - ID_OFFSET);
        return true;
    }

    private static final class KeyCompatibleMediaController extends MediaController {

        private MediaController.MediaPlayerControl playerControl;

        public KeyCompatibleMediaController(Context context) {
            super(context);
        }

        @Override
        public void setMediaPlayer(MediaController.MediaPlayerControl playerControl) {
            super.setMediaPlayer(playerControl);
            this.playerControl = playerControl;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            int keyCode = event.getKeyCode();
            if (playerControl.canSeekForward() && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() + 15000); // milliseconds
                    show();
                }
                return true;
            } else if (playerControl.canSeekBackward() && keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() - 5000); // milliseconds
                    show();
                }
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
    }
}
