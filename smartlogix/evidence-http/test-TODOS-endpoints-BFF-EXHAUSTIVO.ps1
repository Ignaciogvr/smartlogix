# ============================================================================
# TEST EXHAUSTIVO DE TODOS LOS ENDPOINTS DEL BFF
# ============================================================================
# Este script prueba el 100% de endpoints expuestos por el BFF
# Incluye: GET, POST, PUT, DELETE de todos los controladores
# ============================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TEST EXHAUSTIVO BFF - TODOS LOS ENDPOINTS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"
$resultados = @()
$exitosos = 0
$fallidos = 0

# ============================================================================
# OBTENER TOKEN JWT DE AUTH0
# ============================================================================
Write-Host "=== OBTENIENDO TOKEN JWT ===" -ForegroundColor Yellow
try {
    $body = @{
        client_id = "OmTPVf437mhIS422gzlQEiE3ftSqwFOc"
        client_secret = "LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs"
        audience = "https://smartlogix-api"
        grant_type = "client_credentials"
    } | ConvertTo-Json

    $authResponse = Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json"
    $token = $authResponse.access_token
    Write-Host "Token obtenido: $($token.Substring(0,50))..." -ForegroundColor Green
} catch {
    Write-Host "ERROR: No se pudo obtener token. Abortando." -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    exit 1
}

$headers = @{
    Authorization = "Bearer $token"
}

# ============================================================================
# FUNCIÓN AUXILIAR PARA PROBAR ENDPOINTS
# ============================================================================
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Descripcion,
        [object]$Body = $null,
        [int]$ExpectedStatus = 200
    )
    
    Write-Host "--- Testing: $Method $Url ---" -ForegroundColor Cyan
    Write-Host "Descripcion: $Descripcion"
    
    try {
        $params = @{
            Uri = "$baseUrl$Url"
            Method = $Method
            Headers = $headers
        }
        
        if ($Body) {
            $params.Body = ($Body | ConvertTo-Json)
            $params.ContentType = "application/json"
        }
        
        $response = Invoke-RestMethod @params
        $statusCode = 200 # Si llega aquí, fue exitoso
        
        Write-Host "Status: $statusCode" -ForegroundColor Green
        Write-Host "Resultado: OK" -ForegroundColor Green
        
        $script:exitosos++
        $script:resultados += [PSCustomObject]@{
            Metodo = $Method
            Endpoint = $Url
            Descripcion = $Descripcion
            Status = $statusCode
            Resultado = "OK"
        }
        
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $errorMsg = $_.Exception.Message
        
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "Status: $statusCode (esperado)" -ForegroundColor Yellow
            Write-Host "Resultado: OK" -ForegroundColor Green
            $script:exitosos++
            $resultado = "OK (esperado $statusCode)"
        } else {
            Write-Host "Status: $statusCode" -ForegroundColor Red
            Write-Host "Error: $errorMsg" -ForegroundColor Red
            $script:fallidos++
            $resultado = "FAIL"
        }
        
        $script:resultados += [PSCustomObject]@{
            Metodo = $Method
            Endpoint = $Url
            Descripcion = $Descripcion
            Status = $statusCode
            Resultado = $resultado
        }
    }
    
    Write-Host ""
}

# ============================================================================
# GRUPO 1: DASHBOARD / HOME
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 1: DASHBOARD / HOME" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/home" -Descripcion "Obtener datos de home page"
Test-Endpoint -Method "GET" -Url "/home/resumen" -Descripcion "Obtener resumen estadísticas home"
Test-Endpoint -Method "GET" -Url "/destacados" -Descripcion "Productos destacados para dashboard"
Test-Endpoint -Method "GET" -Url "/admin" -Descripcion "Dashboard admin general"
Test-Endpoint -Method "GET" -Url "/ventas" -Descripcion "Dashboard ventas"

# ============================================================================
# GRUPO 2: CATÁLOGO / PRODUCTOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 2: CATÁLOGO / PRODUCTOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/productos" -Descripcion "Listar todos los productos"
Test-Endpoint -Method "GET" -Url "/activos" -Descripcion "Listar productos activos"
Test-Endpoint -Method "GET" -Url "/productos/destacados" -Descripcion "Productos destacados"
Test-Endpoint -Method "GET" -Url "/productos/ofertas" -Descripcion "Productos en oferta"
Test-Endpoint -Method "GET" -Url "/productos/nuevos" -Descripcion "Productos nuevos"
Test-Endpoint -Method "GET" -Url "/productos/1" -Descripcion "Detalle producto ID=1"
Test-Endpoint -Method "GET" -Url "/productos/1/relacionados" -Descripcion "Productos relacionados"
Test-Endpoint -Method "GET" -Url "/productos/buscar?q=laptop" -Descripcion "Buscar productos"
Test-Endpoint -Method "GET" -Url "/productos/categoria/1" -Descripcion "Productos por categoría"

# ============================================================================
# GRUPO 3: COMENTARIOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 3: COMENTARIOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/productos/1/comentarios" -Descripcion "Listar comentarios de producto"

# ============================================================================
# GRUPO 4: BANNERS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 4: BANNERS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/banners" -Descripcion "Listar banners activos"

# ============================================================================
# GRUPO 5: USUARIOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 5: USUARIOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "POST" -Url "/usuarios/me" -Descripcion "Crear/actualizar usuario desde token" -ExpectedStatus 201
Test-Endpoint -Method "GET" -Url "/usuarios/me" -Descripcion "Obtener mi perfil"
Test-Endpoint -Method "PUT" -Url "/usuarios/me" -Descripcion "Actualizar mi perfil" -Body @{ nombre = "Test User"; email = "test@example.com" }

# ============================================================================
# GRUPO 6: CARRITO
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 6: CARRITO" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/carrito" -Descripcion "Obtener mi carrito"
Test-Endpoint -Method "POST" -Url "/api/carrito/items" -Descripcion "Agregar item al carrito" -Body @{ productoId = 1; cantidad = 2 }
Test-Endpoint -Method "PUT" -Url "/api/carrito/items/1" -Descripcion "Actualizar cantidad item" -Body @{ cantidad = 3 }
Test-Endpoint -Method "DELETE" -Url "/api/carrito/items/1" -Descripcion "Eliminar item del carrito"
Test-Endpoint -Method "DELETE" -Url "/api/carrito" -Descripcion "Vaciar carrito completo"

# Volver a agregar items para checkout
Test-Endpoint -Method "POST" -Url "/api/carrito/items" -Descripcion "Re-agregar item para checkout" -Body @{ productoId = 1; cantidad = 1 }

# ============================================================================
# GRUPO 7: CHECKOUT
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 7: CHECKOUT" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "POST" -Url "/api/checkout" -Descripcion "Confirmar checkout" -Body @{ metodoPago = "TARJETA" }
Test-Endpoint -Method "POST" -Url "/api/checkout/confirmar" -Descripcion "Confirmar checkout (alias)" -Body @{ metodoPago = "TARJETA" }

# ============================================================================
# GRUPO 8: PEDIDOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 8: PEDIDOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/pedidos" -Descripcion "Listar todos mis pedidos"
Test-Endpoint -Method "GET" -Url "/api/pedidos/mis-pedidos" -Descripcion "Mis pedidos (alias)"
Test-Endpoint -Method "GET" -Url "/api/pedidos/1" -Descripcion "Obtener detalle pedido ID=1" -ExpectedStatus 404

# ============================================================================
# GRUPO 9: ENVÍOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 9: ENVÍOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/envios" -Descripcion "Listar todos los envíos"
Test-Endpoint -Method "GET" -Url "/api/envios/mis-envios" -Descripcion "Mis envíos"
Test-Endpoint -Method "GET" -Url "/api/envios/cotizar?region=RM&peso=1" -Descripcion "Cotizar envío"
Test-Endpoint -Method "GET" -Url "/api/envios/1" -Descripcion "Obtener envío por ID" -ExpectedStatus 404
Test-Endpoint -Method "GET" -Url "/api/envios/1/seguimiento" -Descripcion "Seguimiento de envío" -ExpectedStatus 404

# ============================================================================
# GRUPO 10: ADMIN - DASHBOARD
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 10: ADMIN - DASHBOARD" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/admin/dashboard" -Descripcion "Dashboard admin"

# ============================================================================
# GRUPO 11: ADMIN - PEDIDOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 11: ADMIN - PEDIDOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/admin/pedidos" -Descripcion "Listar todos los pedidos (admin)"
Test-Endpoint -Method "GET" -Url "/api/admin/pedidos/estado/PENDIENTE" -Descripcion "Pedidos por estado"

# ============================================================================
# GRUPO 12: ADMIN - ENVÍOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 12: ADMIN - ENVÍOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/admin/envios" -Descripcion "Listar todos los envíos (admin)"

# ============================================================================
# RESUMEN FINAL
# ============================================================================
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "RESUMEN FINAL" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Total endpoints probados: $($exitosos + $fallidos)" -ForegroundColor White
Write-Host "Exitosos: $exitosos" -ForegroundColor Green
Write-Host "Fallidos: $fallidos" -ForegroundColor Red
Write-Host ""

# ============================================================================
# EXPORTAR RESULTADOS
# ============================================================================
Write-Host "--- TODOS LOS RESULTADOS ---" -ForegroundColor Yellow
$resultados | Format-Table -AutoSize
$resultados | Export-Csv -Path "test-bff-EXHAUSTIVO-results.csv" -NoTypeInformation
Write-Host "Resultados exportados a: test-bff-EXHAUSTIVO-results.csv" -ForegroundColor Green
Write-Host ""

if ($fallidos -eq 0) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "OK - TODOS LOS ENDPOINTS FUNCIONAN CORRECTAMENTE" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    exit 0
} else {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "FAIL - HAY ENDPOINTS CON FALLAS" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    exit 1
}
