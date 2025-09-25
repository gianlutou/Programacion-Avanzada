package proyectofinal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProyectoFinal extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private Connection conexion;
    private String usuarioLogueado = null;

    public ProyectoFinal() {
        conectarBaseDatos();
        insertarUsuarioPorDefecto();
        iniciarComponentes();
    }

    private void conectarBaseDatos() {
        try {
            String url = "jdbc:sqlite:faltas.db";
            conexion = DriverManager.getConnection(url);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar la base de datos: " + e.getMessage());
            System.exit(1);
        }
    }

    private void insertarUsuarioPorDefecto() {
        try {
            Statement stmt = conexion.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (usuario TEXT PRIMARY KEY, contrasena TEXT)");
            stmt.execute("INSERT OR IGNORE INTO usuarios (usuario, contrasena) VALUES ('12345678', 'marcela')");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al insertar usuario por defecto: " + e.getMessage());
        }
    }

    private void iniciarComponentes() {
        setTitle("Control de Faltas de Docentes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
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

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel botones = new JPanel();
        JButton btnVerFaltas = new JButton("Ver Faltas");
        JButton btnAgregar = new JButton("Agregar Faltas (requiere login)");

        btnVerFaltas.addActionListener(e -> {
            JFrame ventana = new JFrame("Faltas Registradas");
            ventana.setSize(600, 400);
            ventana.setLocationRelativeTo(null);
            ventana.add(crearPanelFaltas(false));
            ventana.setVisible(true);
        });

        btnAgregar.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "login");
        });

        botones.add(btnVerFaltas);
        botones.add(btnAgregar);
        panel.add(new JLabel("\uD83D\uDCC5 Bienvenido al sistema de faltas de docentes", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(botones, BorderLayout.CENTER);
        return panel;
    }

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
        c.gridx = 0; c.gridy = 0; panel.add(lblUsuario, c);
        c.gridx = 1; c.gridy = 0; panel.add(txtUsuario, c);
        c.gridx = 0; c.gridy = 1; panel.add(lblContrasena, c);
        c.gridx = 1; c.gridy = 1; panel.add(txtContrasena, c);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; panel.add(btnIngresar, c);
        c.gridy = 3; panel.add(btnVolver, c);

        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());

            if (verificarCredenciales(usuario, contrasena)) {
                usuarioLogueado = usuario;
                cardLayout.show(panelPrincipal, "faltasProtegidas");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "inicio");
        });

        return panel;
    }

    private JPanel crearPanelFaltas(boolean mostrarFormulario) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"Turno", "Horario", "Materia", "Grado", "Registrado Por"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        try {
            Statement stmt = conexion.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS faltas (turno TEXT, horario TEXT, materia TEXT, grado TEXT, registrado_por TEXT)");
            ResultSet rs = stmt.executeQuery("SELECT turno, horario, materia, grado, registrado_por FROM faltas");
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("turno"),
                    rs.getString("horario"),
                    rs.getString("materia"),
                    rs.getString("grado"),
                    rs.getString("registrado_por")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar faltas: " + e.getMessage());
        }

        panel.add(new JLabel("\uD83D\uDCC5 Faltas Registradas", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        if (mostrarFormulario) {
            JPanel formulario = new JPanel(new GridLayout(7, 2));
            JTextField txtTurno = new JTextField();
            JTextField txtHorario = new JTextField();
            JTextField txtMateria = new JTextField();
            JTextField txtGrado = new JTextField();
            JLabel lblRegistradoPor = new JLabel(usuarioLogueado != null ? usuarioLogueado : "(no logueado)");
            JButton btnAgregar = new JButton("Agregar Falta");
            JButton btnVolverInicio = new JButton("Volver al inicio");

            formulario.add(new JLabel("Turno:")); formulario.add(txtTurno);
            formulario.add(new JLabel("Horario:")); formulario.add(txtHorario);
            formulario.add(new JLabel("Materia:")); formulario.add(txtMateria);
            formulario.add(new JLabel("Grado:")); formulario.add(txtGrado);
            formulario.add(new JLabel("Registrado por (Cédula):")); formulario.add(lblRegistradoPor);
            formulario.add(btnVolverInicio); formulario.add(btnAgregar);

            btnAgregar.addActionListener(e -> {
                try {
                    String sql = "INSERT INTO faltas (turno, horario, materia, grado, registrado_por) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conexion.prepareStatement(sql);
                    stmt.setString(1, txtTurno.getText());
                    stmt.setString(2, txtHorario.getText());
                    stmt.setString(3, txtMateria.getText());
                    stmt.setString(4, txtGrado.getText());
                    stmt.setString(5, usuarioLogueado);
                    stmt.executeUpdate();

                    modelo.addRow(new Object[] {
                        txtTurno.getText(),
                        txtHorario.getText(),
                        txtMateria.getText(),
                        txtGrado.getText(),
                        usuarioLogueado
                    });

                    JOptionPane.showMessageDialog(this, "Falta agregada correctamente");
                    txtTurno.setText("");
                    txtHorario.setText("");
                    txtMateria.setText("");
                    txtGrado.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al agregar falta: " + ex.getMessage());
                }
            });

            btnVolverInicio.addActionListener(e -> {
                cardLayout.show(panelPrincipal, "inicio");
            });

            panel.add(formulario, BorderLayout.SOUTH);
        }

        return panel;
    }

    private boolean verificarCredenciales(String usuario, String contrasena) {
        try {
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al verificar credenciales: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProyectoFinal::new);
    }
}
