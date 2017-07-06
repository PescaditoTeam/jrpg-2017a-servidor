package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import mensajeriaComandos.Comando;
import servidor.EscuchaCliente;
import servidor.Servidor;

/**
 * Cuando se regirtra un nuevo usuario.
 * Si se pudo registrar le envio un mensaje de exito.
 * Si no le mando un mensaje de fracaso.
 */
public class Registro extends ComandoServidor{

	public Registro(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
	}

	@Override
	public void resolver() throws IOException {
		Paquete paqueteSv = new Paquete(null, 0);
		paqueteSv.setComando(Comando.REGISTRO);
		Gson gson = new Gson();
		
		escuchaCliente.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(cadenaLeida, PaqueteUsuario.class)).clone());

		if (Servidor.getConector().registrarUsuario(escuchaCliente.getPaqueteUsuario())) {
			paqueteSv.setMensaje(Paquete.msjExito);
				escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));

		} else {
			paqueteSv.setMensaje(Paquete.msjFracaso);
				escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));

		}	
	}
}
