package es.alvsanand.Blocks;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LyricsActivity extends Activity {
	private static String TAG = "3Blocks";

	private MediaPlayer mMediaPlayer;

	private AlertDialog alertDialog;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_main_about:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.text_about_dialog).setCancelable(false);
			alertDialog = builder.create();

			alertDialog.show();

			alertDialog.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						dialog.dismiss();

						return true;
					}

					return false;
				}
			});

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void playHandler(final View view) {
		switch (view.getId()) {
		case R.id.layout_main_button_player:
			if (mMediaPlayer == null) {
				Log.i(TAG, "Playback initialized");

				Button button = (Button) findViewById(view.getId());
				button.setText(R.string.button_pause);

				try {
					AssetFileDescriptor assetFileDescriptor = getAssets().openFd("music.mp3");

					mMediaPlayer = new MediaPlayer();
					mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(),
							assetFileDescriptor.getLength());
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				} catch (IllegalArgumentException e) {
					Log.e(TAG, "Error initialiting playback", e);
				} catch (IllegalStateException e) {
					Log.e(TAG, "Error initialiting playback", e);
				} catch (IOException e) {
					Log.e(TAG, "Error initialiting playback", e);
				}

				mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						Log.i(TAG, "Playback completed");

						Button button = (Button) findViewById(view.getId());
						button.setText(R.string.button_play);

						try {
							mMediaPlayer.seekTo(0);
						} catch (IllegalStateException e) {
							Log.e(TAG, "Error initialiting playback", e);
						}
					}
				});
			} else {
				Button button = (Button) findViewById(view.getId());

				if (button.getText().equals(getText(R.string.button_pause))) {
					Log.i(TAG, "Playback paused");

					button.setText(R.string.button_resume);

					mMediaPlayer.pause();
				} else {
					if (button.getText().equals(getText(R.string.button_resume))) {
						Log.i(TAG, "Playback resumed");
						button.setText(R.string.button_pause);

						mMediaPlayer.start();
					} else {
						Log.i(TAG, "Playback started");

						button.setText(R.string.button_pause);

						mMediaPlayer.start();
					}
				}
			}

			break;
		}
	}
}
