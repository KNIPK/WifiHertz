package pl.edu.pk.kni.mobile.wifihertz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WyborMapy extends ListActivity implements OnItemClickListener {

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

		setListAdapter(new ArrayAdapter<InformacjeOmapie>(this,
				R.layout.lista_map, listaMap));
		ListView lista = getListView();
		lista.setOnItemClickListener(this);

		// setContentView(R.layout.activity_wybor_mapy);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//this.deleteDatabase("baza.db");
		(new Baza(this)).synchronizuj();
		//startActivity(new Intent(this, EkranSynchronizacji.class));
		return true;
	}

	private void pobierzListeMap() {

		String URL = "http://wifihertz.kalinowski.net.pl/userImages";

		URL += "," + zalogowanyJako;

		System.out.println(URL);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpclient.execute(new HttpGet(URL));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String[] linijkiDoPrzetworzenia = out.toString()
						.split("<br />");
				for (int i = 0; i < linijkiDoPrzetworzenia.length; i++) {
					String[] podzialLinijki = linijkiDoPrzetworzenia[i]
							.split(" ");
					InformacjeOmapie nowaMapa = new InformacjeOmapie(
							podzialLinijki[1],
							Integer.valueOf(podzialLinijki[0]),
							podzialLinijki[2]);
					listaMap.add(nowaMapa);

				}

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View v, int index, long l) {
		String nazwa = listaMap.get(index).getNazwa();
		String adres = listaMap.get(index).getAdresBitmapy();
		String idObrazka = Integer.toString(listaMap.get(index).getId());

		Intent intent = new Intent(this, EkranPomiaru.class);

		intent.putExtra("nazwa", nazwa);
		intent.putExtra("adres", adres);
		intent.putExtra("idObrazka", idObrazka);
		intent.putExtra("idUsera", Integer.toString(zalogowanyJako));

		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_wybor_mapy, menu);
		return true;
	}

	public void wyswietlMape(View view) {
		// String sciezkaDoMapy = new String();
		// sciezkaDoMapy = "sciezka do mapy";
		// Intent intent = new Intent(this, EkranPomiaru.class);
		// intent.putExtra(SCIEZKA_DO_MAPY, sciezkaDoMapy);
		// startActivity(intent);

	}
}
