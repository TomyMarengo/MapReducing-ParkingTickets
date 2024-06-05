# MapReducing-ParkingTickets
MapReduce con Hazelcast en Java. Proporciona respuesta a consultas predefinidas sobre conjuntos de datos de multas de estacionamiento.

## Índice

- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Funcionamiento General](#funcionamiento-general)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Management Center](#management-center)
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

# Funcionamiento General

MapReducing-ParkingTickets utiliza Hazelcast para ejecutar consultas MapReduce sobre conjuntos de datos de multas de estacionamiento. Para ello es necesario ejecutar por lo menos un servidor que actuará como nodo y un cliente que enviará las consultas.

## Ejecución del Servidor
Para ejecutar el servidor, sigue estos pasos:

1. Abre una terminal.
2. Ubicate en la carpeta padre del proyecto.
3. Ejecuta el script run-server con el siguiente comando:

```bash
sh server/src/main/assembly/overlay/run-server.sh #[opciones]
```

### **[opciones]**:
* **-Daddress** = Network Interface Address

Puedes proporcionar opciones adicionales según sea necesario. Consulta el archivo [EJEMPLOS.md](EJEMPLOS.md) para ver ejemplos de uso.

## Ejecución del Cliente

Para ejecutar el cliente, sigue estos pasos:

1. Abre una terminal.
2. Ubicate en la carpeta padre del proyecto.
3. Ejecuta el script de alguna query (N) con el siguiente comando:

```bash
sh client/src/main/assembly/overlay/queryN.sh #[opciones]
```

### **[opciones]**: 
* **-Daddresses** = Direcciones de los nodos del cluster separadas por punto y coma.
* **-DinPath** = Path a la carpeta que contiene los archivos CSV de entrada.
* **-DoutPath** = Path a la carpeta donde se guardarán los archivos de salida queryN.csv y timeN.txt.
* **-Dcity** = Ciudad de la que se quieren obtener los datos.
* **-Dn** = Límite para la query 3.
* **-Dfrom** = Fecha de inicio para la query 4.
* **-Dto** = Fecha de fin para la query 4.

Puedes proporcionar opciones adicionales según sea necesario. Consulta el archivo [EJEMPLOS.md](EJEMPLOS.md) para ver ejemplos de uso.

# Ejemplos de Uso

Para ver ejemplos de uso, consulte el archivo [EJEMPLOS.md](EJEMPLOS.md).

# Management Center

Para instalar y ejecutar el Management Center, consulte el archivo [MANAGEMENT_CENTER.md](MANAGEMENT_CENTER.md).

# Licencia

[MIT](LICENSE)