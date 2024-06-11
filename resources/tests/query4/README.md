# Que testeamos
Estamos testeando los siguientes casos borde
- Una o mas de una infraccion antes del periodo
- Una o mas de una infraccion durante el periodo
- Una o mas de una infraccion luego del periodo
- Sin infracciones en el periodo
- La misma patente en distintos barrios
- Orden alfabetico de barrios

# Correr los tests
Correr los siguientes comandos para testear con NYC
```bash
mkdir -p ./resources/tests/query4/NYC/out
sh client/src/main/assembly/overlay/query4.sh -Daddresses="192.168.0.208:5701" -Dcity=NYC -DinPath=./resources/tests/query4/NYC -DoutPath=./resources/tests/query4/NYC/out -Dfrom='01/01/2002' -Dto='01/01/2005'
```
Correr los siguientes comandos para testear con CHI
```bash
mkdir -p ./resources/tests/query4/CHI/out
sh client/src/main/assembly/overlay/query4.sh -Daddresses="192.168.0.208:5701" -Dcity=CHI -DinPath=./resources/tests/query4/CHI -DoutPath=./resources/tests/query4/CHI/out -Dfrom='01/01/2002' -Dto='01/01/2005'
```
# NYC Respuesta esperada
```csv
County,Plate,Tickets
```

# CHI Respuesta esperada
```csv
LINCOLN PARK,3ac2ab0e3e3b5650d244a5144f8504b9a9c9ebefa10f855cdd414733596a96ef,2
LOOP,5af770a13cea30216706196afbfd08e6fa97a623a1d7a8b96a259901ddc399bd,1
NEAR EAST SIDE,378961210d9f1c916f39cdcb9b278c8c3593b84d27bf90fc6ec8a5f18f8d5025,1
NEAR NORTH SIDE,8df0227fb8b46ab982e659377ec530f13f0e1df780577b9ea920319e0fb201c3,1
NEAR WEST SIDE,378961210d9f1c916f39cdcb9b278c8c3593b84d27bf90fc6ec8a5f18f8d5025,4
WEST GARFIELD PARK,378961210d9f1c916f39cdcb9b278c8c3593b84d27bf90fc6ec8a5f18f8d5025,1
```