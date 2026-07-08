docker run -d --name bff-service --network smartlogix_smartlogix-net -p 8080:8080 -e AUTH0_DOMAIN=dev-nomnv0fhn3zpzt4t.us.auth0.com -e AUTH0_AUDIENCE=https://smartlogix-api -e INVENTORY_SERVICE_URL=http://inventory-service:8081 -e PEDIDOS_SERVICE_URL=http://pedidos-service:8082 -e USUARIOS_SERVICE_URL=http://usuarios-service:8083 -e ENVIO_SERVICE_URL=http://envio-service:8084 smartlogix-bff-service:latest
Start-Sleep -Seconds 20
docker ps --filter "name=bff-service" --format "{{.Names}}: {{.Status}}"
