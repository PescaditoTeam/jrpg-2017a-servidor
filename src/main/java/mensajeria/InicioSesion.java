package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;
import com.socket.SocketServer;

import mensajeriaComandos.Comando;
import servidor.EscuchaCliente;
import servidor.Servidor;

public class InicioSesion extends ComandoServidor{

	public InicioSesion(String cadenaLeida, EscuchaCliente e) {
		super(cadenaLeida, e);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resolver() {
		Paquete paqueteSv = new Paquete(null, 0);
		paqueteSv.setComando(Comando.INICIOSESION);
		Gson gson = new Gson();
		
		// Recibo el paquete usuario
		escuchaCliente.setPaqueteUsuario((PaqueteUsuario) (gson.fromJson(cadenaLeida, PaqueteUsuario.class)));
		
		// Si se puede loguear el usuario le envio un mensaje de exito y el paquete personaje con los datos
		if (Servidor.getConector().loguearUsuario(escuchaCliente.getPaqueteUsuario())) {
			
			escuchaCliente.setPaquetePersonaje(new PaquetePersonaje());
			escuchaCliente.setPaquetePersonaje(Servidor.getConector().getPersonaje(escuchaCliente.getPaqueteUsuario()));
			escuchaCliente.getPaquetePersonaje().setComando(Comando.INICIOSESION);
			escuchaCliente.getPaquetePersonaje().setMensaje(Paquete.msjExito);
			escuchaCliente.setIdPersonaje(escuchaCliente.getPaquetePersonaje().getId());
			SocketServer.agregarCliente(escuchaCliente);
			try {
				escuchaCliente.getSalida().writeObject(gson.toJson(escuchaCliente.getPaquetePersonaje()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			paqueteSv.setMensaje(Paquete.msjFracaso);
			try {
				escuchaCliente.getSalida().writeObject(gson.toJson(paqueteSv));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
