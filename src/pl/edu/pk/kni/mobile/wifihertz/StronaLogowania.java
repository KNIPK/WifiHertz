package pl.edu.pk.kni.mobile.wifihertz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
    	String idUzytkownika = new String("0");
    	String URL = getResources().getString(R.string.urlLogowania); 
    	
    	String login = ((EditText)findViewById(R.id.editText2)).getText().toString();
    	String pass =  ((EditText)findViewById(R.id.editText1)).getText().toString();
    	URL +=","+login+","+pass;
    	
    	System.out.println(URL);
    	
    	
    	HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpclient.execute(new HttpGet(URL));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode()==HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				idUzytkownika = out.toString();
			}
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
  
    	
    	
    	
    	
    	//idUzytkownika = Integer.toString(new Random().nextInt());
    	if(!idUzytkownika.equals("0")){//warunek zalogowania
    		Intent intent = new Intent(this, WyborMapy.class);
        	intent.putExtra(USER_ID, idUzytkownika);
        	startActivity(intent);
    	}
    	else{
    		 AlertDialog alert = new AlertDialog.Builder(this).create();
	        alert.setTitle("Nieudane logowanie");
	        alert.setMessage("Nie udał się zalogować. Sprawdź połączenie z internetem, login oraz hasło.");
	        alert.show();
    	}
    }
    
}
