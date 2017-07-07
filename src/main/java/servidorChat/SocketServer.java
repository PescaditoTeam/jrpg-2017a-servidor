package servidorChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import servidor.EscuchaCliente;
/*
 * Esta clase se encarga de escuchar los comandos del pipe.
 */
public class SocketServer implements Runnable {
    
    public static ServerThread clientes[];
    public ServerSocket server = null;
    public Thread       thread = null;
    public static int cantClientes = 0;
    public int puerto = 1500;
   // public static ArrayList<EscuchaCliente> clientesConectados = new ArrayList<EscuchaCliente>();
    public static ArrayList<String> clientesConectados = new ArrayList<String>();
    public SocketServer(){
       
        clientes = new ServerThread[50];
        
    try{  
        server = new ServerSocket(puerto);
        puerto = server.getLocalPort();
        start(); 
        }
    catch(IOException ioe){  
    }
    }
    
    public SocketServer(int _puerto){
        clientes = new ServerThread[50];
        puerto = _puerto;        
    try{  
        server = new ServerSocket(puerto);
        puerto = server.getLocalPort();
        start(); 
        }
    catch(IOException ioe){  
    }
    }
    
    public void run(){  
    while (thread != null){  
            try{  
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
    
    public static void enviarListaDeUsuarios(String aQuien){
        for(int i = 0; i < cantClientes; i++){
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
        if (pos < cantClientes-1){
                for (int i = pos+1; i < clientesConectados.size(); i++){
                    clientes[i-1] = clientes[i];
            }
        }
        cantClientes--;
        try{  
            hiloAEliminar.close(); 
        }
        catch(IOException ioe){  
        }
        hiloAEliminar.stop(); 
    }
    }
    
    private void agregarHilo(Socket socket){  
    if (cantClientes < clientes.length){
        System.out.println("Entro y creo un nuevo cliente");
        clientes[cantClientes] = new ServerThread(this, socket);
        try{  
            clientes[cantClientes].inicializarObjetosLecturaEscritura(); 
            clientes[cantClientes].start();  
            cantClientes++; 
        }
        catch(IOException e){  
            e.printStackTrace();
        } 
    }
    else{
        System.out.println("No se creo ningun cliente");
    }
    }
//    public static void agregarCliente(EscuchaCliente cliente){
//        clientesConectados.add(cliente);
//        enviarATodos("NUEVO_USUARIO", "SERVER", cliente.getPaqueteUsuario().getUsername());
//        System.out.println(cliente.getPaqueteUsuario().getUsername());
//        enviarListaDeUsuarios(cliente.getPaqueteUsuario().getUsername());
//    }
    public static void agregarCliente(String cliente){
        clientesConectados.add(cliente);
        enviarATodos("NUEVO_USUARIO", "SERVER", cliente);
        System.out.println(cliente);
        enviarListaDeUsuarios(cliente);
    }
    
}