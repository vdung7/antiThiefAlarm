/*******************************************************************************
 * Copyright 2014 VietDung Vu, IUH.CyberSoft Team (http://cyberso.wordpress.com/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package vn.cybersoft.atalarm;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

/**
 * @author vietdung
 *
 */
public class AlertPlayer {
	private AudioManager audioManager;
	private MediaPlayer mMediaPlayer;
	private static AlertPlayer alertPlayer;

	private AlertPlayer() {
		mMediaPlayer = new MediaPlayer();
	}

	public static AlertPlayer getInstance() {
		if (alertPlayer==null) {
			alertPlayer = new AlertPlayer();
		}
		return alertPlayer;
	}

	public void start(Context context) {
		if (mMediaPlayer!=null) {
			if (mMediaPlayer.isPlaying()) {
				return;
			}
		}

		try {
			Uri alert = Constants.ALARM_SOUND;
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, alert);
			if (audioManager == null) {
				audioManager = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
			}
			if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
				// Waking up mobile if it is sleeping
				WakeLocker.acquire(context);
				
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				
				// Releasing wake lock
				// WakeLocker.release();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Has error when make alert sound",
					Toast.LENGTH_LONG).show();
		}
	}

	public void stop() {
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
		}
	}
	
	public boolean isPlaying() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.isPlaying();
		} 
		return false;
	}

}
