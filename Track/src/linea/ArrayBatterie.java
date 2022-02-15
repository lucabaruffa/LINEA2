package linea;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayBatterie {

	private static CopyOnWriteArrayList<ArrayBatteriePostazione> dateList;
	public static int numeroBatterie = 0;

	public ArrayBatterie() {
		dateList = new CopyOnWriteArrayList<ArrayBatteriePostazione>();
	}

	public synchronized void addArray(ArrayBatteriePostazione b) {
		dateList.add(b);
	}

	public synchronized void addArrayTop(ArrayBatteriePostazione b) {

		dateList.add(0, b);
	}

	public synchronized int getDimension() {
		return dateList.size();
	}

	public synchronized ArrayBatteriePostazione getOnTop() {
		return dateList.get(0);
	}

	public Iterator<ArrayBatteriePostazione> getIterator() {
		return dateList.iterator();
	}

	public synchronized boolean contains(Batteria batteria) {
		Iterator<ArrayBatteriePostazione> it = this.getIterator();
		while (it.hasNext()) {
			if (it.next().contains(batteria))
				;
			return true;
		}
		return false;
	}

}// fine classe
