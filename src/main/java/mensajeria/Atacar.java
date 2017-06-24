package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class Atacar extends ComandoServidor{

	public Atacar(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		escuchaCliente.setPaqueteAtacar((PaqueteAtacar) gson .fromJson(cadenaLeida, PaqueteAtacar.class));
		for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
			if(conectado.getIdPersonaje() == escuchaCliente.getPaqueteAtacar().getIdEnemigo()) {
				conectado.getSalida().writeObject(gson.toJson(escuchaCliente.getPaqueteAtacar()));
			}
		}
	}

}
