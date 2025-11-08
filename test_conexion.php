<?php
require('conexion.php');
if ($conn) {
    echo "Conexión a la base de datos exitosa.";
} else {
    echo "Error en la conexión.";
}
?>
