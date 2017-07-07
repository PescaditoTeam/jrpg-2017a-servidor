/*package com.socket;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;

public class ServerFrame extends javax.swing.JFrame {

    public SocketServer server;
    public Thread serverThread;
    public String filePath = "//datosDeUsuario.xml";
    public JFileChooser fileChooser;//Con este objeto abro la ventana de apertura de archivo
    
    public ServerFrame() {
    	getContentPane().setBackground(new Color(240, 255, 240));
        inicializarComponentesDeVentana();
        
        fileChooser = new JFileChooser();
        jTextArea1.setEditable(false);
        getContentPane().setLayout(null);
        getContentPane().add(jScrollPane1);
        getContentPane().add(jButton1);
        
        lblLogs = new JLabel("Logs");
        lblLogs.setBounds(12, 58, 69, 20);
        getContentPane().add(lblLogs);
    }


    @SuppressWarnings("unchecked")
    private void inicializarComponentesDeVentana() {

        jButton1 = new javax.swing.JButton();
        jButton1.setBounds(62, 26, 141, 29);
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setBounds(12, 82, 241, 127);
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor Taller");

        jButton1.setText("Iniciar Servidor");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new Font("Dialog", Font.PLAIN, 14)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

       // pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
       // server = new SocketServer(this);
       // jButton1.setEnabled(false); //jButton2.setEnabled(false);
    }

    public void RetryStart(int port){
        if(server != null){ server.stop(); }
        server = new SocketServer(this, port);
    }

    public static void main(String args[]) {

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex){
            System.out.println("Look & Feel Exception");
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame().setVisible(true);
            }
        });
    }
    
    public boolean esWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }
   
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    private JLabel lblLogs;
    
}*/
