package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class CreacionPJ extends ComandoServidor{

	public CreacionPJ(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		escuchaCliente.setPaquetePersonaje((PaquetePersonaje) (gson.fromJson(cadenaLeida, PaquetePersonaje.class)));
		
		// Guardo el personaje en ese usuario
		Servidor.getConector().registrarPersonaje(escuchaCliente.getPaquetePersonaje(), escuchaCliente.getPaqueteUsuario());
		
		// Le envio el id del personaje
		escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaquetePersonaje(), escuchaCliente.getPaquetePersonaje().getClass()));
	}

}
