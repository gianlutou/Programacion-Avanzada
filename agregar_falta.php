<?php
require_once(__DIR__ . '/conexion.php');
header('Content-Type: application/json');

$docente = $_POST['docente'] ?? '';
$dias = $_POST['dias'] ?? '';
$motivo = $_POST['motivo'] ?? '';

if ($docente && $dias && $motivo) {
    $stmt = $conn->prepare("INSERT INTO control_faltas.faltas (docente, dias, motivo) VALUES (?, ?, ?)");
    if (!$stmt) {
        echo json_encode(["success" => false, "error" => $conn->error]);
        exit;
    }

    // 🔧 Corregido: solo 3 parámetros
    $stmt->bind_param("sss", $docente, $dias, $motivo);

    if ($stmt->execute()) {
        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "error" => $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "error" => "Datos incompletos"]);
}

$conn->close();
?>
