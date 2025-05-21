# Máquina de Snacks Proteicos - Sistema de Gestión

## Logo del Proyecto
![Logo del Proyecto](/assets/images/Imagen%20de%20WhatsApp%202025-05-21%20a%20las%2012.04.16_7e5180ec.jpg)

[![Java](https://img.shields.io/badge/Java-17.0.7-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.9.2-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Estado](https://img.shields.io/badge/Estado-Finalizado-green?style=for-the-badge)](https://github.com/shinji585/maquina-snacks-refactor)
[![Licencia](https://img.shields.io/badge/Licencia-MIT-97CA00?style=for-the-badge)](https://opensource.org/licenses/MIT)

## Descripción

Sistema de gestión para máquinas expendedoras de snacks proteicos, diseñado para administrar productos, usuarios (clientes, empleados y administradores), y transacciones comerciales. Utiliza una arquitectura en capas (Modelo-Presentación-Servicio) y tecnologías como **Guava Tables**, **Gson TypeAdapter**, y persistencia en JSON.

## Características Principales

- **Gestión de Roles**:
  - Administradores: Control total del sistema (CRUD de snacks, reportes).
  - Empleados: Registro de ventas y atención a clientes.
  - Clientes: Compra de snacks y visualización de historial.

- **Tecnologías Clave**:
  - Serialización/Deserialización JSON con **Gson 2.10.1** (TypeAdapters personalizados).
  - Estructuras de datos eficientes con **Guava Tables 32.1.1**.
  - Generación de reportes en PDF con **Apache PDFBox 2.0.27** (`PDFGeneratorService.java`).
  - Interfaz gráfica con formularios Swing personalizados en **Java 17.0.7**.

- **Funcionalidades**:
  - ABM de snacks (crear, modificar, eliminar).
  - Registro de compras con persistencia en `compras.json`.
  - Autenticación de usuarios (login/registro).
  - Reportes: Inventario, historial de compras por cliente.

## Equipo de Desarrollo

| Participante | Rol | GitHub |
|-------------|-----|--------|
| Wilmer Sepulveda | Desarrollador Full-Stack | [![GitHub](https://img.shields.io/badge/GitHub-willsepulvedam-181717?style=for-the-badge&logo=github)](https://github.com/willsepulvedam) |
| Samuel Vargas | Desarrollador Backend | [![GitHub](https://img.shields.io/badge/GitHub-shinji585-181717?style=for-the-badge&logo=github)](https://github.com/shinji585) |
| Keyner Gonzalez | Desarrollador Frontend | [![GitHub](https://img.shields.io/badge/GitHub-Keyner-G-181717?style=for-the-badge&logo=github)](https://github.com/Keyner-G) |

## Requisitos del Sistema

| Componente | Versión | Notas |
|------------|---------|-------|
| JDK        | 17.0.7+ | OpenJDK recomendado |
| Maven      | 3.9.2+  | Para gestión de dependencias |
| Memoria    | 512MB+  | Recomendado para operación fluida |
| Disco      | 100MB+  | Espacio para aplicación y datos |

## Estructura del Proyecto

```
src/
├── main/java/com/example/
│   ├── Dominio/             # Modelos del dominio
│   │   ├── Administrador.java
│   │   ├── Cliente.java
│   │   ├── Snack.java       # (nombre, precio, stock, proteínas)
│   │   └── ... 
│   ├── Presentation/        # Interfaz gráfica (Swing)
│   │   ├── MainSystem.form  # Pantalla principal
│   │   ├── PanelLoginAndRegister.java
│   │   └── ... 
│   └── Servicio/            # Lógica de negocio
│       ├── ServicioAdministradorArchivo.java  # CRUD snacks (JSON)
│       ├── ServicioClienteAcciones.java       # Gestión de compras
│       ├── PDFGeneratorService.java           # Genera reportes
│       └── ... 
│
├── resources/               # Datos y assets
│   ├── clientes.json        # Registro de usuarios
│   ├── snacks.json          # Catálogo de productos
│   └── icon/                # Imágenes para la UI
│
└── test/                    # Pruebas unitarias
    ├── PersonaTest.java
    └── SnackTest.java
```

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java       | 17.0.7  | Lenguaje base con características modernas (Records, Pattern Matching) |
| Gson       | 2.10.1  | Serialización JSON con TypeAdapters personalizados |
| Guava      | 32.1.1  | Estructuras de datos avanzadas (Tables, BiMaps) |
| Apache PDFBox | 2.0.27 | Generación de reportes en formato PDF |
| Maven      | 3.9.2   | Gestión de dependencias y construcción |

## Dependencias Maven

```xml
<dependencies>
    <!-- Google Gson -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- Google Guava -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>32.1.1-jre</version>
    </dependency>
    
    <!-- Apache PDFBox -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>2.0.27</version>
    </dependency>
    
    <!-- JUnit para pruebas -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Instalación y Ejecución

1. **Verificar requisitos**:
   ```bash
   java --version   # Debe ser 17.0.7 o superior
   mvn --version    # Debe ser 3.9.2 o superior
   ```

2. **Clonar y compilar**:
   ```bash
   git clone https://github.com/shinji585/maquina-snacks-refactor
   cd maquina-snacks
   mvn clean install
   ```

3. **Ejecutar**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.Presentation.MainSystem"
   ```

4. **Construir JAR ejecutable**:
   ```bash
   mvn package
   java -jar target/maquina-snacks-1.0.0.jar
   ```

## Uso Básico

```java
// Ejemplo: Crear un snack (Administrador)
ServicioAdministrador servicio = new ServicioAdministradorArchivo();
servicio.agregarSnack(
    new Snack("Barrita Proteica", 3.99, 50, 20.5)
);

// Ejemplo: Realizar compra (Cliente)
ServicioCliente servicioCliente = new ServicioClienteAcciones();
servicioCliente.comprarSnack(clienteId, snackId, 2); // Actualiza stock y genera PDF
```

## Documentación Técnica

- **Persistencia**: Datos almacenados en JSON (`snacks.json`, `clientes.json`).
- **TypeAdapters**: Personalizados para `LocalDateTime` y estructuras complejas.
- **Guava Tables**: Usadas para manejar inventario con claves compuestas (ej: `<TipoSnack, Marca, Snack>`).

## Estado del Proyecto

| Módulo | Estado | Progreso |
|--------|--------|----------|
| Autenticación | ✅ Completado | 100% |
| Gestión de Inventario | ✅ Completado | 100% |
| Reportes PDF | 🔄 En progreso | 80% |
| UI Móvil | 📅 Planificado | 0% |

## Contribución

1. Haz fork del proyecto.
2. Crea una rama: `git checkout -b feature/nueva-funcionalidad`.
3. Realiza tus cambios y prueba con:
   ```bash
   mvn test
   ```
4. Envía un pull request detallando los cambios.

## Licencia

[![Licencia MIT](https://img.shields.io/badge/Licencia-MIT-97CA00?style=for-the-badge)](https://opensource.org/licenses/MIT)

Ver [LICENSE](LICENSE) para más detalles.

## Contacto

- **Equipo de Desarrollo**: [equipo@snacks.com](bersekDevelopers@snacks.com)
- **Repositorio**: [github.com/tu-usuario/maquina-snacks](https://github.com/shinji585/maquina-snacks-refactor)

---

*¡Gracias a la comunidad de Gson y Guava por proveer herramientas increíbles!*