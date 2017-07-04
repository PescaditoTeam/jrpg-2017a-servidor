package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class Intercambiar extends ComandoServidor {

    public Intercambiar(String cadenaLeida, EscuchaCliente e) {
        super(cadenaLeida, e);
    }

    @Override
    public void resolver() throws IOException {
        Gson gson = new Gson();
        PaqueteIntercambio paqueteIntercambio = new PaqueteIntercambio();
        paqueteIntercambio = (PaqueteIntercambio) gson.fromJson(cadenaLeida, PaqueteIntercambio.class);
        for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
            if(conectado.getPaquetePersonaje().getId() == paqueteIntercambio.getId1()) {
                try {
                    conectado.getSalida().writeObject(gson.toJson(paqueteIntercambio));
                } catch (IOException e) {
                    e.printStackTrace();
                }   
            } 
            else if(conectado.getPaquetePersonaje().getId() == paqueteIntercambio.getId2()) {
                try {
                    conectado.getSalida().writeObject(gson.toJson(paqueteIntercambio));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
