<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$servername = "localhost";
$username = "root";
$password = "";
$database = "control_faltas"; // usá el nombre real de tu base

$conn = new mysqli($servername, $username, $password, $database);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["error" => "Error al conectar con la base de datos: " . $conn->connect_error]);
    exit;
}
?>
