# Que testeamos
Estamos testeando los siguientes casos borde
- Sin infracciones para X codigo de infraccion
- 1 infraccion para X codigo de infraccion
- Mas de una infraccion para X codigo de infraccion

# Correr los tests
Correr los siguientes comandos para testear con NYC
```bash
mkdir -p ./resources/tests/query1/NYC/out
sh client/src/main/assembly/overlay/query1.sh -Daddresses="192.168.0.208:5701" -Dcity=NYC -DinPath=./resources/tests/query1/NYC -DoutPath=./resources/tests/query1/NYC/out
```
Correr los siguientes comandos para testear con CHI
```bash
mkdir -p ./resources/tests/query1/CHI/out
sh client/src/main/assembly/overlay/query1.sh -Daddresses="192.168.0.208:5701" -Dcity=CHI -DinPath=./resources/tests/query1/CHI -DoutPath=./resources/tests/query1/CHI/out
```
# NYC Respuesta esperada
```csv
Infraction;Tickets
PHTO SCHOOL ZN SPEED VIOLATION;2
SAFETY ZONE;1
```

# CHI Respuesta esperada
```csv
Infraction;Tickets
RUSH HOUR PARKING;2
EXPIRED METER CENTRAL BUSINESS DISTRICT;1
```