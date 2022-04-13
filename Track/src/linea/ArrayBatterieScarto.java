package linea;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayBatterieScarto {

	private static CopyOnWriteArrayList<ArrayBatteriePostazioneScarto> dateList;
	public static int numeroBatterie = 0;

	public ArrayBatterieScarto() {
		dateList = new CopyOnWriteArrayList<ArrayBatteriePostazioneScarto>();
	}

	public synchronized void addArray(ArrayBatteriePostazioneScarto b) {
		dateList.add(b);
	}

	public synchronized void addArrayTop(ArrayBatteriePostazioneScarto b) {

		dateList.add(0, b);
	}

	public synchronized int getDimension() {
		return dateList.size();
	}

	public synchronized ArrayBatteriePostazioneScarto getOnTop() {
		return dateList.get(0);
	}

	public Iterator<ArrayBatteriePostazioneScarto> getIterator() {
		return dateList.iterator();
	}

	public synchronized boolean contains(Batteria batteria) {
		Iterator<ArrayBatteriePostazioneScarto> it = this.getIterator();
		while (it.hasNext()) {
			if (it.next().contains(batteria))
				;
			return true;
		}
		return false;
	}

}// fine classe
