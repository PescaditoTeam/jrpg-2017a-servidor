package servidor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.socket.SocketServer;

import dominio.Item;
import dominio.Ofertas;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;

public class Servidor extends Thread {

    private static ArrayList<EscuchaCliente> clientesConectados = new ArrayList<>();

    private static Map<Integer, PaqueteMovimiento> ubicacionPersonajes = new HashMap<>();
    private static Map<Integer, PaquetePersonaje> personajesConectados = new HashMap<>();
    private static ArrayList<Ofertas> ofertasDisponibles = new ArrayList<Ofertas>();
    private static ArrayList<Ofertas> ofertasUtilizadas = new ArrayList<Ofertas>();
    public static Item[] itemsExistentes;

    private static Thread server;

    private static ServerSocket serverSocket;
    private static Conector conexionDB;
    private final int PUERTO = 9999;

    private final static int ANCHO = 700;
    private final static int ALTO = 640;
    private final static int ALTO_LOG = 520;
    private final static int ANCHO_LOG = ANCHO - 25;

    public static JTextArea log;

    public static AtencionConexiones atencionConexiones = new AtencionConexiones();
    public static AtencionMovimientos atencionMovimientos = new AtencionMovimientos();;
    public static SocketServer serverChat;
    
    public static void main(String[] args) {
        cargarInterfaz();
        serverChat = new SocketServer();
        if(serverChat!=null) System.out.println("Servidor de Chat Instanciado");
    }

    private static void cargarInterfaz() {
        JFrame ventana = new JFrame("Servidor WOME");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(ANCHO, ALTO);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(null);

        JLabel titulo = new JLabel("Log del servidor...");
        titulo.setFont(new Font("Courier New", Font.BOLD, 16));
        titulo.setBounds(10, 0, 200, 30);
        ventana.add(titulo);

        log = new JTextArea();
        log.setEditable(false);
        log.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(log,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(10, 40, ANCHO_LOG, ALTO_LOG);
        ventana.add(scroll);

        final JButton botonIniciar = new JButton();
        final JButton botonDetener = new JButton();
        botonIniciar.setText("Iniciar");
        botonIniciar.setBounds(220, ALTO - 70, 100, 30);
        botonIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                server = new Thread(new Servidor());
                server.start();
                botonIniciar.setEnabled(false);
                botonDetener.setEnabled(true);
            }
        });

        ventana.add(botonIniciar);

        botonDetener.setText("Detener");
        botonDetener.setBounds(360, ALTO - 70, 100, 30);
        botonDetener.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    server.stop();//////////////////////////////////////////////////////////////////////////////////////
                    for (EscuchaCliente cliente : clientesConectados) {
                        cliente.getSalida().close();
                        cliente.getEntrada().close();
                        cliente.getSocket().close();
                    }
                    serverSocket.close();
                } catch (IOException e1) {
                    log.append("Fallo al intentar detener el servidor."
                            + System.lineSeparator());
                    e1.printStackTrace();
                }
                if (conexionDB != null)
                    conexionDB.close();
                botonDetener.setEnabled(false);
                botonIniciar.setEnabled(true);
            }
        });
        botonDetener.setEnabled(false);
        ventana.add(botonDetener);

        ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if (serverSocket != null) {
                    try {
                        server.stop();
                        for (EscuchaCliente cliente : clientesConectados) {
                            cliente.getSalida().close();
                            cliente.getEntrada().close();
                            cliente.getSocket().close();
                        }
                        serverSocket.close();
                    } catch (IOException e) {
                        log.append("Fallo al intentar detener el servidor."
                                + System.lineSeparator());
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                if (conexionDB != null)
                    conexionDB.close();
                System.exit(0);
            }
        });
        File fi = new File("Items.txt");
        Scanner sc;
        try {
            sc = new Scanner(fi);
            sc.useLocale(Locale.ENGLISH);
            itemsExistentes = new Item[sc.nextInt()];
            // int tam = sc.nextInt();
            for (int i = 0; i < itemsExistentes.length; i++) {
                itemsExistentes[i] = new Item(i + 1, sc.next(), sc.nextInt(),
                        sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(),
                        sc.nextInt());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ventana.setVisible(true);
    }

    public void run() {
        try {

            conexionDB = new Conector();
            conexionDB.connect();

            log.append("Iniciando el servidor..." + System.lineSeparator());
            serverSocket = new ServerSocket(PUERTO);
            log.append("Servidor esperando conexiones..."
                    + System.lineSeparator());
            String ipRemota;

            atencionConexiones.start();
            atencionMovimientos.start();

            while (true) {
                Socket cliente = serverSocket.accept();
                ipRemota = cliente.getInetAddress().getHostAddress();
                log.append(
                        ipRemota + " se ha conectado" + System.lineSeparator());

                ObjectOutputStream salida = new ObjectOutputStream(
                        cliente.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(
                        cliente.getInputStream());

                EscuchaCliente atencion = new EscuchaCliente(ipRemota, cliente,
                        entrada, salida);
                atencion.start();
                clientesConectados.add(atencion);
            }
        } catch (Exception e) {
            log.append("Fallo la conexi�n." + System.lineSeparator());
            e.printStackTrace();
        }
    }

    public static ArrayList<EscuchaCliente> getClientesConectados() {
        return clientesConectados;
    }

    public static Map<Integer, PaqueteMovimiento> getUbicacionPersonajes() {
        return ubicacionPersonajes;
    }

    public static Map<Integer, PaquetePersonaje> getPersonajesConectados() {
        return personajesConectados;
    }

    public static Conector getConector() {
        return conexionDB;
    }

    public static ArrayList<Ofertas> getOfertasDisponibles() {
        return ofertasDisponibles;
    }

    public static void AgregarOferta(Ofertas o) {
        ofertasDisponibles.add(o);
    }
    public static void SacarOferta(Ofertas o1, Ofertas o2){
        
        ofertasUtilizadas.add(o1);
        ofertasUtilizadas.add(o2);
    }

    public static void setOfertasDisponibles(
            ArrayList<Ofertas> ofertasDisponibles) {
        Servidor.ofertasDisponibles = ofertasDisponibles;
    }

    public static Item[] getItemsExistentes() {
        return itemsExistentes;
    }

    public static String getItemsExistentesName(int pos) {
        return itemsExistentes[pos].getNombre();
    }

    public static void setItemsExistentes(Item[] itemsExistentes) {
        Servidor.itemsExistentes = itemsExistentes;
    }

}