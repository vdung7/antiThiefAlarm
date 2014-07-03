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

import vn.cybersoft.atalarm.Preferences;
import vn.cybersoft.atalarm.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class EnterPINActivity extends Activity {
	private EditText editPIN;
	private EditText confirmPIN;
	private Button btnOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_enter_pin);

		btnOK = (Button) findViewById(R.id.btn_ok);
		editPIN = (EditText) findViewById(R.id.edit_pin);	
		confirmPIN = (EditText) findViewById(R.id.confirm_pin);	

		confirmPIN.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					verifyPIN();
				}
				return false;
			}
		});

		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				verifyPIN();
			}
		});
	}

	private void verifyPIN() {
		String newPIN = editPIN.getText().toString();
		String confirm = confirmPIN.getText().toString();
		
		// 1st step: two PIN value is match ?
		if (newPIN.equalsIgnoreCase(confirm)) {
			Preferences.getInstance().savePIN(newPIN);
			EnterPINActivity.this.finish();
		} else {
			Toast.makeText(EnterPINActivity.this,
					EnterPINActivity.this.getString(R.string.unmatch_pin),
					Toast.LENGTH_LONG).show();
			editPIN.requestFocus();
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		EnterPINActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
