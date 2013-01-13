package pl.edu.pk.kni.mobile.wifihertz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static pl.edu.pk.kni.mobile.wifihertz.BazaStale.*;

public class BazaTworzenie extends SQLiteOpenHelper {
	private static final String NAZWA_BAZY_DANYCH = "baza.db";
	private static final int WERSJA_BAZY_DANYCH = 6;
	
	/*Tworzy obiekt pomocniczy dla bazy*/
	public BazaTworzenie(Context ktks){
		super(ktks, NAZWA_BAZY_DANYCH, null, WERSJA_BAZY_DANYCH);
	}
	
	@Override
	public void onCreate(SQLiteDatabase bd) {
		bd.execSQL("CREATE TABLE " + NAZWA_TABELI + " ("+ _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
				IMAGE_ID +" TEXT NOT NULL, "+ DATA_TIME +" INTEGER, "+WIFI_NAME+" TEXT NOT NULL, "+WIFI_SSID+" TEXT NOT NULL, " +
						WIFI_RANGE+" TEXT NOT NULL, "+POSITION_X+" INTEGER, "+POSITION_Y+" INTEGER);");
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase bd, int staraWersja, int nowaWersja) {
		bd.execSQL("DROP TABLE IF EXISTS "+ NAZWA_TABELI);
		onCreate(bd);
	}
	
	
}
