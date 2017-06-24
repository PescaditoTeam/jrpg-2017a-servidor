package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class MostrarMapas extends ComandoServidor{

	public MostrarMapas(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		// Indico en el log que el usuario se conecto a ese mapa
		escuchaCliente.setPaquetePersonaje((PaquetePersonaje) gson .fromJson(cadenaLeida, PaquetePersonaje.class));
		Servidor.log.append(escuchaCliente.getSocket().getInetAddress().getHostAddress() + " ha elegido el mapa " + escuchaCliente.getPaquetePersonaje().getMapa() + System.lineSeparator());
		
	}

}
