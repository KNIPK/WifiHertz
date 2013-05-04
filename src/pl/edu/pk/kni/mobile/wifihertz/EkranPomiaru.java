package pl.edu.pk.kni.mobile.wifihertz;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class EkranPomiaru extends Activity implements OnTouchListener {

	ArrayList<PointF> listaPunktow;
	Bitmap bitmapaMapy;
	WifiManager wifi;
	private Mapa mapa;
	float polozenieMapyX;
	float polozenieMapyY;
	float polozenieZnacznikaX;
	float polozenieZnacznikaY;
	int fps;
	InformacjeOmapie informacjeOmapie;
	int zalogowanyJako;
	int id_obrazka;
	float wcisniecieX;
	float wcisniecieY;

	Baza bazaPunktow;
	WifiReceiver wifiRec = null;
	
	public boolean onTouch(View v, MotionEvent event) {

		// żeby nie zażynać sprzętu
		try {
			Thread.sleep(1000 / fps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			polozenieMapyX += event.getX() - wcisniecieX;
			polozenieMapyY += event.getY() - wcisniecieY;
			wcisniecieX = event.getX();
			wcisniecieY = event.getY();
			break;

		case MotionEvent.ACTION_DOWN:
			polozenieZnacznikaX = event.getX() - polozenieMapyX;
			polozenieZnacznikaY = event.getY() - polozenieMapyY;

			wcisniecieX = event.getX();
			wcisniecieY = event.getY();

		case MotionEvent.ACTION_UP:
			polozenieMapyX += event.getX() - wcisniecieX;
			polozenieMapyY += event.getY() - wcisniecieY;
			break;

		default:
			// return super.onTouchEvent(event);

		}

		return true;
	}

	private void dodajPunkt() {
		wifiRec.zrobPomiarWPunkcie(id_obrazka, polozenieZnacznikaX, polozenieZnacznikaY);
		System.out.println("Zbadano sieć w: "+polozenieZnacznikaX+" "+polozenieZnacznikaY);
		listaPunktow.add(new PointF(polozenieZnacznikaX, polozenieZnacznikaY));
		
		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings_pobranie_mapy:
			pobierzMapeZserwera();
			break;
		case R.id.menu_settings_pomiar:
			Log.d(INPUT_METHOD_SERVICE, "Pobrano parametry sieci");
			dodajPunkt();

			break;

		default:
			break;
		}

		return true;
	}

	private void pobierzMapeZserwera() {
		try {
			URL url = new URL(informacjeOmapie.getAdresBitmapy());
			URLConnection conn = url.openConnection();
			bitmapaMapy = BitmapFactory.decodeStream(conn.getInputStream());

		} catch (Exception e) {

			e.printStackTrace();
		}
		FileOutputStream out;
		try {
			File plik = new File(Environment.getExternalStorageDirectory()
					.toString(), informacjeOmapie.getId() + ".png");
			out = new FileOutputStream(plik);
			bitmapaMapy.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (FileNotFoundException e) {
			AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setMessage("Nie udało się zapisać pliku.");
			alert.show();
			e.printStackTrace();
		}

		System.out.println("Pobrano plik z serwera");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String nazwa = getIntent().getStringExtra("nazwa");
		String adres = getIntent().getStringExtra("adres");
		zalogowanyJako = Integer.valueOf(getIntent().getStringExtra("idUsera"));

		bazaPunktow = new Baza(this);

		id_obrazka = Integer.valueOf(getIntent()
				.getStringExtra("idObrazka"));
		
		
		
		
		informacjeOmapie = new InformacjeOmapie(adres, id_obrazka, nazwa);
		zaladujBitmape();
		mapa = new Mapa(this);
		setContentView(R.layout.activity_ekran_pomiaru);

		setContentView(mapa);
		listaPunktow = new ArrayList<PointF>();
		
		/*zsynchronizujemy dane z tymi, ktore sa na serwerze*/
		pobierzZSerwera();
		
		zaladujPunktyZbazy();
		mapa.setOnTouchListener(this);
		fps = 20;

		if(wifiRec == null)
		{
			wifiRec = new WifiReceiver(this);
		}
		
		
		
	    wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    
        registerReceiver(wifiRec, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //wifi.startScan();
	}

	private void zaladujBitmape() {
		// bitmapaMapy =
		// BitmapFactory.decodeResource(getResources(),R.drawable.plan);

		File plik = new File(Environment.getExternalStorageDirectory()
				.toString(), informacjeOmapie.getId() + ".png");
		if (!plik.exists())
			pobierzMapeZserwera();

		bitmapaMapy = BitmapFactory.decodeFile(plik.getAbsolutePath());
		System.out.println("Otworzono mapę.");

	}

	private void zaladujPunktyZbazy() {
		Cursor c = bazaPunktow.pobierzDane(informacjeOmapie.getId());
		c.moveToFirst();
		float x,y;
		if(c.getCount()>0)
			do{
				x = c.getFloat(c.getColumnIndex("positionX"));
				y = c.getFloat(c.getColumnIndex("positionY"));
				listaPunktow.add(new PointF(x,y));
			}
			while(c.moveToNext());
		c.close();
	}
	
	private void pobierzZSerwera(){
		/*metoda pobiera punkty z serwera*/
		int i = 0;
		String positions = "";
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		ByteArrayOutputStream out;
		
		while(true){
			//tutaj synchro w 2 strone
			
			try {
				response = httpclient.execute(new HttpGet("http://wifihertz.kalinowski.net.pl/index.php?page=getOneData&imageId="+id_obrazka+"&nr="+i));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode()==HttpStatus.SC_OK){
					out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					
					positions = out.toString();
					System.out.println(out);
				}
			}
			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(positions.length()<3){
				break;
			}else{
				String [] positions2 = positions.split(" ");
				PointF p = new PointF(Float.valueOf(positions2[0]), Float.valueOf(positions2[1]));
				if(!listaPunktow.contains(p))
				{
					listaPunktow.add(p);
				}
				i++;
			}
		}
		
	}

	@Override
	protected void onPause() {
		//Toast.makeText(this, "Trwa synchronizacja. Cierpliwości...", Toast.LENGTH_LONG).show();
		//bazaPunktow.synchronizuj(id_obrazka);
		super.onPause();
		mapa.pause();
		
	}
	@Override
    public void onStop()
    {
    	super.onStop();
		unregisterReceiver(wifiRec);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		mapa.resume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_ekran_pomiaru, menu);

		return true;
	}

	class Mapa extends SurfaceView implements Runnable {
		Thread t = null;
		SurfaceHolder holder;
		Boolean dzialaj = false;
		Bitmap bitmapaPunktu;
		Bitmap bitmapaZnacznika;

		public Mapa(Context context) {
			super(context);
			holder = getHolder();

			bitmapaZnacznika = BitmapFactory.decodeResource(getResources(),
					R.drawable.znacznik);
			bitmapaPunktu = BitmapFactory.decodeResource(getResources(),
					R.drawable.punkt);
		}

		public void run() {
			while (dzialaj) {
				if (!holder.getSurface().isValid())
					continue;

				Canvas c = holder.lockCanvas();
				c.drawARGB(255, 255, 255, 200);
				c.drawBitmap(bitmapaMapy, polozenieMapyX, polozenieMapyY, null);
				for (int i = 0; i < listaPunktow.size(); i++) {
					float x = listaPunktow.get(i).x;
					float y = listaPunktow.get(i).y;
					c.drawBitmap(bitmapaPunktu, x - bitmapaPunktu.getWidth()
							/ 2 + polozenieMapyX, y - bitmapaPunktu.getWidth()
							/ 2 + polozenieMapyY, null);
				}
				c.drawBitmap(bitmapaZnacznika, polozenieZnacznikaX
						- bitmapaZnacznika.getWidth() / 2 + polozenieMapyX,
						polozenieZnacznikaY - bitmapaZnacznika.getWidth() / 2
								+ polozenieMapyY, null);

				holder.unlockCanvasAndPost(c);

			}

		}

		public void resume() {
			dzialaj = true;
			t = new Thread(this);
			t.start();
		}
		
		public void pause() {
			dzialaj = false;
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

	}

}
