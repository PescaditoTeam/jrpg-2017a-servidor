package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import dominio.Item;
import dominio.MyRandom;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class ActualizarPersonaje extends ComandoServidor{

	public ActualizarPersonaje(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		escuchaCliente.setPaquetePersonaje((PaquetePersonaje) gson.fromJson(cadenaLeida, PaquetePersonaje.class));
		if(escuchaCliente.getPaquetePersonaje().isGano() == true){
    		MyRandom r = new MyRandom();
    		Item item = new Item();
    		item = Servidor.getItemsExistentes()[r.obtenerAleatorioMenorQue(10)];
    		escuchaCliente.getPaquetePersonaje().getMochila().add(item);
		}

		Servidor.getConector().actualizarPersonaje(escuchaCliente.getPaquetePersonaje());
		
		Servidor.getPersonajesConectados().remove(escuchaCliente.getPaquetePersonaje().getId());
		Servidor.getPersonajesConectados().put(escuchaCliente.getPaquetePersonaje().getId(), escuchaCliente.getPaquetePersonaje());

		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaquetePersonaje()));
		}
		
		
	}

}
