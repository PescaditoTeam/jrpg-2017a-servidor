package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class Conexion extends ComandoServidor{

	public Conexion(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		escuchaCliente.setPaquetePersonaje((PaquetePersonaje) (gson .fromJson(cadenaLeida, PaquetePersonaje.class)).clone());

		Servidor.getPersonajesConectados().put(escuchaCliente.getPaquetePersonaje().getId(), (PaquetePersonaje) escuchaCliente.getPaquetePersonaje().clone());
		Servidor.getUbicacionPersonajes().put(escuchaCliente.getPaquetePersonaje().getId(), (PaqueteMovimiento) new PaqueteMovimiento(escuchaCliente.getPaquetePersonaje().getId()).clone());
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
		
	}

}
