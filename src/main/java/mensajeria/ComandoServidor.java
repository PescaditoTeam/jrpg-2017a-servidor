package mensajeria;

import servidor.EscuchaCliente;

public abstract class ComandoServidor extends Comando{
	protected EscuchaCliente escuchaCliente;

	public EscuchaCliente getEscuchaCliente() {
		return escuchaCliente;
	}

	public void setEscuchaCliente(EscuchaCliente escuchaCliente) {
		this.escuchaCliente = escuchaCliente;
	}

	

}
