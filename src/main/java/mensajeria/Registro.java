package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.Servidor;

/**
 * Cuando se regirtra un nuevo usuario.
 * Si se pudo registrar le envio un mensaje de exito.
 * Si no le mando un mensaje de fracaso.
 */
public class Registro extends ComandoServidor{

	@Override
	public void resolver() {
		Paquete paqueteSv = new Paquete(null, 0);
		paqueteSv.setComando(Comando.REGISTRO);
		Gson gson = new Gson();
		
		//ERROR ACA
		escuchaCliente.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(cadenaLeida, PaqueteUsuario.class)).clone());

		if (Servidor.getConector().registrarUsuario(escuchaCliente.getPaqueteUsuario())) {
			paqueteSv.setMensaje(Paquete.msjExito);
			try {
				escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			paqueteSv.setMensaje(Paquete.msjFracaso);
			try {
				escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}	
	}
}
