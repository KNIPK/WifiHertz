package pl.edu.pk.kni.mobile.wifihertz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WyborMapy extends ListActivity {

	public static String SCIEZKA_DO_MAPY = "pl.edu.pk.kni.mobile.wifiHertz.wyborMapy.map_path";
	int zalogowanyJako;
	ArrayList<InformacjeOmapie> listaMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		zalogowanyJako = Integer.valueOf(intent
				.getStringExtra(StronaLogowania.USER_ID));

		
		listaMap = new ArrayList<InformacjeOmapie>();
		pobierzListeMap();
		
		setListAdapter(new ArrayAdapter<InformacjeOmapie>(this,R.layout.lista_map, listaMap));
		ListView lista = getListView();
		
		//setContentView(R.layout.activity_wybor_mapy);
	}
	
	
	
	private void pobierzListeMap(){
		
    	String URL = "http://wifihertz.kalinowski.net.pl/userImages"; 

    	URL +=","+zalogowanyJako;
    	
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
				String[] linijkiDoPrzetworzenia = out.toString().split("<br />");
				for (int i = 0; i < linijkiDoPrzetworzenia.length; i++) {
					String[] podzialLinijki = linijkiDoPrzetworzenia[i].split(" ");
					InformacjeOmapie nowaMapa = new InformacjeOmapie(podzialLinijki[1], Integer.valueOf(podzialLinijki[0]), podzialLinijki[2]);
					listaMap.add(nowaMapa);
					
				}
				
			}
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_wybor_mapy, menu);
		return true;
	}

	public void wyswietlMape(View view) {
		String sciezkaDoMapy = new String();
		sciezkaDoMapy = "sciezka do mapy";
		Intent intent = new Intent(this, EkranPomiaru.class);
		intent.putExtra(SCIEZKA_DO_MAPY, sciezkaDoMapy);
		startActivity(intent);

	}
}
