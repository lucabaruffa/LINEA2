package linea;

public class greenCode {

	private String nome = "";
	private String greencode = "";
	private String Linea = "";
	private String cdc = "";

	public greenCode() {

	}

	public greenCode(String nome, String codice, String linea, String cdc) {
		this.nome = nome;
		this.greencode = codice;
		this.cdc = cdc;
		Linea = linea;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGreencode() {
		return greencode;
	}

	public void setGreencode(String greencode) {
		this.greencode = greencode;
	}

	public String getLinea() {
		return Linea;
	}

	public void setLinea(String linea) {
		Linea = linea;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

}// fine classe
