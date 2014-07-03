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
package vn.cybersoft.atalarm.receivers;

import vn.cybersoft.atalarm.AlertPlayer;
import vn.cybersoft.atalarm.Preferences;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerConnectReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Preferences.getInstance().isPluginLock()) {
			boolean isDisconnect = intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
			if (isDisconnect) {
				AlertPlayer.getInstance().start(context);
			}
		}
	}
	
}
