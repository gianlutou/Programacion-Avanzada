# Sistema de Gestión de Inasistencias Docentes

## Descripción general
El Sistema de Gestión de Inasistencias es una aplicación desarrollada en Java (NetBeans) con conexión a una base de datos MySQL. Su objetivo es registrar, administrar y consultar licencias médicas de docentes dentro de instituciones educativas.

El sistema contempla distintos perfiles de usuario y permite visualizar información relevante de manera clara, rápida y ordenada.

Este repositorio contiene el código fuente completo, junto con la estructura necesaria para ejecutar el proyecto.

---

## Propósito del sistema
El sistema brinda una herramienta centralizada para:

- Registrar licencias docentes.
- Gestionar usuarios con diferentes niveles de acceso.
- Permitir la consulta pública de licencias por parte de estudiantes.

---

## Alcance

### El sistema abarca:
- Registro y modificación de licencias.
- Visualización de licencias.
- Gestión de usuarios con roles diferenciados (docente / administrativo).
- Conexión directa a base de datos MySQL.
- Interfaz gráfica construida en NetBeans.

### El sistema no abarca:
- Control de asistencia estudiantil.
- Integración con plataformas externas.
- Notificaciones automáticas.
- Gestión salarial o administrativa más allá de las licencias.

---

## Organización del equipo

- **Gianlucca Tourn – Coordinador general / Backend**
  - Coordinación del equipo.
  - Desarrollo de la lógica principal y conexión a la base de datos.
  - Supervisión de tiempos y requisitos.

- **Javier Hernández – Responsable de documentación técnica / UML**
  - Elaboración y mantenimiento de la documentación.
  - Diagramas UML, ER y casos de uso.
  - Aporte en validación y pruebas.

- **Nahuel Silva – Subcoordinador / Backend / Gestión documental**
  - Apoyo en coordinación.
  - Colaboración en backend.
  - Organización de archivos y documentación técnica.

---

## Análisis FODA (resumen)

- **Fortalezas:** buena comunicación interna, organización, conocimientos técnicos complementarios.
- **Debilidades:** poca experiencia en proyectos colaborativos.
- **Oportunidades:** alta demanda de sistemas educativos y apoyo docente.
- **Amenazas:** tiempos ajustados y riesgo de retrasos.

---

## Requerimientos del Sistema

### Funcionales
- Registro y gestión de docentes.
- Registro y gestión de licencias.
- Visualización pública de licencias.
- Gestión de usuarios y autenticación.
- Formularios y listados mediante interfaz gráfica.

### No funcionales
- Interfaz clara y utilizable por usuarios sin conocimientos técnicos.
- Acceso seguro mediante roles.
- Escalabilidad en la base de datos.
- Código modular y mantenible.

---

## Modelos y diagramas incluidos
El proyecto incluye:

- Diagrama de casos de uso.
- Diagrama entidad–relación (DER).
- Pasaje a tablas y normalización.
- Diagrama UML general del sistema.

---

## Base de datos (estructura)

### Tablas principales:

- **USUARIO** (ci, nombre, apellido, contrasenia, rol)
- **DOCENTE** (ci)
- **ADMINISTRATIVO** (ci)
- **GRUPO** (id_grupo, turno)
- **PERTENECE** (ci, id_grupo, materia)
- **LICENCIA** (id_licencia, fecha_inicio, fecha_fin, motivo)
- **TIENE** (ci, id_licencia)

## Casos de uso principales
- **Iniciar sesión:** usuarios administrativos o docentes acceden mediante CI y contraseña.
- **Registrar licencia:** permite cargar datos validados antes de almacenarlos.
- **Ver licencias: muestra** información completa de las licencias activas.

## Ejecución del sistema

Para poder ejecutar correctamente el programa es necesario contar con un entorno local que permita manejar la base de datos MySQL.  
El sistema fue desarrollado utilizando **XAMPP**, por lo tanto se requiere instalarlo antes de iniciar el proyecto.

### Requerimientos previos
- XAMPP instalado (incluye Apache y MySQL).
- Servicio **MySQL** iniciado desde el panel de control de XAMPP.
- Base de datos importada (archivo `.sql` incluido en el proyecto).
- NetBeans o un entorno compatible con proyectos Java.

---

## Guía básica para ejecutar el programa

1. **Instalar XAMPP**  
   Descargar e instalar XAMPP en su versión estable.

2. **Iniciar MySQL**  
   Abrir el Panel de Control de XAMPP y presionar *Start* en el módulo MySQL.  
   Esto habilita la conexión a la base de datos utilizada por el sistema.

3. **Crear la base de datos**  
   - Abrir phpMyAdmin desde XAMPP.  
   - Crear una nueva base de datos con el nombre utilizado por el proyecto.  
   - Importar el archivo `.sql` proporcionado.

4. **Configurar archivos necesarios para la correcta conexion con la base de datos**  
    Bueno ahora el paso que se debe de hacer es en esta direccion: "el nombre de tu almacenamiento"\xampp\htdocs y ahi crear una carpeta que se llame "faltas" y pegar dentro de ella los 5 archivos .php que se encuentran disponibles

5. **Configurar la conexión en NetBeans**  
   Verificar que el proyecto tenga configurados correctamente:
   - URL de conexión  
   - Usuario  
   - Contraseña  
   Estos valores deben coincidir con la configuración de MySQL en XAMPP.

6. **Ejecutar el proyecto**  
   Una vez iniciados los servicios y cargada la base de datos, simplemente abrir el proyecto en NetBeans y presionar Run.  
   El sistema debería iniciar sin necesitar pasos adicionales.
