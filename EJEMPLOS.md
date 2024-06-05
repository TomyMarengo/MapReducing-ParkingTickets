# Ejemplos de Uso

## Primeros Pasos

1. Para poder correr un cliente utilizando las addresses X e Y, primero se debe correr por lo menos un servidor en una interfaz habilitada.

Definiendo, por ejemplo X=192.168.1.* y Y=192.168.1.*, se pueden correr dos nodos en la misma máquina.

```bash
sh server/src/main/assembly/overlay/run-server.sh -Daddress="192.168.1.*"
```

```bash
sh server/src/main/assembly/overlay/run-server.sh -Daddress="192.168.1.*"
```

2. Crea una carpeta resources en el directorio de trabajo del proyecto. Y una carpeta out dentro de esta.

```bash
mkdir -p resources
mkdir -p resources/out
```

En la carpeta resources deberias tener dos datasets de las ciudades que quieras utilizar, y un archivo json con el orden de las columnas de los archivos de tickets.

Por ejemplo, para NYC serían necesarios:

* `resources/infractionsNYC.csv`
* `resources/ticketsNYC.csv`
* `resources/ticketsNYC.json`

En ticketsNYC.json deberías tener algo como esto:

```json
{
  "plate": 0,
  "issueDate": 1,
  "infractionCode": 2,
  "fineAmount": 3,
  "countyName": 4,
  "issuingAgency": 5
}
```

## Queries

Luego de tener los servidores corriendo, se puede correr un cliente

* para la query N,
* utilizando los nodos con IP:Puerto "Z" y "W",
* usando el dataset de NYC, 

con el siguiente comando:

```bash
sh client/src/main/assembly/overlay/queryN.sh -Daddresses="Z;W" -Dcity=NYC -DinPath=./resources -DoutPath=./resources/out
```

### Otras opciones

* En el caso de la **query 3**, se puede definir el valor de N con el parámetro `-Dn=N`.

* En el caso de la **query 4**, se puede definir el rango de fechas con los parámetros `-Dfrom='dd/MM/yyyy'` y `-Dto='dd/MM/yyyy'`.

### Query 1: Total de multas por infracción

Utilizando dos nodos y el dataset de NYC:
```bash
sh client/src/main/assembly/overlay/query1.sh -Daddresses="192.168.1.51:5701;192.168.1.52:5701" -Dcity=NYC -DinPath=./resources -DoutPath=./resources/out
```

### Query 2: Top 3 infracciones más populares de cada barrio

Utilizando un nodo y el dataset de NYC:
```bash
sh client/src/main/assembly/overlay/query2.sh -Daddresses="192.168.1.51:5701" -Dcity=NYC -DinPath=./resources -DoutPath=./resources/out
```

### Query 3: Top N agencias con mayor porcentaje de recaudación

Utilizando un nodo, el dataset de CHI y n=4:
```bash
sh client/src/main/assembly/overlay/query3.sh -Daddresses="192.168.1.51:5701" -Dcity=CHI -Dn=4 -DinPath=./resources -DoutPath=./resources/out
```

### Query 4: Patente con más infracciones de cada barrio en el rango [from, to]

Utilizando dos nodos, el dataset de NYC y el rango de fechas [01/01/2017, 31/12/2017]:
```bash
sh client/src/main/assembly/overlay/query4.sh -Daddresses="192.168.1.51:5701" -Dcity=NYC -Dfrom='01/01/2017' -Dto='31/12/2017' -DinPath=./resources -DoutPath=./resources/out
```

### Query 5: Pares de infracciones que tienen, en grupos de a cientos, el mismo promedio de monto de multa

Utilizando dos nodos y el dataset de CHI:
```bash
sh client/src/main/assembly/overlay/query5.sh -Daddresses="192.168.1.51:5701;192.168.1.52:5701" -Dcity=CHI -DinPath=./resources -DoutPath=./resources/out
```