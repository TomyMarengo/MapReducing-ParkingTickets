# What we test
We are testing the following edge cases:
- No infractions for X infraction code
- 1 infraction for X infraction code
- More than one infraction for X infraction code

# Run query 1 tests
Run the command to test NYC
```bash
sh client/src/main/assembly/overlay/query1.sh -Daddresses="192.168.0.208:5701" -Dcity=NYC -DinPath=./resources/tests/query1/NYC -DoutPath=./resources/tests/query1/NYC/out
```
Run the command to test CHI
```bash
sh client/src/main/assembly/overlay/query1.sh -Daddresses="192.168.0.208:5701" -Dcity=CHI -DinPath=./resources/tests/query1/CHI -DoutPath=./resources/tests/query1/CHI/out
```
# NYC Expected output
```csv
Infraction;Tickets
PHTO SCHOOL ZN SPEED VIOLATION;2
SAFETY ZONE;1
```

# CHI Expected output
```csv
Infraction;Tickets
RUSH HOUR PARKING;2
EXPIRED METER CENTRAL BUSINESS DISTRICT;1
```