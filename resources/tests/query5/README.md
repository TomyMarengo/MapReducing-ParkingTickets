# Que testeamos
Probamos los siguientes casos:
- grupo compuesto por una única infracción no debe imprimirse
- grupo con promedio menor a $100 no debe imprimirse
- no se listan los pares opuestos
- orden de impresión descendente por grupo y el orden de los pares dentro de cada grupo es alfabético por infracción


# Comandos para testear query 3
Comando para testear NYC
```bash
sh client/src/main/assembly/overlay/query5.sh -Daddresses="192.168.0.208:5701" -Dcity=NYC  -DinPath=./resources/tests/query5/NYC -DoutPath=./resources/tests/query5/NYC/out
```
Comando para testear CHI
```bash
sh client/src/main/assembly/overlay/query5.sh -Daddresses="192.168.0.208:5701" -Dcity=CHI  -DinPath=./resources/tests/query5/CHI -DoutPath=./resources/tests/query5/CHI/out
```
# NYC Salida esperada
```csv
Group;Infraction A;Infraction B
700;NO STANDING-DAY/TIME LIMITS;NO STANDING-HOTEL LOADING
200;IDLING;NO PARKING-DAY/TIME LIMITS
200;IDLING;NO STANDING-OFF-STREET LOT
200;NO PARKING-DAY/TIME LIMITS;NO STANDING-OFF-STREET LOT
```

# CHI Salida esperada
```csv
Group;Infraction A;Infraction B
700;EXP. METER NON-CENTRAL BUSINESS DISTRICT;MOTOR RUNNING IN WRIGLEY BUS PERMIT ZONE
700;EXP. METER NON-CENTRAL BUSINESS DISTRICT;NO DISPLAY OF BACK-IN PERMIT
700;MOTOR RUNNING IN WRIGLEY BUS PERMIT ZONE;NO DISPLAY OF BACK-IN PERMIT
```