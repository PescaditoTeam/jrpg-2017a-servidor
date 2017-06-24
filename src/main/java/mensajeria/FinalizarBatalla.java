package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import estados.Estado;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class FinalizarBatalla extends ComandoServidor{

	public FinalizarBatalla(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		escuchaCliente.setPaqueteFinalizarBatalla((PaqueteFinalizarBatalla) gson.fromJson(cadenaLeida, PaqueteFinalizarBatalla.class));
		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarBatalla().getId()).setEstado(Estado.estadoJuego);
		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteFinalizarBatalla().getIdEnemigo()).setEstado(Estado.estadoJuego);
		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			if(conectado.getIdPersonaje() == escuchaCliente.getPaqueteFinalizarBatalla().getIdEnemigo()) {
				conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteFinalizarBatalla()));
			}
		}
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
		
	}

}
