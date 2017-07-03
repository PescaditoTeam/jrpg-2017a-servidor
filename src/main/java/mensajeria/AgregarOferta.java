package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class AgregarOferta extends ComandoServidor{

	public AgregarOferta(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		PaqueteMercado paqueteMercado = new PaqueteMercado();
		paqueteMercado = (PaqueteMercado) gson.fromJson(cadenaLeida, PaqueteMercado.class);
		Servidor.AgregarOferta(paqueteMercado.getOferta());
		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			conectado.getSalida().writeObject(gson.toJson(paqueteMercado));
		}	
		
	}

}
