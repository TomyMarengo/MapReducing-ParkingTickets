#!/bin/bash

# Parámetro adicional para identificar la consulta
ADDITIONAL_PARAM="-Dquery=5"

# Ejecuta el script run-client.sh con el parámetro adicional
sh client/src/main/assembly/overlay/run-client.sh "$@" "$ADDITIONAL_PARAM"