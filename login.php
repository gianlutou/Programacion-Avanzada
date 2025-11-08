<?php
require_once(__DIR__ . "/conexion.php");

if (isset($_POST['usuario']) && isset($_POST['contrasena'])) {
    $usuario = $_POST['usuario'];
    $contrasena = $_POST['contrasena'];

    $stmt = $conn->prepare("SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?");
    $stmt->bind_param("ss", $usuario, $contrasena);
    $stmt->execute();

    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        echo "✅ Inicio de sesión exitoso";
    } else {
        echo "❌ Usuario o contraseña incorrectos";
    }

    $stmt->close();
} else {
    echo "⚠️ Faltan datos (usa POST con usuario y contrasena)";
}
?>
