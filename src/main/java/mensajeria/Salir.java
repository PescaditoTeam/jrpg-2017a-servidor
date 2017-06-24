package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class Salir extends ComandoServidor{

	public Salir(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		// Cierro todo
		escuchaCliente.getEntrada().close();
		escuchaCliente.getSalida().close();
		escuchaCliente.getSocket().close();
		
		// Lo elimino de los clientes conectados
		Servidor.getClientesConectados().remove(escuchaCliente);
		
		// Indico que se desconecto
		Paquete paquete = (Paquete) gson.fromJson(cadenaLeida, Paquete.class);
		Servidor.log.append(paquete.getIp() + " se ha desconectado." + System.lineSeparator());
		
		//return; Que hago aca?	
	}

}
