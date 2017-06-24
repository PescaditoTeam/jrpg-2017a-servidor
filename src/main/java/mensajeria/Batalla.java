package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import estados.Estado;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class Batalla extends ComandoServidor{

	public Batalla(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		// Le reenvio al id del personaje batallado que quieren pelear
		Gson gson = new Gson();
		escuchaCliente.setPaqueteBatalla((PaqueteBatalla) gson.fromJson(cadenaLeida, PaqueteBatalla.class));
		Servidor.log.append(escuchaCliente.getPaqueteBatalla().getId() + " quiere batallar con " + escuchaCliente.getPaqueteBatalla().getIdEnemigo() + System.lineSeparator());
		
		//seteo estado de batalla
		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatalla().getId()).setEstado(Estado.estadoBatalla);
		Servidor.getPersonajesConectados().get(escuchaCliente.getPaqueteBatalla().getIdEnemigo()).setEstado(Estado.estadoBatalla);
		escuchaCliente.getPaqueteBatalla().setMiTurno(true);
		escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatalla()));
		for(EscuchaCliente conectado : Servidor.getClientesConectados()){
			if(conectado.getIdPersonaje() == escuchaCliente.getPaqueteBatalla().getIdEnemigo()){
				int aux = escuchaCliente.getPaqueteBatalla().getId();
				escuchaCliente.getPaqueteBatalla().setId(escuchaCliente.getPaqueteBatalla().getIdEnemigo());
				escuchaCliente.getPaqueteBatalla().setIdEnemigo(aux);
				escuchaCliente.getPaqueteBatalla().setMiTurno(false);
				conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteBatalla()));
				return;
			}
		}
		
		synchronized(Servidor.atencionConexiones){
			Servidor.atencionConexiones.notify();
		}
	}

}
