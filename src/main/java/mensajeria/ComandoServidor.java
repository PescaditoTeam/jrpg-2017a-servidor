package mensajeria;

import servidor.EscuchaCliente;

public abstract class ComandoServidor extends Comando{
	public ComandoServidor(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida);
		this.escuchaCliente = e;
	}

	protected EscuchaCliente escuchaCliente;

	public EscuchaCliente getEscuchaCliente() {
		return escuchaCliente;
	}

	public void setEscuchaCliente(EscuchaCliente escuchaCliente) {
		this.escuchaCliente = escuchaCliente;
	}

	

}
