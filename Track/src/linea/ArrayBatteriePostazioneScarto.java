package linea;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayBatteriePostazioneScarto {

	private CopyOnWriteArrayList<Batteria> dateList;
	public String nomeLista = "";
	public static int totaleBatterie = 0; //batterie nella linea
	public int parzialeBatterie = 0; //batterie nella postazione

	public ArrayBatteriePostazioneScarto() {
		dateList = new CopyOnWriteArrayList<Batteria>();
	}

	public void addBatteria(Batteria b) {
		dateList.addIfAbsent(b);
		// dateList.add(b);
		totaleBatterie += 1;
		parzialeBatterie +=1;
	}

	public void addBatteriaTop(Batteria b) {
		dateList.add(0, b);
		totaleBatterie += 1;
		parzialeBatterie +=1;
	}

	public int getDimension() {
		return dateList.size();
	}

	public Batteria getOnTop() throws Exception {
		if (dateList.get(0) == null)
			throw new Exception("array vuoto");
		else
			return dateList.get(0);
	}

	public Iterator<Batteria> getIterator() {
		return dateList.iterator();
	}

	public void cancellaBatteria(Batteria index) {
		dateList.remove(index);
		totaleBatterie -= 1;
		parzialeBatterie -=1;
	}
	
	public void cancellaTutto() {
		dateList.clear();
		totaleBatterie = 0;
		parzialeBatterie = 0;
	}

	public boolean contains(Batteria batteria) {
		Iterator<Batteria> it = this.getIterator();
		while (it.hasNext()) {
			if ((it.next().getCodiceBatteria().equals(batteria.getCodiceBatteria())))
				// if((it.next().getCodiceBatteria().equals(batteria.getCodiceBatteria())) &&
				// it.next().gettimestamp().equals(batteria.gettimestamp()) )
				return true;
		}
		return false;
	}

}// fine classe
