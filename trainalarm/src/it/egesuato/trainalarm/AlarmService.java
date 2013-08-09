package it.egesuato.trainalarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service implements Runnable {

	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
