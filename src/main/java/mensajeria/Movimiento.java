package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class Movimiento extends ComandoServidor{

	public Movimiento(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Gson gson = new Gson();
		escuchaCliente.setPaqueteMovimiento((PaqueteMovimiento) (gson.fromJson((String) cadenaLeida, PaqueteMovimiento.class)));

		Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setPosX(escuchaCliente.getPaqueteMovimiento().getPosX());
		Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setPosY(escuchaCliente.getPaqueteMovimiento().getPosY());
		Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setDireccion(escuchaCliente.getPaqueteMovimiento().getDireccion());
		Servidor.getUbicacionPersonajes().get(escuchaCliente.getPaqueteMovimiento().getIdPersonaje()).setFrame(escuchaCliente.getPaqueteMovimiento().getFrame());
		
		synchronized(Servidor.atencionMovimientos){
			Servidor.atencionMovimientos.notify();
		}
	}

}
