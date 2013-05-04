package pl.edu.pk.kni.mobile.wifihertz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ProgressBar;

public class EkranSynchronizacji extends Activity {

	ProgressBar mProgress;
	final Activity activity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ekran_synchronizacji);
		mProgress = (ProgressBar) findViewById(R.id.progressBar1);
		mProgress.setProgress(0);;
		(new Baza(activity)).synchronizuj();

		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ekran_synchronizacji, menu);
		return true;
	}

}
