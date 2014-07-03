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

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

public class Preferences extends Application {
	// Preferences Constants
	private final String PREFERENCES_NAME = "preferences";
	private final String MOVING_LOCK = "moving";
	private final String PLUGIN_LOCK = "plugin";
	private final String LAST_UPDATE = "lstUdt";
	private final String PIN = "pinpin";

	private SharedPreferences preferences;
	private static Preferences pre;

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate() {
		super.onCreate();
		pre = this;
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		
		// default PIN - for testing
		// savePIN("1234");
	}

	/**
	 * Get an instance of Preferences
	 * 
	 * @return
	 */
	public static Preferences getInstance() {
		return pre;
	}

	/**
	 * Remove all store info
	 */
	public void removeAll() {
		Thread.setDefaultUncaughtExceptionHandler(null);
		System.gc();
	}

	/**
	 * Set moving lock
	 * 
	 */
	public void setMovingLock(boolean lock) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(MOVING_LOCK, lock);
		editor.commit();
	}

	/**
	 * check moving lock
	 * 
	 * @return false if not exist
	 */
	public boolean isMovingLock() {
		return preferences.getBoolean(MOVING_LOCK, false);
	}
	
	/**
	 * Set plug in lock
	 * 
	 */
	public void setPluginLock(boolean lock) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PLUGIN_LOCK, lock);
		editor.commit();
	}

	/**
	 * check plug in lock
	 * 
	 * @return false if not exist
	 */
	public boolean isPluginLock() {
		return preferences.getBoolean(PLUGIN_LOCK, false);
	}
	
	/**
	 * Save last update to preferences
	 * @param lastUpdate
	 */
	public void saveLastUpdate(long lastUpdate) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(LAST_UPDATE, lastUpdate);
		editor.commit();
	}
	
	/**
	 * Get last update from preferences
	 * @return current time if not exist
	 */
	public long getLastUpdate() {
		return preferences.getLong(LAST_UPDATE, System.currentTimeMillis());
	}
	
	/**
	 * Save PIN to preference
	 * 
	 * @param pin
	 */
	public void savePIN(String pin) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PIN, pin);
		editor.commit();
	}

	/**
	 * Get PIN from preference
	 * 
	 * @return null if not exist
	 */
	public String getPIN() {
		return preferences.getString(PIN, null);
	}
}
