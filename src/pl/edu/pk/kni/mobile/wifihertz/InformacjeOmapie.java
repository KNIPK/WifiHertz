package pl.edu.pk.kni.mobile.wifihertz;

import java.io.File;

import android.os.Environment;




public class InformacjeOmapie {
	private String adresBitmapy;
	private int id;
	private String nazwa;
	
	public InformacjeOmapie(){
		adresBitmapy = null;
		id = -1;
		nazwa = null;
	}
	public Boolean jestPoprawny(){
		if(id>0 && nazwa!= null && adresBitmapy !=null)
			return true;
		else 
			return false;
	} 
	public InformacjeOmapie(String adresBitmapy, int id, String nazwa) {
		this.adresBitmapy = adresBitmapy;
		this.id = id;
		this.nazwa = nazwa;
		System.out.println("Otworzono nowa mape: "+id+"\t"+adresBitmapy+"\t"+nazwa);
		
		
	}
	public String getAdresBitmapy() {
		return adresBitmapy;
	}
	public void setAdresBitmapy(String adresBitmapy) {
		this.adresBitmapy = adresBitmapy;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	@Override
	public String toString() {
		String wynik = new String(nazwa);
		File plik = new File(Environment.getExternalStorageDirectory().toString(), Integer.toString(id)+".png"); 
		if(plik.exists())
			wynik+= " (pobrane)";
		
		return wynik;
	
	}
	
	
	
}
