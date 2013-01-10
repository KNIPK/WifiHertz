package pl.edu.pk.kni.mobile.wifihertz;

import android.provider.BaseColumns;

public interface BazaStale extends BaseColumns {
	public static final String NAZWA_TABELI = "wifiData";
	
	//kolumny w tabeli "wifiData"
	public static final String IMAGE_ID = "imageId";
	public static final String DATA_TIME = "dataTime";
	public static final String WIFI_NAME = "wifiName";
	public static final String WIFI_SSID = "wifiSsid";
	public static final String WIFI_RANGE = "wifiRange";
	public static final String POSITION_X = "positionX";
	public static final String POSITION_Y = "positionY";
	
}
