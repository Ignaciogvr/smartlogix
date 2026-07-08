# Test completo de todos los endpoints del BFF
# Fecha: 2026-07-06

$ErrorActionPreference = "Continue"

# Obtener token de Auth0
Write-Host "`n=== OBTENIENDO TOKEN JWT ===" -ForegroundColor Cyan
$body = @{
    client_id = "OmTPVf437mhIS422gzlQEiE3ftSqwFOc"
    client_secret = "LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs"
    audience = "https://smartlogix-api"
    grant_type = "client_credentials"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json" -ErrorAction Stop
$TOKEN = $response.access_token
Write-Host "Token obtenido: $($TOKEN.Substring(0, 50))..." -ForegroundColor Green

$BASE_URL = "http://localhost:8080/api"
$headers = @{
    "Authorization" = "Bearer $TOKEN"
    "Content-Type" = "application/json"
}

$resultados = @()

function Test-Endpoint {
    param(
        [string]$Metodo,
        [string]$Endpoint,
        [string]$Descripcion,
        [hashtable]$Body = $null,
        [string]$Microservicio
    )
    
    Write-Host "`n--- Testing: $Metodo $Endpoint ---" -ForegroundColor Yellow
    Write-Host "Descripcion: $Descripcion"
    Write-Host "Microservicio: $Microservicio"
    
    try {
        $url = "$BASE_URL$Endpoint"
        
        if ($Body) {
            $jsonBody = $Body | ConvertTo-Json -Depth 5
            $response = Invoke-WebRequest -Uri $url -Method $Metodo -Headers $headers -Body $jsonBody -UseBasicParsing
        } else {
            $response = Invoke-WebRequest -Uri $url -Method $Metodo -Headers $headers -UseBasicParsing
        }
        
        $statusCode = $response.StatusCode
        $resultado = "OK"
        $color = "Green"
        
        Write-Host "Status: $statusCode" -ForegroundColor $color
        Write-Host "Resultado: $resultado" -ForegroundColor $color
        
        $script:resultados += [PSCustomObject]@{
            Endpoint = $Endpoint
            Metodo = $Metodo
            Descripcion = $Descripcion
            Microservicio = $Microservicio
            StatusCode = $statusCode
            Resultado = $resultado
            Error = ""
        }
        
    } catch {
        $statusCode = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.Value__ } else { "N/A" }
        $errorMsg = $_.Exception.Message
        
        Write-Host "Status: $statusCode" -ForegroundColor Red
        Write-Host "Error: $errorMsg" -ForegroundColor Red
        
        $script:resultados += [PSCustomObject]@{
            Endpoint = $Endpoint
            Metodo = $Metodo
            Descripcion = $Descripcion
            Microservicio = $Microservicio
            StatusCode = $statusCode
            Resultado = "FAIL"
            Error = $errorMsg
        }
    }
}

# ===================================
# GRUPO 1: HOME / LANDING
# ===================================
Write-Host "`n`n========== GRUPO 1: HOME / LANDING ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/home" -Descripcion "Datos home page" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/home/resumen" -Descripcion "Resumen estadísticas" -Microservicio "inventory-service"

# ===================================
# GRUPO 2: CATÁLOGO
# ===================================
Write-Host "`n`n========== GRUPO 2: CATALOGO ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/productos" -Descripcion "Listar todos productos" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/productos/destacados" -Descripcion "Productos destacados" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/productos/ofertas" -Descripcion "Productos en oferta" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/productos/nuevos" -Descripcion "Productos nuevos" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/productos/1" -Descripcion "Detalle producto ID=1" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/productos/1/relacionados" -Descripcion "Productos relacionados" -Microservicio "inventory-service"
Test-Endpoint -Metodo "GET" -Endpoint "/productos/buscar?q=laptop" -Descripcion "Buscar productos" -Microservicio "inventory-service"

# ===================================
# GRUPO 3: COMENTARIOS
# ===================================
Write-Host "`n`n========== GRUPO 3: COMENTARIOS ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/productos/1/comentarios" -Descripcion "Listar comentarios producto" -Microservicio "inventory-service"

# ===================================
# GRUPO 4: BANNERS
# ===================================
Write-Host "`n`n========== GRUPO 4: BANNERS ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/banners" -Descripcion "Listar banners activos" -Microservicio "inventory-service"

# ===================================
# GRUPO 5: USUARIOS
# ===================================
Write-Host "`n`n========== GRUPO 5: USUARIOS ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "POST" -Endpoint "/usuarios/me" -Descripcion "Crear usuario desde token" -Microservicio "usuarios-service"
Test-Endpoint -Metodo "GET" -Endpoint "/usuarios/me" -Descripcion "Obtener mi perfil" -Microservicio "usuarios-service"

# ===================================
# GRUPO 6: CARRITO
# ===================================
Write-Host "`n`n========== GRUPO 6: CARRITO ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/carrito" -Descripcion "Obtener carrito activo" -Microservicio "pedidos-service"
Test-Endpoint -Metodo "POST" -Endpoint "/carrito/items" -Descripcion "Agregar item al carrito" -Microservicio "pedidos-service" -Body @{
    productoId = 1
    cantidad = 2
}
Test-Endpoint -Metodo "PUT" -Endpoint "/carrito/items/1?cantidad=3" -Descripcion "Actualizar cantidad item" -Microservicio "pedidos-service"
Test-Endpoint -Metodo "DELETE" -Endpoint "/carrito/items/1" -Descripcion "Eliminar item del carrito" -Microservicio "pedidos-service"

# ===================================
# GRUPO 7: CHECKOUT
# ===================================
Write-Host "`n`n========== GRUPO 7: CHECKOUT ==========" -ForegroundColor Magenta

# Primero agregamos items al carrito
Test-Endpoint -Metodo "POST" -Endpoint "/carrito/items" -Descripcion "Agregar item para checkout" -Microservicio "pedidos-service" -Body @{
    productoId = 1
    cantidad = 1
}

Test-Endpoint -Metodo "POST" -Endpoint "/checkout" -Descripcion "Procesar checkout" -Microservicio "pedidos-service" -Body @{
    direccionEnvio = "Av. Siempre Viva 123, Santiago"
    metodoPago = "TRANSFERENCIA"
    telefonoContacto = "+56912345678"
    notasEntrega = "Tocar el timbre"
}

# ===================================
# GRUPO 8: PEDIDOS
# ===================================
Write-Host "`n`n========== GRUPO 8: PEDIDOS ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/pedidos/mis-pedidos" -Descripcion "Listar mis pedidos" -Microservicio "pedidos-service"
Test-Endpoint -Metodo "GET" -Endpoint "/pedidos/1" -Descripcion "Detalle de pedido" -Microservicio "pedidos-service"

# ===================================
# GRUPO 9: ENVÍOS / TRACKING
# ===================================
Write-Host "`n`n========== GRUPO 9: ENVIOS / TRACKING ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/envios/mis-envios" -Descripcion "Listar mis envíos" -Microservicio "envio-service"
Test-Endpoint -Metodo "GET" -Endpoint "/tracking/codigo/TRK123456" -Descripcion "Tracking por código" -Microservicio "envio-service"

# ===================================
# GRUPO 10: ADMIN - DASHBOARD
# ===================================
Write-Host "`n`n========== GRUPO 10: ADMIN - DASHBOARD ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/admin/dashboard" -Descripcion "Dashboard admin" -Microservicio "Varios (pedidos, envio)"

# ===================================
# GRUPO 11: ADMIN - PEDIDOS
# ===================================
Write-Host "`n`n========== GRUPO 11: ADMIN - PEDIDOS ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/admin/pedidos" -Descripcion "Listar todos pedidos (admin)" -Microservicio "pedidos-service"

# ===================================
# GRUPO 12: ADMIN - ENVÍOS
# ===================================
Write-Host "`n`n========== GRUPO 12: ADMIN - ENVIOS ==========" -ForegroundColor Magenta

Test-Endpoint -Metodo "GET" -Endpoint "/admin/envios" -Descripcion "Listar todos envíos (admin)" -Microservicio "envio-service"

# ===================================
# RESUMEN FINAL
# ===================================
Write-Host "`n`n========== RESUMEN FINAL ==========" -ForegroundColor Cyan

$total = $resultados.Count
$ok = ($resultados | Where-Object { $_.Resultado -eq "OK" }).Count
$fail = ($resultados | Where-Object { $_.Resultado -eq "FAIL" }).Count

Write-Host "`nTotal endpoints probados: $total" -ForegroundColor White
Write-Host "Exitosos: $ok" -ForegroundColor Green
Write-Host "Fallidos: $fail" -ForegroundColor Red

if ($fail -gt 0) {
    Write-Host "`n--- ENDPOINTS FALLIDOS ---" -ForegroundColor Red
    $resultados | Where-Object { $_.Resultado -eq "FAIL" } | Format-Table -AutoSize
}

Write-Host "`n--- TODOS LOS RESULTADOS ---" -ForegroundColor Cyan
$resultados | Format-Table -AutoSize

# Exportar resultados a archivo
$resultados | Export-Csv -Path "test-bff-results.csv" -NoTypeInformation -Encoding UTF8
Write-Host "`nResultados exportados a: test-bff-results.csv" -ForegroundColor Green
