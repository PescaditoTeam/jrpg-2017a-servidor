package com.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.chat.socket.Mensaje;

import servidor.EscuchaCliente;
/*
 * Esta clase se encarga de escuchar los comandos del pipe.
 */
public class SocketServer implements Runnable {
	static ArrayList<EscuchaCliente> clientesConectados= new ArrayList<>();
	
    public static ServerThread clientes[];/////////////////
    public ServerSocket server = null;
    public Thread       thread = null;
    public static int cantClientes = 0;
    public int puerto = 1500;
    private static String nombreDeUsuarioMomentaneo = "";

    public SocketServer(){
       
        clientes = new ServerThread[50];
        
        
	try{  
	    server = new ServerSocket(puerto);
	    //System.out.println("\nServidor Chat iniciado"); 
	    puerto = server.getLocalPort();
	    start(); 
        }
	catch(IOException ioe){  

	}
    }
    
    public SocketServer(/*ServerFrame frame,*/ int _puerto){
        clientes = new ServerThread[20];//cant max de clientes
        puerto = _puerto;
        
	try{  
	    server = new ServerSocket(puerto);
            puerto = server.getLocalPort();
            //System.out.println("\nServidor Chat iniciado"); 
	    start(); 
        }
	catch(IOException ioe){  
		//System.out.println("\nNo se puede utilizar el puerto seleccionado"); 
	}
    }
	
    public void run(){  
	while (thread != null){  
            try{  
            	//System.out.println("Servidor Chat esperando clientes..");
	        agregarHilo(server.accept()); 
	    }
	    catch(Exception ioe){ 
	    }
        }
    }
	
    public void start(){  
    	if (thread == null){  
            thread = new Thread(this); 
	    thread.start();
	}
    }
    
    @SuppressWarnings("deprecation")
    public void stop(){  
        if (thread != null){  
            thread.stop(); 
	    thread = null;
	}
    }
    
    private int buscarCliente(int ID){  
    	for (int i = 0; i < cantClientes; i++){
        	if (clientes[i].getID() == ID){
                    return i;
                }
	}
	return -1;
    }
	/////////////-------------------------------------------------------
    public synchronized void handle(int ID, Mensaje mensaje){  
	if (mensaje.contenido.equals(".bye")){
            enviarATodos("SALIR", "SERVER", mensaje.remitente);
            eliminarUsuario(ID); 
	}
	else{
			if(mensaje.tipo.equals("MENSAJE")){
                if(mensaje.destinatario.equals("A TODOS")){
                    enviarATodos("MENSAJE", mensaje.remitente, mensaje.contenido);
                }
                else{
                    buscarThreadDeUsuario(mensaje.destinatario).enviarMensaje(new Mensaje(mensaje.tipo, mensaje.remitente, mensaje.contenido, mensaje.destinatario));
                    clientes[buscarCliente(ID)].enviarMensaje(new Mensaje(mensaje.tipo, mensaje.remitente, mensaje.contenido, mensaje.destinatario));
                }
            }
            else if(mensaje.tipo.equals("TEST")){
                clientes[buscarCliente(ID)].enviarMensaje(new Mensaje("TEST", "SERVER", "OK", mensaje.remitente));
            }
	}
    }
    
    public static void enviarATodos(String tipo, String emisor, String contenido){
        Mensaje mensaje = new Mensaje(tipo, emisor, contenido, "A TODOS");
        for(int i = 0; i < cantClientes; i++){
            clientes[i].enviarMensaje(mensaje);
        }
    }
    //ACAAAAAAAA
    public static void enviarListaDeUsuarios(String aQuien){
        for(int i = 0; i < cantClientes; i++){
           // System.out.println(clientes[i].nombreUsuario + " estamos en el metodo enviarListaDeUsuarios");
            buscarThreadDeUsuario(aQuien).enviarMensaje(new Mensaje("NUEVO_USUARIO", "SERVER", clientes[i].nombreUsuario, aQuien));
        }
            
    }
    
    public static ServerThread buscarThreadDeUsuario(String _usuario){
        for(int i = 0; i < cantClientes; i++){
            if(clientes[i].nombreUsuario.equals(_usuario)){
                return clientes[i];
            }
        }
        return null;
    }
	
    @SuppressWarnings("deprecation")
    public synchronized void eliminarUsuario(int ID){  
    int pos = buscarCliente(ID);
        if (pos >= 0){  
            ServerThread hiloAEliminar = clientes[pos];
            //System.out.println("\nEliminando hilo de usuario");
	    if (pos < cantClientes-1){
                for (int i = pos+1; i < cantClientes; i++){
                    clientes[i-1] = clientes[i];
	        }
	    }
	    cantClientes--;
	    try{  
	      	hiloAEliminar.close(); 
	    }
	    catch(IOException ioe){  
	      	//System.out.println("Fallo cerrar HiloAEliminar.close 211 SocketServer");
	    }
	    hiloAEliminar.stop(); 
	}
    }
    
    private void agregarHilo(Socket socket){  
	if (cantClientes < clientes.length){  
		 //System.out.println("\nCliente Aceptado:");
	    clientes[cantClientes] = new ServerThread(this, socket);
	    try{  
	      	clientes[cantClientes].inicializarObjetosLecturaEscritura();
	        clientes[cantClientes].start();  
	        cantClientes++;
	        
	        clientes[cantClientes - 1].nombreUsuario = nombreDeUsuarioMomentaneo;
	        //System.out.println(clientes[cantClientes - 1].nombreUsuario + " se agrego al vector clientes");
	        enviarListaDeUsuarios(clientes[cantClientes - 1].nombreUsuario);
	    }
	    catch(IOException e){  
	    	 //System.out.println("\nError al abrir Thread"); 
	    } 
	}
	else{
		 //System.out.println("\nMaximo de clientes alcanzado");
	}
    }

	public static void agregarCliente(EscuchaCliente atencion) {
	clientesConectados.add(atencion);
	//System.out.println(atencion.getPaqueteUsuario().getUsername()+" se ha conectado");
	nombreDeUsuarioMomentaneo = atencion.getPaqueteUsuario().getUsername();
	if(atencion.getPaqueteUsuario()==null)  //System.out.println("\nPaquete USR NULL"); 
    enviarATodos("NUEVO_USUARIO", "SERVER", atencion.getPaqueteUsuario().getUsername());
   // enviarListaDeUsuarios(atencion.getPaqueteUsuario().getUsername());
	
		
	}
}
