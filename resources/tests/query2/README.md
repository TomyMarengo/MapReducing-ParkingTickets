# Que testeamos
Para NYC probamos los siguientes casos:
- Un barrio con una sola infracción (Kings)
- Un barrio con dos infracciones (Queens)
- Barrio con más de cuatro infracciones devuelva bien el top3 (Bronx)

Para CHI probamos los siguientes casos:
- Un barrio con una sola infracción (LOOP)
- Un barrio con dos infracciones (LINCOLN PARK)
- Barrio con más de cuatro infracciones devuelva bien el top3 (SOUTH LAWNDALE)

# Correr los tests
Correr los siguientes comandos para testear con NYC
```bash
mkdir -p ./resources/tests/query2/NYC/out
sh client/src/main/assembly/overlay/query2.sh -Daddresses="192.168.0.208:5701" -Dcity=NYC -DinPath=./resources/tests/query2/NYC -DoutPath=./resources/tests/query2/NYC/out -Dn=3
```
Correr los siguientes comandos para testear con CHI
```bash
mkdir -p ./resources/tests/query2/CHI/out
sh client/src/main/assembly/overlay/query2.sh -Daddresses="192.168.0.208:5701" -Dcity=CHI -DinPath=./resources/tests/query2/CHI -DoutPath=./resources/tests/query2/CHI/out -Dn=3
```
# NYC Respuesta esperada
```csv
County;InfractionTop1;InfractionTop2;InfractionTop3
Bronx;NO STOPPING-DAY/TIME LIMITS;NO STANDING-HOTEL LOADING;IDLING
Kings;PHTO SCHOOL ZN SPEED VIOLATION;-;-
Queens;PHTO SCHOOL ZN SPEED VIOLATION;FAIL TO DSPLY MUNI METER RECPT;-
```

# CHI Respuesta esperada
```csv
County;InfractionTop1;InfractionTop2;InfractionTop3
LINCOLN PARK;EXPIRED METER CENTRAL BUSINESS DISTRICT;EXP. METER NON-CENTRAL BUSINESS DISTRICT;-
LOOP;RUSH HOUR PARKING;-;-
SOUTH LAWNDALE;RUSH HOUR PARKING;EXP. METER NON-CENTRAL BUSINESS DISTRICT;RESIDENTIAL PERMIT PARKING
```