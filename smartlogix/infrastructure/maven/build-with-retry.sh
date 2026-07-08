#!/bin/bash
# build-with-retry.sh
# Script que ejecuta mvn con reintentos automáticos y timeouts robustos

MAX_RETRIES=${MAX_RETRIES:-5}
RETRY_DELAY=${RETRY_DELAY:-30}
MVN_ARGS="$@"

# Agregar timeouts y configuración de Maven
MVN_OPTS="-Dmaven.wagon.http.connectTimeout=120000 \
          -Dmaven.wagon.http.readTimeout=120000 \
          -Dmaven.wagon.rto=120000 \
          -Dorg.slf4j.simpleLogger.defaultLogLevel=WARN"

retry_count=0

while [ $retry_count -lt $MAX_RETRIES ]; do
    echo "========================================="
    echo "Intento $((retry_count + 1)) de $MAX_RETRIES: mvn $MVN_ARGS"
    echo "========================================="
    
    mvn $MVN_ARGS $MVN_OPTS
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ]; then
        echo "✓ Build exitoso!"
        exit 0
    fi
    
    retry_count=$((retry_count + 1))
    
    if [ $retry_count -lt $MAX_RETRIES ]; then
        echo "✗ Build falló (exit code: $EXIT_CODE). Esperando ${RETRY_DELAY}s antes del reintento $((retry_count + 1))..."
        sleep $RETRY_DELAY
        # Incrementar delay exponencialmente
        RETRY_DELAY=$((RETRY_DELAY * 2))
    else
        echo "✗ Build falló después de $MAX_RETRIES intentos"
    fi
done

exit 1
