# Sistema de Gestión de Turnos - Clínica

Implementación en Java basada en el diagrama UML proporcionado.

## Estructura del Proyecto

```
src/main/java/com/clinica/
├── enums/
│   └── EstadoTurno.java
├── interfaces/
│   ├── IUsuario.java
│   └── IOperaria.java
├── models/
│   ├── Paciente.java (clase abstracta)
│   ├── PacienteMayor.java
│   ├── PacienteEmbarazada.java
│   ├── PacienteRegular.java
│   └── Turno.java
├── gestor/
│   └── GestorTurnos.java (Singleton)
└── Main.java
```

## Componentes Principales

### Enums
- **EstadoTurno**: Estados posibles de un turno (PENDIENTE, ATENDIDO, CANCELADO)

### Interfaces
- **IUsuario**: Define operaciones de usuario (solicitar y cancelar turnos)
- **IOperaria**: Define operaciones de operaria (inicializar contadores, ver turnos, cambiar estado)

### Modelos
- **Paciente**: Clase abstracta que define propiedades comunes
  - **PacienteMayor**: Prefijo "PM"
  - **PacienteEmbarazada**: Prefijo "PE"
  - **PacienteRegular**: Prefijo "PR"
- **Turno**: Representa un turno con número, paciente, hora y estado

### Gestor
- **GestorTurnos**: Singleton que implementa IUsuario e IOperaria
  - Gestiona lista de turnos y pacientes registrados
  - Genera números de turno con prefijo y contador
  - Valida consistencia de datos

## Características

- Patrón Singleton para GestorTurnos
- Uso de generics (List, Map)
- Validaciones de consistencia
- Prevención de turnos duplicados activos
- Cambio dinámico de estado de turnos
- Filtrado de turnos activos

## Cómo Compilar

```bash
mvn clean compile
```

## Cómo Ejecutar

```bash
mvn clean compile exec:java -Dexec.mainClass="com.clinica.Main"
```
