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
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.*;

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
	public Cursor pobierzDane(){
		SQLiteDatabase db = baza.getReadableDatabase();
		Cursor kursor = db.query(NAZWA_TABELI, FROM, null, null, null, null, ORDER_BY);
		activity.startManagingCursor(kursor);
		
		kursor.moveToNext();
		String name = kursor.getString(3);
		
		AlertDialog alert = new AlertDialog.Builder(activity).create();
		alert.setTitle("wpis");
        alert.setMessage(name);
        alert.show();
        
        return kursor;
	}
	
	
	public void synchronizuj(){
		Cursor kursor = pobierzDane();
		
		/*tworzymy sobie klienta http, zebysmy mogli przesy³aæ dane do serwera*/
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		
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
	        
			try {
				response = httpclient.execute(new HttpGet("http://wifihertz.kalinowski.net.pl/index.php?page=addData&imageId="+imageId+"&dataId="+dataId+"&dataTime="+dataTime+"&wifiName="+wifiName+"&wifiSsid="+wifiSsid+"&wifiRange="+wifiRange+"&positionX="+positionX+"&positionY="+positionY));
//				StatusLine statusLine = response.getStatusLine();
//				if(statusLine.getStatusCode()==HttpStatus.SC_OK){
//					ByteArrayOutputStream out = new ByteArrayOutputStream();
//					response.getEntity().writeTo(out);
//					out.close();
//					idUzytkownika = out.toString();
//				}
			}
			catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
