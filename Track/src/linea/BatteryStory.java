package linea;

import java.util.LinkedList;
import java.util.Queue;

public class BatteryStory {

	String codice_batteria = "";
	String timestamp = "";
	Queue<Batteria> arrayBatterie = new LinkedList<>();
	private static int MAX_SIZE = 25; // numero massimo stazioni

	public BatteryStory(Batteria batt) {
		/*
		 * codice_batteria = batt.getCodiceBatteria(); if
		 * (arrayBatterie.size()>MAX_SIZE) { arrayBatterie.poll(); }
		 */
		this.aggiungi(batt);

	}

	public Batteria getFirstBatteria() {
		// return arrayBatterie.poll(); //la restituisce, la rimuove anche dalla coda
		return arrayBatterie.element();// la restituisce, ma NON la rimuove anche dalla coda
	}

	public void aggiungi(Batteria batt) {

		codice_batteria = batt.getCodiceBatteria();

		if (timestamp.equals(""))
			timestamp = batt.gettimestamp();

		synchronized (arrayBatterie) {
			if (arrayBatterie.size() > MAX_SIZE) {
				arrayBatterie.poll();
			}

			arrayBatterie.add(batt);
		}

	}// fine aggiungi

	public void itera() {
		arrayBatterie.forEach(batteria -> {
			batteria.getCodiceBatteria(); // faccio qualcosa

		});
	}

	public boolean contains(Batteria batt) {
		synchronized (arrayBatterie) {
			for (Batteria batteria : arrayBatterie) {
				if (batt.getCodiceBatteria().equals(batteria.getCodiceBatteria())) {
					return true;
				}
			} // fine for
		}
		return false;
	}

	public String print() {

		String print = "";
		for (Batteria batteria : arrayBatterie) {
			print += "-- > Codice Bat:" + batteria.getCodiceBatteria() + " - Postazione:" + batteria.getPostazione();
		} // fine for

		return print + "\n";
	}// fine print

	public Queue<Batteria> getElencoBatterie() {
		// Queue<Batteria> tmp = new LinkedList<>(arrayBatterie);//clono
		return arrayBatterie;
	}

	public String getCodiceBatteryStory() {

		return codice_batteria;
	}

	public String getTimestamp() {

		return timestamp;
	}

}// fine classe
