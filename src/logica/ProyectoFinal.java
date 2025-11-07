package logica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.*;

/**
 * Proyecto Final - Control de Faltas de Docentes Versión con conexión PHP/MySQL
 * vía HTTP (XAMPP)
 */
public class ProyectoFinal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private String usuarioLogueado = null;

    public ProyectoFinal() {
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        setTitle("Control de Faltas de Docentes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        panelPrincipal.add(crearPanelInicio(), "inicio");
        panelPrincipal.add(crearPanelLogin(), "login");
        panelPrincipal.add(crearPanelFaltas(true), "faltasProtegidas");

        add(panelPrincipal);
        cardLayout.show(panelPrincipal, "inicio");
        setVisible(true);
    }

    // ---------------------------------------------------
    // PANEL DE INICIO
    // ---------------------------------------------------
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel botones = new JPanel();
        JButton btnVerFaltas = new JButton("Ver Faltas");
        JButton btnAgregar = new JButton("Agregar Faltas (requiere login)");

        btnVerFaltas.addActionListener(e -> {
            JFrame ventana = new JFrame("Faltas Registradas");
            ventana.setSize(700, 400);
            ventana.setLocationRelativeTo(null);
            ventana.add(crearPanelFaltas(false));
            ventana.setVisible(true);
        });

        btnAgregar.addActionListener(e -> cardLayout.show(panelPrincipal, "login"));

        botones.add(btnVerFaltas);
        botones.add(btnAgregar);
        panel.add(new JLabel("📅 Bienvenido al sistema de faltas de docentes", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(botones, BorderLayout.CENTER);
        return panel;
    }

    // ---------------------------------------------------
    // PANEL DE LOGIN
    // ---------------------------------------------------
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblContrasena = new JLabel("Contraseña:");
        JTextField txtUsuario = new JTextField(15);
        JPasswordField txtContrasena = new JPasswordField(15);
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnVolver = new JButton("Volver al inicio");

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lblUsuario, c);
        c.gridx = 1;
        panel.add(txtUsuario, c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(lblContrasena, c);
        c.gridx = 1;
        panel.add(txtContrasena, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(btnIngresar, c);
        c.gridy = 3;
        panel.add(btnVolver, c);

        btnIngresar.addActionListener(e -> {
    String usuario = txtUsuario.getText();
    String contrasena = new String(txtContrasena.getPassword());

    String respuesta = enviarPOST(
            "http://localhost/faltas/login.php",
            "usuario=" + usuario + "&contrasena=" + contrasena
    );

    if (respuesta != null && respuesta.contains("✅ Inicio de sesión exitoso")) {
        usuarioLogueado = usuario;
        cardLayout.show(panelPrincipal, "faltasProtegidas");
    } else {
        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
    }
});

        

        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "inicio"));
        return panel;
    }

    private JPanel crearPanelFaltas(boolean mostrarFormulario) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"Docente", "Días", "Motivo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        String json = enviarPOST("http://localhost/faltas/listar_faltas.php", "");
        try {
            if (json != null && !json.isEmpty()) {
                JSONObject obj = new JSONObject(json);
                if (obj.getBoolean("success")) {
                    JSONArray faltas = obj.getJSONArray("faltas");
                    for (int i = 0; i < faltas.length(); i++) {
                        JSONObject f = faltas.getJSONObject(i);
                        modelo.addRow(new Object[]{
                            f.getString("docente"),
                            f.getString("dias"),
                            f.getString("motivo"), //                            f.getString("registrado_por")
                        });
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar faltas: " + ex.getMessage());
        }

        panel.add(new JLabel("📋 Faltas Registradas", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        // FORMULARIO (solo si el usuario está logueado)
        if (mostrarFormulario) {
            JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
            JTextField txtDocente = new JTextField();
            JTextField txtDias = new JTextField();
            JTextField txtMotivo = new JTextField();
            JButton btnAgregar = new JButton("Agregar Falta");
            JButton btnVolver = new JButton("Volver al inicio");

            form.add(new JLabel("Docente:"));
            form.add(txtDocente);
            form.add(new JLabel("Días:"));
            form.add(txtDias);
            form.add(new JLabel("Motivo:"));
            form.add(txtMotivo);
            form.add(btnAgregar);
            form.add(btnVolver);

            btnAgregar.addActionListener(e -> {
                if (usuarioLogueado == null) {
                    JOptionPane.showMessageDialog(this, "Debes iniciar sesión para agregar faltas.");
                    return;
                }

                String parametros = "docente=" + txtDocente.getText()
                        + "&dias=" + txtDias.getText()
                        + "&motivo=" + txtMotivo.getText()
                        + "&registrado_por=" + usuarioLogueado;

                String respuesta = enviarPOST("http://localhost/faltas/agregar_falta.php", parametros);

                if (respuesta != null && respuesta.contains("\"success\":true")) {
                    modelo.addRow(new Object[]{
                        txtDocente.getText(),
                        txtDias.getText(),
                        txtMotivo.getText(),
                        usuarioLogueado
                    });
                    JOptionPane.showMessageDialog(this, "Falta agregada correctamente");
                    txtDocente.setText("");
                    txtDias.setText("");
                    txtMotivo.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar falta");
                }
            });

            btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "inicio"));
            panel.add(form, BorderLayout.SOUTH);
        }

        return panel;
    }

   private String enviarPOST(String urlDestino, String parametros) {
    HttpURLConnection conexion = null;
    try {
        // Crear la conexión
        URL url = new URL(urlDestino);
        conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        // Enviar los datos
        try (OutputStream os = conexion.getOutputStream()) {
            byte[] input = parametros.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // Leer la respuesta
        BufferedReader br = new BufferedReader(
                new InputStreamReader(conexion.getInputStream(), "UTF-8")
        );
        StringBuilder respuesta = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            respuesta.append(linea);
        }

        br.close();
        return respuesta.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    } finally {
        if (conexion != null) conexion.disconnect();
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProyectoFinal::new);
    }
}
