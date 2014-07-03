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
package vn.cybersoft.atalarm.services;

import vn.cybersoft.atalarm.AlertPlayer;
import vn.cybersoft.atalarm.Preferences;
import vn.cybersoft.atalarm.R;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author vietdung
 *
 */
public class MovingLockService extends Service {
	private static final String t = "MovingLockService";
	private static final int FOREGROUND_NTF_ID = 1951;

	private SensorManager sensorManager;
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				getAccelerometer(event);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// nothing to do here
		}

		private void getAccelerometer(SensorEvent event) {
			long lastUpdate = Preferences.getInstance().getLastUpdate();
			float[] values = event.values;
			// Movement
			float x = values[0];
			float y = values[1];
			float z = values[2];

			float accelationSquareRoot = (x * x + y * y + z * z)
					/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
			long actualTime = event.timestamp;
			if (accelationSquareRoot >= 1.1) //
			{
				if (actualTime - lastUpdate < 2000) {
					return;
				}
				AlertPlayer.getInstance()
				.start(Preferences.getInstance().getApplicationContext());
				Preferences.getInstance().saveLastUpdate(actualTime);
			}
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(t, "moving locker has been started");

		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManager.registerListener(sensorEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		Preferences.getInstance().setMovingLock(true);

		// make foreground service
		RemoteViews ntfContentView = 
				new RemoteViews(getPackageName(), R.layout.notification_bar_layout);
		Notification notification = new Notification();
		notification.icon = R.drawable.bell;
		notification.contentView = ntfContentView;
		startForeground(FOREGROUND_NTF_ID, notification);
		
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(sensorEventListener);
		Log.i(t, "moving locker has been stopped");
	}

}
