/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.example.Presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.example.Dominio.Snack;
import com.example.Servicio.IservicioCliente;
import com.example.Servicio.IservicioCompra;
import com.example.Servicio.IservicioSnakcs;
import com.example.Servicio.ServicioSnacksArchivo;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Synd
 */
public class Cliente extends javax.swing.JFrame {

    ImageIcon[] imagenes;
    ImageIcon[] imagenes2;
    ImageIcon[] imagenes3;
    ImageIcon[] imagenes4;

    private ServicioSnacksArchivo servicioSnacks = new ServicioSnacksArchivo();
    private DefaultTableModel modelDisponibles = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Todas las celdas no editables
        }
    };

    private DefaultTableModel modelSeleccionados = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private IservicioCompra servicioCompra;
    private IservicioCliente servicioCliente;

    int index1 = 0;
    int index2 = 0;
    int index3 = 0;
    int index4 = 0;

    public Cliente(IservicioSnakcs servicioSnacks, IservicioCompra servicioCompra, IservicioCliente servicioCliente) {
        initComponents();
        inicializarComponentesPersonalizados();
        this.servicioCompra = servicioCompra;
        this.servicioCliente = servicioCliente;
    }

    // Constructor sin parámetros para compatibilidad con el main
    public Cliente() {
        initComponents();
        inicializarComponentesPersonalizados();
    }

    // MOVER TODO EL CÓDIGO PERSONALIZADO A ESTE MÉTODO
    private void inicializarComponentesPersonalizados() {
        iniciarCarrusel();
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        
        // Configurar modelos de tabla
        modelDisponibles.setColumnIdentifiers(new String[] { "ID", "Producto", "Precio", "Disponibles" });
        modelSeleccionados.setColumnIdentifiers(new String[] { "ID", "Producto", "Precio", "Cantidad" });

        JTB_E_disponibles_E_seleccionados.setModel(modelDisponibles);

        // Configurar botones
        Jbtn_agregar_p.addActionListener(e -> agregarSnackALaCompra());
        JBTN_Eliminar_P.addActionListener(e -> eliminarSnackDeLaCompra());
        
        // Configurar click en Report Client
        JLB_Report_Client.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirReporteCliente();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                // Efecto hover - cambiar color cuando pase el mouse
                JLB_Report_Client.setForeground(new Color(200, 200, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar color original
                JLB_Report_Client.setForeground(Color.WHITE);
            }
        });

        // Cargar datos iniciales
        cargarSnacksDisponibles();
    }

    // Método para cargar y cambiar imágenes automáticamente
    private void iniciarCarrusel() {
        // Imágenes para el primer JLabel
        imagenes = new ImageIcon[] {
                new ImageIcon(getClass().getResource("/recta/recta1.png")),
                new ImageIcon(getClass().getResource("/recta/recta2.png")),
                new ImageIcon(getClass().getResource("/recta/recta3.png"))
        };

        // Imágenes para el segundo JLabel
        imagenes2 = new ImageIcon[] {
                new ImageIcon(getClass().getResource("/r/r1.png")),
                new ImageIcon(getClass().getResource("/r/r2.jpg")),
                new ImageIcon(getClass().getResource("/r/r3.png"))
        };

        // Imágenes para el tercer JLabel
        imagenes3 = new ImageIcon[] {
                new ImageIcon(getClass().getResource("/img/p1.jpg")),
                new ImageIcon(getClass().getResource("/img/p2.png")),
                new ImageIcon(getClass().getResource("/img/p3.png"))
        };
        
        // Imágenes para el cuarto JLabel
        imagenes4 = new ImageIcon[] {
                new ImageIcon(getClass().getResource("/y/t3.jpg")),
                new ImageIcon(getClass().getResource("/y/t2.png")),
                new ImageIcon(getClass().getResource("/y/t1.png"))
        };

        // Timer para Label1
        new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JLB_imagen_1.setIcon(resizeImage(imagenes[index1], JLB_imagen_1));
                index1 = (index1 + 1) % imagenes.length;
            }
        }).start();

        // Timer para Label2
        new Timer(3500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JLB_imagen_2.setIcon(resizeImage(imagenes2[index2], JLB_imagen_2));
                index2 = (index2 + 1) % imagenes2.length;
            }
        }).start();

        // Timer para Label3
        new Timer(3500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JLB_imagen_3.setIcon(resizeImage(imagenes3[index3], JLB_imagen_3));
                index3 = (index3 + 1) % imagenes3.length;
            }
        }).start();

        // Timer para Label4 - CORREGIR: usar imagenes4 en lugar de imagenes3
        new Timer(3500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JLB_imagen_4.setIcon(resizeImage(imagenes4[index4], JLB_imagen_4));
                index4 = (index4 + 1) % imagenes4.length;
            }
        }).start();
    }

    private Icon resizeImage(ImageIcon originalIcon, JLabel label) {
        Image image = originalIcon.getImage();
        int width = label.getWidth();
        int height = label.getHeight();

        // Escalar la imagen al tamaño del JLabel
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        Bg = new javax.swing.JPanel();
        JPanel_user_icon = new javax.swing.JPanel();
        jSeparator_user = new javax.swing.JSeparator();
        jSeparator_report = new javax.swing.JSeparator();
        jSeparator_comunicarse = new javax.swing.JSeparator();
        JLB_User = new javax.swing.JLabel();
        JLB_Report_Client = new javax.swing.JLabel();
        JLB_comunicarse = new javax.swing.JLabel();
        JLB_user_icon = new javax.swing.JLabel();
        jbtn_config = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        Jpanel_tabla_f = new javax.swing.JPanel();
        Panel_tabla = new javax.swing.JPanel();
        JTXF_Busqueda = new javax.swing.JTextField();
        separator_busqueda = new javax.swing.JSeparator();
        JLB_busqueda_icon = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTB_E_disponibles_E_seleccionados = new javax.swing.JTable();
        JBTN_Eliminar_P = new javax.swing.JButton();
        Jbtn_agregar_p = new javax.swing.JButton();
        jPanel_imagenes = new javax.swing.JPanel();
        JLB_imagen_3 = new javax.swing.JLabel();
        JLB_imagen_4 = new javax.swing.JLabel();
        JLB_imagen_2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JLB_imagen_1 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Snacks_Bums.com");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPanel_user_icon.setBackground(new java.awt.Color(48, 73, 240));

        JLB_User.setFont(new java.awt.Font("Gadugi", 1, 14)); // NOI18N
        JLB_User.setForeground(new java.awt.Color(255, 255, 255));
        JLB_User.setText("User");

        JLB_Report_Client.setFont(new java.awt.Font("Gadugi", 1, 14)); // NOI18N
        JLB_Report_Client.setForeground(new java.awt.Color(255, 255, 255));
        JLB_Report_Client.setText("Report client");
        JLB_Report_Client.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        JLB_comunicarse.setFont(new java.awt.Font("Gadugi", 1, 14)); // NOI18N
        JLB_comunicarse.setForeground(new java.awt.Color(255, 255, 255));
        JLB_comunicarse.setText("Comunicarse");
        JLB_comunicarse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        JLB_user_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLB_user_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/avatar-design.png"))); // NOI18N

        jbtn_config.setBackground(new java.awt.Color(48, 73, 240));
        jbtn_config.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/settings_1.png"))); // NOI18N
        jbtn_config.setBorder(null);
        jbtn_config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn_configActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPanel_user_iconLayout = new javax.swing.GroupLayout(JPanel_user_icon);
        JPanel_user_icon.setLayout(JPanel_user_iconLayout);
        JPanel_user_iconLayout.setHorizontalGroup(
            JPanel_user_iconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                .addGroup(JPanel_user_iconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(JPanel_user_iconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator_user)
                            .addComponent(jSeparator_report)
                            .addComponent(jSeparator_comunicarse)
                            .addComponent(JLB_Report_Client, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                    .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                        .addGroup(JPanel_user_iconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(JLB_User, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(JLB_comunicarse))
                            .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(JLB_user_icon)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jbtn_config, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanel_user_iconLayout.setVerticalGroup(
            JPanel_user_iconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel_user_iconLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(JLB_user_icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLB_User)
                .addGap(18, 18, 18)
                .addComponent(jSeparator_user, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(JLB_Report_Client)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator_report, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(JLB_comunicarse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator_comunicarse, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jbtn_config, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        Bg.add(JPanel_user_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 500));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        Jpanel_tabla_f.setBackground(new java.awt.Color(225, 239, 239));

        Panel_tabla.setBackground(new java.awt.Color(255, 255, 255));

        JTXF_Busqueda.setBorder(null);

        separator_busqueda.setForeground(new java.awt.Color(51, 51, 51));

        JLB_busqueda_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLB_busqueda_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Search.png"))); // NOI18N

        JTB_E_disponibles_E_seleccionados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Elementos disponibles", "Elementos seleccionados"
            }
        ));
        JTB_E_disponibles_E_seleccionados.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                JTB_E_disponibles_E_seleccionadosAncestorRemoved(evt);
            }
        });
        jScrollPane1.setViewportView(JTB_E_disponibles_E_seleccionados);

        JBTN_Eliminar_P.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/dumbbell.png"))); // NOI18N
        JBTN_Eliminar_P.setBorder(null);

        Jbtn_agregar_p.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png"))); // NOI18N
        Jbtn_agregar_p.setBorder(null);

        javax.swing.GroupLayout Panel_tablaLayout = new javax.swing.GroupLayout(Panel_tabla);
        Panel_tabla.setLayout(Panel_tablaLayout);
        Panel_tablaLayout.setHorizontalGroup(
            Panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_tablaLayout.createSequentialGroup()
                .addGroup(Panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_tablaLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(Panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JTXF_Busqueda, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                            .addComponent(separator_busqueda))
                        .addGap(18, 18, 18)
                        .addComponent(JLB_busqueda_icon))
                    .addGroup(Panel_tablaLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(Jbtn_agregar_p, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBTN_Eliminar_P, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        Panel_tablaLayout.setVerticalGroup(
            Panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_tablaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JBTN_Eliminar_P, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel_tablaLayout.createSequentialGroup()
                        .addGroup(Panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(Panel_tablaLayout.createSequentialGroup()
                                .addComponent(JTXF_Busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(separator_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(JLB_busqueda_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Jbtn_agregar_p, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout Jpanel_tabla_fLayout = new javax.swing.GroupLayout(Jpanel_tabla_f);
        Jpanel_tabla_f.setLayout(Jpanel_tabla_fLayout);
        Jpanel_tabla_fLayout.setHorizontalGroup(
            Jpanel_tabla_fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Jpanel_tabla_fLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Panel_tabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Jpanel_tabla_fLayout.setVerticalGroup(
            Jpanel_tabla_fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Jpanel_tabla_fLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Panel_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel_imagenes.setBackground(new java.awt.Color(255, 255, 255));

        JLB_imagen_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLB_imagen_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/p2.png"))); // NOI18N

        JLB_imagen_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLB_imagen_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/y/t3.jpg"))); // NOI18N

        JLB_imagen_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLB_imagen_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/r/r1.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ISOLATE PROTEIN");

        jLabel4.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        jLabel4.setText("ISOLATE PROTEIN CB");

        jLabel5.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("RAW PRE");

        javax.swing.GroupLayout jPanel_imagenesLayout = new javax.swing.GroupLayout(jPanel_imagenes);
        jPanel_imagenes.setLayout(jPanel_imagenesLayout);
        jPanel_imagenesLayout.setHorizontalGroup(
            jPanel_imagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_imagenesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_imagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_imagenesLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JLB_imagen_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JLB_imagen_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JLB_imagen_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_imagenesLayout.setVerticalGroup(
            jPanel_imagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_imagenesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLB_imagen_2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLB_imagen_3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLB_imagen_4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        JLB_imagen_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLB_imagen_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recta/recta3.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Jpanel_tabla_f, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLB_imagen_1, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel_imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel_imagenes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(JLB_imagen_1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Jpanel_tabla_f, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Bg.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 760, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    // MÉTODOS PERSONALIZADOS - FUERA DEL CÓDIGO GENERADO
    private void cargarSnacksDisponibles() {
        modelDisponibles.setRowCount(0);
        servicioSnacks.getSnacks().cellSet().forEach(cell -> {
            Snack snack = cell.getValue();
            modelDisponibles.addRow(new Object[] {
                    snack.getIdSnack(),
                    snack.getNombre(),
                    snack.getPrecio(),
                    snack.getCantidad()
            });
        });
    }

    private void agregarSnackALaCompra() {
    int filaSeleccionada = JTB_E_disponibles_E_seleccionados.getSelectedRow();
    if (filaSeleccionada >= 0) {
        try {
            int id = (int) modelDisponibles.getValueAt(filaSeleccionada, 0);
            Snack snackComprado = servicioSnacks.comprarSnackJSON(id); 
            
            // Actualizar modelo de seleccionados
            modelSeleccionados.addRow(new Object[]{
                snackComprado.getIdSnack(),
                snackComprado.getNombre(),
                snackComprado.getPrecio(),
                1
            });
            
            
            // Actualizar modelo de disponibles
            int nuevaCantidad = (int) modelDisponibles.getValueAt(filaSeleccionada, 3) - 1;
            modelDisponibles.setValueAt(nuevaCantidad, filaSeleccionada, 3);
            
            JOptionPane.showMessageDialog(this, "¡Snack añadido al carrito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un snack", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
    private void eliminarSnackDeLaCompra() {
        int filaSeleccionada = JTB_E_disponibles_E_seleccionados.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) modelSeleccionados.getValueAt(filaSeleccionada, 0);
            servicioSnacks.getSnacks().row(id).values().forEach(snack -> {
                snack.setCantidad(snack.getCantidad() + 1);
            });
            modelSeleccionados.removeRow(filaSeleccionada);
            actualizarDisponibilidad(id, 1);
        }
    }

    private void actualizarDisponibilidad(int id, int cambio) {
        for (int i = 0; i < modelDisponibles.getRowCount(); i++) {
            if ((int) modelDisponibles.getValueAt(i, 0) == id) {
                int nuevaCantidad = (int) modelDisponibles.getValueAt(i, 3) + cambio;
                modelDisponibles.setValueAt(nuevaCantidad, i, 3);
                break;
            }
        }
        servicioSnacks.actualizarArchivo();
    }
    
    // Método para abrir la vista de reportes
    private void abrirReporteCliente() {
        try {
            // Crear y mostrar la ventana de reportes
            ReporteCliente reporteVentana = new ReporteCliente();
            reporteVentana.setVisible(true);
            
            // Opcional: Cerrar la ventana actual o mantenerla abierta
            // this.dispose(); // Descomenta si quieres cerrar la ventana actual
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al abrir la ventana de reportes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // EVENTOS GENERADOS POR NETBEANS
    private void JTB_E_disponibles_E_seleccionadosAncestorRemoved(javax.swing.event.AncestorEvent evt) {
        servicioSnacks.ObtenerSnacks();
        cargarSnacksDisponibles();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        // TODO add your handling code here:
    }

    private void formWindowIconified(java.awt.event.WindowEvent evt) {
        // TODO add your handling code here:
    }

    private void jbtn_configActionPerformed(java.awt.event.ActionEvent evt) {
        new config().setVisible(true);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel Bg;
    private javax.swing.JButton JBTN_Eliminar_P;
    private javax.swing.JLabel JLB_Report_Client;
    private javax.swing.JLabel JLB_User;
    private javax.swing.JLabel JLB_busqueda_icon;
    private javax.swing.JLabel JLB_comunicarse;
    private javax.swing.JLabel JLB_imagen_1;
    private javax.swing.JLabel JLB_imagen_2;
    private javax.swing.JLabel JLB_imagen_3;
    private javax.swing.JLabel JLB_imagen_4;
    private javax.swing.JLabel JLB_user_icon;
    private javax.swing.JPanel JPanel_user_icon;
    private javax.swing.JTable JTB_E_disponibles_E_seleccionados;
    private javax.swing.JTextField JTXF_Busqueda;
    private javax.swing.JButton Jbtn_agregar_p;
    private javax.swing.JPanel Jpanel_tabla_f;
    private javax.swing.JPanel Panel_tabla;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel_imagenes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator_comunicarse;
    private javax.swing.JSeparator jSeparator_report;
    private javax.swing.JSeparator jSeparator_user;
    private javax.swing.JButton jbtn_config;
    private javax.swing.JSeparator separator_busqueda;
    // End of variables declaration                   
}