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
package vn.cybersoft.atalarm.controllers;

import vn.cybersoft.atalarm.AlertPlayer;
import vn.cybersoft.atalarm.Preferences;
import vn.cybersoft.atalarm.R;
import vn.cybersoft.atalarm.services.MovingLockService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class MainActivity extends Activity {
	private ToggleButton togglePluginLock; 
	private ToggleButton toggleMovingLock; 
	private TextView resetPin;
	private TextView about;
	private TextView github;
	private Button btnStop;
	
	// for advertisement
	private StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// for advertisement
		StartAppSDK.init(this, "108427450", "207484768");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StartAppAd.showSlider(this);
		
		// check PIN initialization
		if (Preferences.getInstance().getPIN()==null) {
			Intent intent = new Intent(MainActivity.this, EnterPINActivity.class);
			startActivity(intent);
		}
		
		togglePluginLock = (ToggleButton) findViewById(R.id.togglePlugin);
		toggleMovingLock = (ToggleButton) findViewById(R.id.toggleMoving);
		resetPin = (TextView) findViewById(R.id.reset);
		about = (TextView) findViewById(R.id.about);
		github = (TextView) findViewById(R.id.github);
		btnStop = (Button) findViewById(R.id.btn_stop);

		// get current values
		togglePluginLock.setChecked(Preferences.getInstance().isPluginLock());

		// set buttons listeners
		togglePluginLock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Preferences.getInstance().setPluginLock(isChecked);
			}
		});
		toggleMovingLock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent intent = new Intent(MainActivity.this,
						MovingLockService.class);
				if (isChecked) {
					startService(intent);
				} else {
					stopService(intent);
				}
				Preferences.getInstance().setMovingLock(isChecked);
			}
		});
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (AlertPlayer.getInstance().isPlaying()) {
					checkPINtoStop();
				}
			}
		});
		
		resetPin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkPINtoReset();
			}
		});
		
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog;
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
				.setTitle(R.string.about);
				final FrameLayout frameView = new FrameLayout(MainActivity.this);
				builder.setView(frameView);
				builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alertDialog = builder.create();
				LayoutInflater inflater = alertDialog.getLayoutInflater();
				View dialoglayout;
				dialoglayout = inflater.inflate(R.layout.about_dialog, frameView);
				
				TextView version = (TextView) dialoglayout
						.findViewById(R.id.version);
				try {
					String versionName = MainActivity.this.getPackageManager()
							.getPackageInfo(MainActivity.this.getPackageName(),
									0).versionName;
					version.setText("Version " + versionName);
				} catch (NameNotFoundException e) {
					version.setText("Unknown version");
				}
				alertDialog.show();
			}
		});
		
		github.setText( Html.fromHtml("<a href=\"https://github.com/vdung7/antiThiefAlarm\">" +
				getString(R.string.github)+
				"</a>"));
		github.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void checkPINtoStop() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getString(R.string.enter_pin));
		final EditText input = new EditText(this);
		input.setHint("PIN");
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(input);

		builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String pinIn = input.getText().toString();

				if (pinIn.equalsIgnoreCase(Preferences.getInstance().getPIN())) {
					AlertPlayer.getInstance().stop();
					if (Preferences.getInstance().isMovingLock()) {
						Intent intent = new Intent(MainActivity.this, MovingLockService.class);
						stopService(intent);
						toggleMovingLock.setChecked(false);
						Preferences.getInstance().setMovingLock(false);
					}
				} else {
					Toast.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.invalid_pin),
							Toast.LENGTH_LONG).show();
				}

			}
		});

		builder.show();
	}

	private void checkPINtoReset() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getString(R.string.old_pin));
		final EditText input = new EditText(this);
		input.setHint("PIN");
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(input);

		builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String pinIn = input.getText().toString();

				if (pinIn.equalsIgnoreCase(Preferences.getInstance().getPIN())) {
					Intent intent = new Intent(MainActivity.this, EnterPINActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.invalid_pin),
							Toast.LENGTH_LONG).show();
				}

			}
		});

		builder.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
	    startAppAd.onBackPressed();
	    super.onBackPressed();
	}
}
