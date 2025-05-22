package com.raven.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.example.Dominio.Cliente;
import com.example.Servicio.ServicioClienteArchivo;
import com.example.Servicio.ServicioCompraArchivo;
import com.example.Servicio.ServicioSnacksArchivo;
import com.raven.model.ModelLogin;
import com.raven.swing.Button;
import com.raven.swing.MyPasswordField;
import com.raven.swing.MyTextField;

import net.miginfocom.swing.MigLayout;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    private ServicioClienteArchivo servicioCliente;
    private ServicioSnacksArchivo servicioSnacks;
    private ServicioCompraArchivo servicioCompra;

    public ModelLogin getDataLogin() {
        return dataLogin;
    }

    public Cliente getUser() {
        return user;
    }

    private Cliente user;
    private ModelLogin dataLogin;

    public PanelLoginAndRegister(ActionListener eventRegister, ActionListener eventLogin) {
        initComponents();
        // Inicializar el servicio de cliente
        servicioCliente = new ServicioClienteArchivo();
        initRegister(eventRegister);
        initLogin(eventLogin);
        login.setVisible(false);
        register.setVisible(true);
    }

    private void initRegister(ActionListener eventRegister) {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Crear Cuenta");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(48, 73, 240));
        register.add(label);
        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/user.png")));
        txtUser.setHint("Nombre");
        register.add(txtUser, "w 60%");
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setHint("Correo");
        register.add(txtEmail, "w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setHint("Contraseña");
        register.add(txtPass, "w 60%");
        Button cmd = new Button();
        cmd.setBackground(new Color(48, 73, 240));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("REGISTRARSE");
        register.add(cmd, "w 40%, h 40");

        // Un solo ActionListener que maneja la validación y registro
        cmd.addActionListener(e -> {
            String userName = txtUser.getText().trim();
            String email = txtEmail.getText().trim();
            String password = String.valueOf(txtPass.getPassword());

            if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Crear un usuario temporal con ID 0 (se generará el ID correcto en el
                // servicio)
                user = new Cliente(0, userName, email, password);

                // Registrar el usuario
                boolean registroExitoso = servicioCliente.registrarCliente(user);

                if (registroExitoso) {
                    JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Notificar evento de registro exitoso
                    eventRegister.actionPerformed(e);

                    // Limpiar campos
                    txtUser.setText("");
                    txtEmail.setText("");
                    txtPass.setText("");

                    // Cambiar a la vista de inicio de sesión

                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar. El correo ya existe.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    private void initLogin(ActionListener eventLogin) {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Iniciar Sesión");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(48, 73, 240));
        login.add(label);
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setHint("Correo");
        login.add(txtEmail, "w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setHint("Contraseña");
        login.add(txtPass, "w 60%");
        JButton cmdForget = new JButton("Has olvidado tu contraseña?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);
        Button cmd = new Button();
        cmd.setBackground(new Color(48, 73, 240));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("INGRESAR");
        login.add(cmd, "w 40%, h 40");

        // Un solo ActionListener para el inicio de sesión
        cmd.addActionListener((ActionEvent ae) -> {
            String email = txtEmail.getText().trim();
            String password = String.valueOf(txtPass.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Solo preparar el modelo y pasar al listener
            dataLogin = new ModelLogin(email, password);
            //eventLogin.actionPerformed(ae);

            // Limpiar campos
            txtEmail.setText("");
            txtPass.setText("");

            com.example.Presentacion.Cliente clienteWindow = new com.example.Presentacion.Cliente();
            // If Cliente has setter methods for services, use them:
            // clienteWindow.setServicioSnacks(servicioSnacks);
            // clienteWindow.setServicioCompra(servicioCompra);
            // clienteWindow.setServicioCliente(servicioCliente);
            clienteWindow.setVisible(true);
            SwingUtilities.getWindowAncestor(this).dispose();


        });

    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE));
        loginLayout.setVerticalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE));
        registerLayout.setVerticalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}