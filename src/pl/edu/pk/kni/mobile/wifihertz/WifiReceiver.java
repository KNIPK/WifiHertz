package pl.edu.pk.kni.mobile.wifihertz;

import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver 
{
	int imageId;
	float x;
	float y;
	Boolean czyZapisywacWynik;
	EkranPomiaru ob1;
	List<ScanResult> res;
	Baza db;
	
	
	public WifiReceiver(EkranPomiaru ob1)
	{
		super();
		this.ob1 = ob1;
		czyZapisywacWynik = false;
		db = new Baza(ob1);
	}
	
	@Override
	public void onReceive(Context arg0, Intent arg1) 
	{
	
	     res = ob1.wifi.getScanResults();
	     if(czyZapisywacWynik){
	    	 for(ScanResult listka : res){  
					db.dodajDane(imageId, listka.SSID, listka.BSSID, ""+listka.level,(int)x, (int)y);
				}
	    	System.out.println("onReceive uruchomiony");
	 		this.czyZapisywacWynik = false;
			AlertDialog alert = new AlertDialog.Builder(arg0).create();
			alert.setTitle("Ok");
			alert.setMessage("Pomiar wykonany pomyślnie");
	        alert.show();
	     }
		     
	}
	
	public void zrobPomiarWPunkcie(int imageid, float x, float y){
		//robi pomiar wszystkich sieci i dodaje w pisy do bazy o wspolrzednych x, y
		
		this.x = x;
		this.y= y;
		this.imageId = imageid;
		this.czyZapisywacWynik = true;
		ob1.wifi.startScan();

				
		
	}
	
}
