<?php
require_once(__DIR__ . "/conexion.php");

header("Content-Type: application/json");

$res = $conn->query("SELECT docente, dias, motivo FROM faltas");
$faltas = [];

while ($row = $res->fetch_assoc()) {
    $faltas[] = $row;
}

echo json_encode(["success" => true, "faltas" => $faltas]);
?>
