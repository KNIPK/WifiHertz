package pl.edu.pk.kni.mobile.wifihertz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class EkranPomiaru extends Activity {
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapa.pause();
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapa.resume();
	}

	WifiManager wifi;
	private Mapa mapa;
	float polozenieMapyX;
	float polozenieMapyY;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapa = new Mapa(this);
        setContentView(mapa);
    }

  
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ekran_pomiaru, menu);
    
        return true;
    }
    
    class Mapa extends SurfaceView implements Runnable{
    	Thread t = null;
    	SurfaceHolder holder;
    	Boolean dzialaj = false;
		
    	public Mapa(Context context){
    		super(context);
    		holder = getHolder();
    	}
    	
    	public void run() {
			while(dzialaj)
			{
				if(!holder.getSurface().isValid())
					continue;
				
				Canvas c = holder.lockCanvas();
				c.drawARGB(255, 255, 255, 200);
				holder.unlockCanvasAndPost(c);	
				
			}
			
		}
		
		public void resume(){
			dzialaj = true;
			t = new Thread(this);
			t.start();
		}
		
		public void pause(){
			dzialaj = false;
			while(true)
			{
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
