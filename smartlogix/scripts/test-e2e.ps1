# SmartLogix - Test E2E completo
# Ejecutar DESPUES de start.ps1 o cuando el sistema este levantado

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   SmartLogix - Pruebas E2E" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

# ========== PASO 1: OBTENER TOKEN ==========
Write-Host "[PASO 1] Obteniendo token M2M de Auth0..." -ForegroundColor Yellow

$tokenResponse = Invoke-RestMethod `
    -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" `
    -Method Post `
    -ContentType "application/json" `
    -Body (@{
        client_id     = "OmTPVf437mhIS422gzlQEiE3ftSqwFOc"
        client_secret = "LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs"
        audience      = "https://smartlogix-api"
        grant_type    = "client_credentials"
    } | ConvertTo-Json)

$TOKEN = $tokenResponse.access_token
$H = @{ "Authorization" = "Bearer $TOKEN"; "Content-Type" = "application/json" }

Write-Host "  Token obtenido (expira en $($tokenResponse.expires_in)s)" -ForegroundColor Green

$ok = 0; $fail = 0
$pedidoId = $null; $envioId = $null; $trackingId = $null

function Test-API($label, $method, $url, $body = $null) {
    Write-Host "`n--- $label ---" -ForegroundColor White
    Write-Host "  $method $url" -ForegroundColor DarkGray
    try {
        $params = @{ Uri = $url; Method = $method; Headers = $H }
        if ($body) { $params.Body = ($body | ConvertTo-Json -Depth 10) }
        $r = Invoke-RestMethod @params
        Write-Host "  STATUS: OK" -ForegroundColor Green
        $r | ConvertTo-Json -Depth 8 | Write-Host
        return $r
    } catch {
        $script:fail++
        try {
            $stream = $_.Exception.Response.GetResponseStream()
            $errBody = [System.IO.StreamReader]::new($stream).ReadToEnd()
            Write-Host "  STATUS: FAIL - $errBody" -ForegroundColor Red
        } catch {
            Write-Host "  STATUS: FAIL - $_" -ForegroundColor Red
        }
        return $null
    }
    $script:ok++
}

# ========== PASO 2: INVENTARIO ==========
Write-Host "`n[PASO 2] Consultar inventario" -ForegroundColor Yellow

$inv = Test-API "GET /productos - Listar catalogo" "GET" "http://localhost:8081/productos"
$stock = Test-API "GET /productos/stock/1 - Stock de Laptop ProBook G5" "GET" "http://localhost:8081/productos/stock/1"
if ($inv) { $ok++ }; if ($stock) { $ok++ }

# ========== PASO 3: USUARIO ==========
Write-Host "`n[PASO 3] Gestionar usuario" -ForegroundColor Yellow

$me = Test-API "POST /usuarios/me - Crear/obtener usuario desde token" "POST" "http://localhost:8083/usuarios/me"
$perfil = Test-API "GET /usuarios/me - Mi perfil" "GET" "http://localhost:8083/usuarios/me"
$lista = Test-API "GET /usuarios - Listar usuarios (ADMIN)" "GET" "http://localhost:8083/usuarios"
if ($me) { $ok++ }; if ($perfil) { $ok++ }; if ($lista) { $ok++ }

# ========== PASO 4: CREAR PEDIDO ==========
Write-Host "`n[PASO 4] Crear pedido" -ForegroundColor Yellow

$pedidoBody = @{
    usuarioId        = "OmTPVf437mhIS422gzlQEiE3ftSqwFOc@clients"
    direccionDestino = "Av. Libertador 1234, Buenos Aires"
    productos        = @(@{ productoId = 1; cantidad = 1; precioUnitario = 1200.0 })
}

$pedido = Test-API "POST /pedidos - Crear pedido" "POST" "http://localhost:8082/pedidos" $pedidoBody
if ($pedido) {
    $ok++
    $pedidoId = $pedido.data.id
    Write-Host "  >>> Pedido ID: $pedidoId" -ForegroundColor Cyan
}

# ========== PASO 5: PAGAR PEDIDO ==========
Write-Host "`n[PASO 5] Pagar pedido" -ForegroundColor Yellow

if ($pedidoId) {
    $pago = Test-API "PUT /pedidos/$pedidoId/pagar - Pagar pedido" "PUT" "http://localhost:8082/pedidos/$pedidoId/pagar"
    if ($pago) { $ok++ }
}

# ========== PASO 6: ESPERAR KAFKA ==========
Write-Host "`n[PASO 6] Esperando procesamiento asíncrono Kafka (5s)..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# ========== PASO 7: VERIFICAR ENVIO ==========
Write-Host "`n[PASO 7] Verificar envios generados" -ForegroundColor Yellow

$envios = Test-API "GET /api/envios/usuario/{id} - Envios del usuario" "GET" "http://localhost:8084/api/envios/usuario/OmTPVf437mhIS422gzlQEiE3ftSqwFOc@clients"
if ($envios) {
    $ok++
    # Puede ser array o un solo objeto
    $lastEnvio = if ($envios -is [array]) { $envios | Select-Object -Last 1 } else { $envios }
    $envioId   = $lastEnvio.id
    $trackingId = $lastEnvio.trackingId
    Write-Host "  >>> Envio ID: $envioId  |  Tracking: $trackingId" -ForegroundColor Cyan
}

# ========== PASO 8: TRACKING ==========
Write-Host "`n[PASO 8] Consultar tracking" -ForegroundColor Yellow

if ($envioId) {
    $envioDetalle = Test-API "GET /api/envios/$envioId - Detalle de envio" "GET" "http://localhost:8084/api/envios/$envioId"
    if ($envioDetalle) { $ok++ }
}

if ($trackingId) {
    $tracking = Test-API "GET /api/envios/tracking/$trackingId - Rastrear envio" "GET" "http://localhost:8084/api/envios/tracking/$trackingId"
    if ($tracking) { $ok++ }
}

# ========== PASO 9: VERIFICAR STOCK DESCONTADO ==========
Write-Host "`n[PASO 9] Verificar stock descontado por Kafka" -ForegroundColor Yellow
$stockFinal = Test-API "GET /productos/stock/1 - Stock post-compra" "GET" "http://localhost:8081/productos/stock/1"
if ($stockFinal) { $ok++ }

# ========== RESUMEN ==========
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "   RESUMEN DE PRUEBAS" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Pedido ID  : $pedidoId"
Write-Host "  Envio ID   : $envioId"
Write-Host "  Tracking   : $trackingId"
Write-Host ""
if ($fail -eq 0) {
    Write-Host "  RESULTADO: TODAS LAS PRUEBAS PASARON" -ForegroundColor Green
} else {
    Write-Host "  RESULTADO: $fail prueba(s) fallaron" -ForegroundColor Red
}
Write-Host "============================================`n" -ForegroundColor Cyan
