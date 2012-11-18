package pl.edu.pk.kni.mobile.wifihertz;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class WyborMapy extends Activity {

	public static String SCIEZKA_DO_MAPY = "pl.edu.pk.kni.mobile.wifiHertz.wyborMapy.map_path";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        String zalogowanyJako = intent.getStringExtra(StronaLogowania.USER_ID);
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Zalogowano");
        alert.setMessage("Zalogowano jako: "+zalogowanyJako);
        alert.show();
        setContentView(R.layout.activity_wybor_mapy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wybor_mapy, menu);
        return true;
    }
    
    public void wyswietlMape(View view){
    	String sciezkaDoMapy = new String();
    	sciezkaDoMapy = "sciezka do mapy";
    	Intent intent = new Intent(this, EkranPomiaru.class);
    	intent.putExtra(SCIEZKA_DO_MAPY, sciezkaDoMapy);
    	startActivity(intent);
    	
    }
}
