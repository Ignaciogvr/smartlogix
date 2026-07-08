# ============================================================================
# TEST DE ENDPOINTS NO PROBADOS DEL BFF
# ============================================================================
# Este script prueba SOLO los endpoints que NO están en el test de 29 endpoints
# ============================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TEST ENDPOINTS NO PROBADOS - BFF" -ForegroundColor Cyan
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
    exit 1
}

$headers = @{
    Authorization = "Bearer $token"
    "Content-Type" = "application/json"
}

# ============================================================================
# FUNCIÓN AUXILIAR
# ============================================================================
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Descripcion,
        [object]$Body = $null
    )
    
    Write-Host "--- Testing: $Method $Url ---" -ForegroundColor Cyan
    Write-Host "Descripcion: $Descripcion"
    
    try {
        $params = @{
            Uri = "$baseUrl$Url"
            Method = $Method
            Headers = $headers
            UseBasicParsing = $true
        }
        
        if ($Body) {
            $params.Body = ($Body | ConvertTo-Json -Depth 5)
        }
        
        $response = Invoke-WebRequest @params
        $statusCode = $response.StatusCode
        
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
        $statusCode = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERROR" }
        $errorMsg = $_.Exception.Message
        
        Write-Host "Status: $statusCode" -ForegroundColor Red
        Write-Host "Error: $errorMsg" -ForegroundColor Red
        $script:fallidos++
        
        $script:resultados += [PSCustomObject]@{
            Metodo = $Method
            Endpoint = $Url
            Descripcion = $Descripcion
            Status = $statusCode
            Resultado = "FAIL"
        }
    }
    
    Write-Host ""
}

# ============================================================================
# GRUPO 1: /api/home - ENDPOINTS ADICIONALES
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 1: /api/home - NO PROBADOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/home" -Descripcion "Home principal (wrapper)"
Test-Endpoint -Method "GET" -Url "/api/home/destacados" -Descripcion "Destacados desde /api/home"
Test-Endpoint -Method "GET" -Url "/api/home/ofertas" -Descripcion "Ofertas desde /api/home"
Test-Endpoint -Method "GET" -Url "/api/home/nuevos" -Descripcion "Nuevos desde /api/home"
Test-Endpoint -Method "GET" -Url "/api/home/banners" -Descripcion "Banners desde /api/home"
Test-Endpoint -Method "GET" -Url "/api/home/resumen" -Descripcion "Resumen desde /api/home"

# ============================================================================
# GRUPO 2: /dashboard - ENDPOINTS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 2: /dashboard - NO PROBADOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/dashboard/home" -Descripcion "Dashboard home"
Test-Endpoint -Method "GET" -Url "/dashboard/destacados" -Descripcion "Dashboard destacados"
Test-Endpoint -Method "GET" -Url "/dashboard/admin" -Descripcion "Dashboard admin"
Test-Endpoint -Method "GET" -Url "/dashboard/ventas" -Descripcion "Dashboard ventas"

# ============================================================================
# GRUPO 3: /api/tracking - TRACKING ADICIONAL
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 3: /api/tracking - NO PROBADOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/tracking/1" -Descripcion "Tracking por ID numérico"
Test-Endpoint -Method "GET" -Url "/api/tracking/codigo/TRK123" -Descripcion "Tracking por código"

# ============================================================================
# GRUPO 4: /api/usuarios - GESTIÓN AVANZADA
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 4: /api/usuarios - GESTIÓN" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/usuarios" -Descripcion "Listar todos los usuarios"
Test-Endpoint -Method "GET" -Url "/api/usuarios/internal/auth0|123" -Descripcion "Validar existencia usuario"

# ============================================================================
# GRUPO 5: /api/envios - ENDPOINTS ADICIONALES
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 5: /api/envios - ADICIONALES" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/envios/tracking/TRK123" -Descripcion "Tracking por código en envíos"

# ============================================================================
# GRUPO 6: /api/admin - OPERACIONES ADMIN
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 6: /api/admin - OPERACIONES" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/admin/pedidos/estado/PENDIENTE" -Descripcion "Pedidos por estado PENDIENTE"
Test-Endpoint -Method "GET" -Url "/api/admin/pedidos/estado/CONFIRMADO" -Descripcion "Pedidos por estado CONFIRMADO"
Test-Endpoint -Method "GET" -Url "/api/admin/pedidos/estado/ENVIADO" -Descripcion "Pedidos por estado ENVIADO"

# ============================================================================
# GRUPO 7: /api - CATÁLOGO ADICIONAL
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 7: /api - CATÁLOGO ADICIONAL" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/activos" -Descripcion "Productos activos"
Test-Endpoint -Method "GET" -Url "/api/productos/categoria/1" -Descripcion "Productos por categoría"

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
Write-Host "--- RESULTADOS DETALLADOS ---" -ForegroundColor Yellow
$resultados | Format-Table -AutoSize
$resultados | Export-Csv -Path "test-bff-NO-PROBADOS-results.csv" -NoTypeInformation
Write-Host "Resultados exportados a: test-bff-NO-PROBADOS-results.csv" -ForegroundColor Green
Write-Host ""

if ($fallidos -eq 0) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "OK - TODOS LOS ENDPOINTS ADICIONALES FUNCIONAN" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    exit 0
} else {
    Write-Host "========================================" -ForegroundColor Yellow
    Write-Host "ATENCION - HAY ENDPOINTS CON FALLAS" -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Yellow
    exit 1
}
