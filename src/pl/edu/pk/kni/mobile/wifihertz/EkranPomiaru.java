package pl.edu.pk.kni.mobile.wifihertz;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class EkranPomiaru extends Activity {
	  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekran_pomiaru);
    }

  
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ekran_pomiaru, menu);
    
        return true;
    }
}
