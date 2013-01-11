package pl.edu.pk.kni.mobile.wifihertz;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

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

	public boolean onTouch(View v, MotionEvent event) {

		// żeby nie zażynać sprzętu
		try {
			Thread.sleep(1000 / fps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			polozenieZnacznikaX = event.getX();
			polozenieZnacznikaY = event.getY();
			Log.d(INPUT_METHOD_SERVICE, "Przestawiono znacznik");
			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Log.d(INPUT_METHOD_SERVICE, "Pobrano parametry sieci");
		listaPunktow.add(new PointF(polozenieZnacznikaX, polozenieZnacznikaY));
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bitmapaMapy = BitmapFactory.decodeResource(getResources(),
				R.drawable.plan);
		mapa = new Mapa(this);
		setContentView(mapa);
		listaPunktow = new ArrayList<PointF>();
		mapa.setOnTouchListener(this);
		fps = 20;

		String nazwa = getIntent().getStringExtra("nazwa");
		String adres = getIntent().getStringExtra("adres");
		zalogowanyJako = Integer.valueOf(getIntent().getStringExtra("idUsera"));
		int id_obrazka = Integer.valueOf(getIntent().getStringExtra("idObrazka"));
		
		informacjeOmapie = new InformacjeOmapie(adres,id_obrazka, nazwa);
		

	}

	@Override
	protected void onPause() {
		super.onPause();
		mapa.pause();
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
							/ 2, y - bitmapaPunktu.getWidth() / 2, null);
				}
				c.drawBitmap(bitmapaZnacznika, polozenieZnacznikaX
						- bitmapaZnacznika.getWidth() / 2, polozenieZnacznikaY
						- bitmapaZnacznika.getWidth() / 2, null);

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
