package pl.edu.pk.kni.mobile.wifihertz;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class StronaLogowania extends Activity {

    public static final String USER_ID = "pl.edu.pk.kni.mobile.wifiHertz.stronaLogowania.user_id";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strona_logowania);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public void zaloguj(View view){
    	String idUzytkownika = new String();
    	idUzytkownika = Integer.toString(new Random().nextInt());
    	if(!idUzytkownika.equals("0")){//warunek zalogowania
    		Intent intent = new Intent(this, WyborMapy.class);
        	intent.putExtra(USER_ID, idUzytkownika);
        	startActivity(intent);
    	}
    }
    
}
