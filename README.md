# MapReducing-ParkingTickets
MapReduce con Hazelcast en Java. Proporciona respuesta a consultas predefinidas sobre conjuntos de datos de multas de estacionamiento.

## Índice

- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
  - [Ejemplos de Uso](#ejemplos-de-uso)
- [Licencia](#licencia)

# Requisitos
* Java >= 20.0.0
* Apache Maven >= 3.9.4.

# Instalación

Para instalar y compilar MapReducing-ParkingTickets, primero clona el repositorio:

```bash
git clone https://github.com/TomyMarengo/MapReducing-ParkingTickets.git
```

Luego, compila el programa con el siguiente comando maven:

```bash
mvn clean install
```

# Uso

### Ejecución del Servidor
Para ejecutar el servidor, sigue estos pasos:

1. Abre una terminal.
2. Ubicate en la carpeta padre del proyecto.
3. Ejecuta el script run-server con el siguiente comando:

```bash
sh server/src/main/assembly/overlay/run-server.sh #[opciones]
```

O en windows:

```bash
server/src/main/assembly/overlay/run-server.bat #[opciones]
```

* **[opciones]**: Puedes proporcionar opciones adicionales según sea necesario. Consulta el archivo [EJEMPLOS.md](EJEMPLOS.md) para ver ejemplos de uso.

### Ejecución del Cliente

Para ejecutar el cliente, sigue estos pasos:

1. Abre una terminal.
2. Ubicate en la carpeta padre del proyecto.
3. Ejecuta el script de alguna query (N) con el siguiente comando:

```bash
sh client/src/main/assembly/overlay/queryN.sh #[opciones]
```

O en windows:

```bash
client/src/main/assembly/overlay/queryN.bat #[opciones]
```

* **[opciones]**: Puedes proporcionar opciones adicionales según sea necesario. Consulta el archivo [EJEMPLOS.md](EJEMPLOS.md) para ver ejemplos de uso.

## Ejemplos de Uso

Para ver ejemplos de uso, consulte el archivo [EJEMPLOS.md](EJEMPLOS.md).

# Licencia

[MIT](LICENSE)