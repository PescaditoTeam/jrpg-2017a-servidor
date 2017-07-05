package mensajeria;

import servidor.EscuchaCliente;

public class FactoryComandoServidor {


	public Comando elegir(int nro, String cadenaLeida, EscuchaCliente ec) {
		Comando comando;
		switch (nro) {
		case Comando.REGISTRO:
			comando = new Registro(cadenaLeida, ec);
			break;
		case Comando.CREACIONPJ:
			comando = new CreacionPJ(cadenaLeida, ec);
			break;
		case Comando.INICIOSESION:
			comando = new InicioSesion(cadenaLeida, ec);
			break;
		case Comando.SALIR:
			comando = new Salir(cadenaLeida, ec);
			break;
		case Comando.CONEXION:
			comando = new Conexion(cadenaLeida, ec);
			break;
		case Comando.MOVIMIENTO:
			comando = new Movimiento(cadenaLeida, ec);
			break;
		case Comando.MOSTRARMAPAS:
			comando = new MostrarMapas(cadenaLeida, ec);
			break;
		case Comando.BATALLA:
			comando = new Batalla(cadenaLeida, ec);
			break;
		case Comando.ATACAR:
			comando = new Atacar(cadenaLeida, ec);
			break;
		case Comando.FINALIZARBATALLA:
			comando = new FinalizarBatalla(cadenaLeida, ec);
			break;
		case Comando.ACTUALIZARPERSONAJE:
			comando = new ActualizarPersonaje(cadenaLeida, ec);
			break;
		case 12/*Comando.AGREGAROFERTA*/:
			comando = new AgregarOferta(cadenaLeida, ec);
			break;
		case 13 /*Comando.INTERCAMBIAR*/:
		    comando = new Intercambiar(cadenaLeida, ec);
		    break;
		case 14 /*Comando.ACTUALIZARINTERCAMBIO*/:
		    comando = new ActualizarIntercambio(cadenaLeida, ec);
		    break;
		case 15/*Comando.SACAROFERTA*/:
		    comando = new SacarOferta(cadenaLeida, ec);
		    break;
		default:
			return null;
		}
		return comando;

	}

}
