# MapReducing-ParkingTickets
MapReduce con Hazelcast en Java. Proporciona respuesta a consultas predefinidas sobre conjuntos de datos de multas de estacionamiento.

## Índice

- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Ejemplos](#ejemplos)
- [Licencia](#licencia)

# Requisitos
Se requiere Java >=17.0.0 y Apache Maven >=3.5.0.

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

Para ejecutar el programa, primero se debe compilar el proyecto con maven. Luego, se debe ejecutar el servidor y los clientes.

### Server
Para ejecutar el servidor, se debe correr el siguiente comando:

```bash
sh server/src/main/assembly/overlay/run-server.sh
```

O en windows:

```bash
server/src/main/assembly/overlay/run-server.bat
```

### Client

Para ejecutar un cliente, se debe correr el siguiente comando:

```bash
sh client/src/main/assembly/overlay/run-client.sh
```

O en windows:

```bash
client/src/main/assembly/overlay/run-client.bat
```

# Ejemplos

Para ver ejemplos de uso, consulte el archivo [EJEMPLOS.md](EJEMPLOS.md).

# Licencia

[MIT](LICENSE)