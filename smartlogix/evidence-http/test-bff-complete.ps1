# Test completo BFF - Endpoints uno por uno
$ErrorActionPreference = "Continue"

# Obtener token
Write-Host "=== Obteniendo token de Auth0 ===" -ForegroundColor Cyan
$body = '{"client_id":"OmTPVf437mhIS422gzlQEiE3ftSqwFOc","client_secret":"LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs","audience":"https://smartlogix-api","grant_type":"client_credentials"}'
$response = Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json"
$TOKEN = $response.access_token
Write-Host "Token obtenido: $($TOKEN.Substring(0, 50))..." -ForegroundColor Green

$BASE = "http://localhost:8080/api"
$h = @{"Authorization" = "Bearer $TOKEN"; "Content-Type" = "application/json"}

$results = @()

function Test-Endpoint {
    param([string]$Method, [string]$Endpoint, [string]$Desc, [string]$Body = $null, [string]$Svc)
    
    Write-Host "`n[$Method $Endpoint]" -ForegroundColor Yellow
    Write-Host "  Desc: $Desc" -ForegroundColor Gray
    Write-Host "  Microservicio: $Svc" -ForegroundColor Gray
    
    try {
        if ($Body) {
            $resp = Invoke-WebRequest -Uri "$BASE$Endpoint" -Method $Method -Headers $h -Body $Body -UseBasicParsing
        } else {
            $resp = Invoke-WebRequest -Uri "$BASE$Endpoint" -Method $Method -Headers $h -UseBasicParsing
        }
        $status = $resp.StatusCode
        Write-Host "  ✓ Status: $status" -ForegroundColor Green
        
        $script:results += [PSCustomObject]@{
            Endpoint = $Endpoint
            Method = $Method
            Status = $status
            Result = "OK"
            Microservicio = $Svc
        }
    } catch {
        $status = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.Value__ } else { "ERROR" }
        $msg = $_.Exception.Message
        Write-Host "  ✗ Status: $status - $msg" -ForegroundColor Red
        
        $script:results += [PSCustomObject]@{
            Endpoint = $Endpoint
            Method = $Method
            Status = $status
            Result = "FAIL"
            Microservicio = $Svc
        }
    }
}

Write-Host "`n`n========== GRUPO 1: HOME ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/home" "Home page data" "" "inventory-service"
Test-Endpoint "GET" "/home/resumen" "Home summary stats" "" "inventory-service"

Write-Host "`n`n========== GRUPO 2: CATALOGO ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/productos" "Listar productos" "" "inventory-service"
Test-Endpoint "GET" "/productos/destacados" "Destacados" "" "inventory-service"
Test-Endpoint "GET" "/productos/ofertas" "Ofertas" "" "inventory-service"
Test-Endpoint "GET" "/productos/nuevos" "Nuevos" "" "inventory-service"
Test-Endpoint "GET" "/productos/1" "Detalle producto" "" "inventory-service"
Test-Endpoint "GET" "/productos/1/relacionados" "Relacionados" "" "inventory-service"
Test-Endpoint "GET" "/productos/buscar?q=laptop" "Buscar" "" "inventory-service"

Write-Host "`n`n========== GRUPO 3: COMENTARIOS ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/productos/1/comentarios" "Listar comentarios" "" "inventory-service"

Write-Host "`n`n========== GRUPO 4: BANNERS ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/banners" "Listar banners" "" "inventory-service"

Write-Host "`n`n========== GRUPO 5: USUARIOS ==========" -ForegroundColor Magenta
Test-Endpoint "POST" "/usuarios/me" "Crear usuario desde token" "" "usuarios-service"
Test-Endpoint "GET" "/usuarios/me" "Mi perfil" "" "usuarios-service"

Write-Host "`n`n========== GRUPO 6: CARRITO ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/carrito" "Obtener carrito" "" "pedidos-service"
Test-Endpoint "POST" "/carrito/items" "Agregar item" '{"productoId":1,"cantidad":2}' "pedidos-service"
Test-Endpoint "PUT" "/carrito/items/1?cantidad=3" "Actualizar cantidad" "" "pedidos-service"
Test-Endpoint "DELETE" "/carrito/items/1" "Eliminar item" "" "pedidos-service"

Write-Host "`n`n========== GRUPO 7: CHECKOUT ==========" -ForegroundColor Magenta
# Primero agregar item
Test-Endpoint "POST" "/carrito/items" "Agregar para checkout" '{"productoId":1,"cantidad":1}' "pedidos-service"
Test-Endpoint "POST" "/checkout" "Procesar checkout" '{"direccionEnvio":"Av. Siempre Viva 123, Santiago","metodoPago":"TRANSFERENCIA","telefonoContacto":"+56912345678","notasEntrega":"Tocar timbre"}' "pedidos-service"

Write-Host "`n`n========== GRUPO 8: PEDIDOS ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/pedidos/mis-pedidos" "Mis pedidos" "" "pedidos-service"
Test-Endpoint "GET" "/pedidos/1" "Detalle pedido" "" "pedidos-service"

Write-Host "`n`n========== GRUPO 9: ENVIOS / TRACKING ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/envios/mis-envios" "Mis envios" "" "envio-service"
Test-Endpoint "GET" "/tracking/1" "Tracking" "" "envio-service"

Write-Host "`n`n========== GRUPO 10: ADMIN ==========" -ForegroundColor Magenta
Test-Endpoint "GET" "/admin/dashboard" "Dashboard admin" "" "Varios"
Test-Endpoint "GET" "/admin/pedidos" "Todos pedidos" "" "pedidos-service"
Test-Endpoint "GET" "/admin/envios" "Todos envios" "" "envio-service"

Write-Host "`n`n========== RESUMEN FINAL ==========" -ForegroundColor Cyan
$total = $results.Count
$ok = ($results | Where-Object { $_.Result -eq "OK" }).Count
$fail = ($results | Where-Object { $_.Result -eq "FAIL" }).Count

Write-Host "`nTotal: $total" -ForegroundColor White
Write-Host "OK: $ok" -ForegroundColor Green
Write-Host "FAIL: $fail" -ForegroundColor Red

if ($fail -gt 0) {
    Write-Host "`n--- ENDPOINTS FALLIDOS ---" -ForegroundColor Red
    $results | Where-Object { $_.Result -eq "FAIL" } | Format-Table -AutoSize
}

Write-Host "`n--- TODOS LOS RESULTADOS ---" -ForegroundColor Cyan
$results | Format-Table -AutoSize

$results | Export-Csv -Path "test-bff-results-final.csv" -NoTypeInformation -Encoding UTF8
Write-Host "`nResultados en: test-bff-results-final.csv" -ForegroundColor Green
