package pl.edu.pk.kni.mobile.wifihertz;

import static android.provider.BaseColumns._ID;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.DATA_TIME;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.IMAGE_ID;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.NAZWA_TABELI;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.POSITION_X;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.POSITION_Y;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.WIFI_NAME;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.WIFI_RANGE;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.WIFI_SSID;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Baza {

	private BazaTworzenie baza;
	private Activity activity;
	
	public Baza(Activity activity){
		baza = new BazaTworzenie(activity);
		this.activity = activity;
	}
	
	public void dodajDane(int imageId, String wifiName, String wifiSsid, String wifiRange, int positionX, int positionY){
		SQLiteDatabase db = baza.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		
		wartosci.put(IMAGE_ID, imageId);
		wartosci.put(DATA_TIME, System.currentTimeMillis());
		wartosci.put(WIFI_NAME, wifiName);
		wartosci.put(WIFI_SSID, wifiSsid);
		wartosci.put(WIFI_RANGE, wifiRange);
		wartosci.put(POSITION_X, positionX);
		wartosci.put(POSITION_Y, positionY);
		
		db.insertOrThrow(NAZWA_TABELI, null, wartosci);
	}
	
	private static String[] FROM = { _ID, IMAGE_ID, DATA_TIME, WIFI_NAME, WIFI_SSID, WIFI_RANGE, POSITION_X, POSITION_Y };
	private static String ORDER_BY = _ID +" DESC";
	public Cursor pobierzDane(int idImage){
		SQLiteDatabase db = baza.getReadableDatabase();
		Cursor kursor = null;
		if(idImage==0){
			kursor = db.query(NAZWA_TABELI, FROM, null, null, null, null, ORDER_BY);
		}else{
			kursor = db.query(NAZWA_TABELI, FROM, IMAGE_ID +" = ?", new String[] { ""+idImage }, null, null, ORDER_BY);
		}
		
		activity.startManagingCursor(kursor);
		
//		AlertDialog alert = new AlertDialog.Builder(activity).create();
//		alert.setTitle("wpis");
//        alert.setMessage(""+kursor.getCount());
//        alert.show();
		
		
//		while(kursor.moveToNext()){
//			String name = kursor.getString(1);
//			
//			//AlertDialog alert = new AlertDialog.Builder(activity).create();
//			alert.setTitle("wpis");
//	        alert.setMessage(name);
//	        alert.show();
//	        break;
//		}
        return kursor;
	}
	
	
	public void synchronizuj(){
		Cursor kursor = pobierzDane(0);
		
		
		/*tworzymy sobie klienta http, zebysmy mogli przesy�a� dane do serwera*/
		HttpClient httpclient = new DefaultHttpClient();
//		HttpResponse response = null;
		
		while(kursor.moveToNext()){
			//kazdy wpis wysylamy na serwer, zeby sprawdzic czy tam juz taki istnieje, jesli nie to dodajemy
			String dataId = kursor.getString(0);
			String imageId = kursor.getString(1);
			String dataTime = kursor.getString(2);
			String wifiName = kursor.getString(3);
			String wifiSsid = kursor.getString(4);
			String wifiRange = kursor.getString(5);
			String positionX = kursor.getString(6);
			String positionY = kursor.getString(7);
			
			wifiName = wifiName.replace(" ", "_");
	        
			try {
				httpclient.execute(new HttpGet("http://wifihertz.kalinowski.net.pl/index.php?page=addData&imageId="+imageId+"&dataId="+dataId+"&dataTime="+dataTime+"&wifiName="+wifiName+"&wifiSsid="+wifiSsid+"&wifiRange="+wifiRange+"&positionX="+positionX+"&positionY="+positionY));
			}
			catch (ClientProtocolException e) {
				AlertDialog alert = new AlertDialog.Builder(activity).create();
				alert.setTitle("Błąd synchronizacji");
		        alert.setMessage(e.toString());
		        alert.show();
			} catch (IOException e) {
				AlertDialog alert2 = new AlertDialog.Builder(activity).create();
				alert2.setTitle("Błąd synchronizacji");
		        alert2.setMessage(e.toString());
		        alert2.show();
			}
		}
		System.out.println("Wykonano synchronizacje");
	}
	
	
}
