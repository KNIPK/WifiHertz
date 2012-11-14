package pl.edu.pk.kni.mobile.wifihertz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WyborMapy extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybor_mapy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wybor_mapy, menu);
        return true;
    }
}
