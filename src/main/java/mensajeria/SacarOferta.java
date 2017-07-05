package mensajeria;

import java.io.IOException;

import com.google.gson.Gson;

import servidor.EscuchaCliente;
import servidor.Servidor;

public class SacarOferta extends ComandoServidor {

    public SacarOferta(String cadenaLeida, EscuchaCliente e) {
        super(cadenaLeida, e);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void resolver() throws IOException {
        Gson gson = new Gson();
        PaqueteMercado paqueteMercado = new PaqueteMercado();
        paqueteMercado = (PaqueteMercado) gson.fromJson(cadenaLeida, PaqueteMercado.class);
        Servidor.SacarOferta(paqueteMercado.getOferta(), paqueteMercado.getOferta2());
        for(EscuchaCliente conectado : Servidor.getClientesConectados()) {
            conectado.getSalida().writeObject(gson.toJson(paqueteMercado));
        }   

    }

}
