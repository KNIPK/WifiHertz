package pl.edu.pk.kni.mobile.wifihertz;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;

public class WyborMapy extends Activity {

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
}
