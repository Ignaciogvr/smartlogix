# TEST COMPLETO BFF - TODOS LOS ENDPOINTS
Write-Host "=== OBTENIENDO TOKEN ===" -ForegroundColor Cyan
$token = (Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -ContentType "application/json" -Body '{"client_id":"OmTPVf437mhIS422gzlQEiE3ftSqwFOc","client_secret":"LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs","audience":"https://smartlogix-api","grant_type":"client_credentials"}').access_token
$h = @{"Authorization"="Bearer $token"; "Content-Type"="application/json"}
$BASE = "http://localhost:8080/api"
Write-Host "Token obtenido OK" -ForegroundColor Green

$okCount = 0
$failCount = 0

function Test-EP {
    param($method, $path, $desc, $body=$null)
    try {
        $params = @{Uri="$BASE$path"; Method=$method; Headers=$h; TimeoutSec=10; UseBasicParsing=$true; ErrorAction="Stop"}
        if ($body) { $params.Body = ($body | ConvertTo-Json -Depth 5 -Compress) }
        $r = Invoke-WebRequest @params
        Write-Host "[OK $($r.StatusCode)] $method $path - $desc" -ForegroundColor Green
        $script:okCount++
        return $true
    } catch {
        $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
        Write-Host "[FAIL $code] $method $path - $desc" -ForegroundColor Red
        $script:failCount++
        return $false
    }
}

Write-Host "`n=== GRUPO 1: HOME ===" -ForegroundColor Magenta
Test-EP "GET" "/home" "Home data"
Test-EP "GET" "/home/resumen" "Home summary"

Write-Host "`n=== GRUPO 2: CATALOGO ===" -ForegroundColor Magenta
Test-EP "GET" "/productos" "Listar productos"
Test-EP "GET" "/productos/destacados" "Productos destacados"
Test-EP "GET" "/productos/ofertas" "Productos en oferta"
Test-EP "GET" "/productos/nuevos" "Productos nuevos"
Test-EP "GET" "/productos/menos-vendidos" "Productos menos vendidos"
Test-EP "GET" "/productos/1" "Detalle producto ID=1"
Test-EP "GET" "/productos/1/relacionados" "Productos relacionados"
Test-EP "GET" "/productos/buscar?q=laptop" "Buscar productos"

Write-Host "`n=== GRUPO 3: COMENTARIOS ===" -ForegroundColor Magenta
Test-EP "GET" "/productos/1/comentarios" "Comentarios del producto"

Write-Host "`n=== GRUPO 4: BANNERS ===" -ForegroundColor Magenta
Test-EP "GET" "/banners" "Listar banners activos"

Write-Host "`n=== GRUPO 5: USUARIOS ===" -ForegroundColor Magenta
Test-EP "POST" "/usuarios/me" "Crear usuario desde token"
Test-EP "GET" "/usuarios/me" "Obtener mi perfil"
Test-EP "PUT" "/usuarios/me" "Actualizar perfil" @{nombre="Test User"}

Write-Host "`n=== GRUPO 6: CARRITO ===" -ForegroundColor Magenta
Test-EP "GET" "/carrito" "Obtener carrito"
Test-EP "POST" "/carrito/items" "Agregar item" @{productoId=1;cantidad=2}
Test-EP "GET" "/carrito" "Ver carrito con items"
Test-EP "PUT" "/carrito/items/1?cantidad=3" "Actualizar cantidad"
Test-EP "DELETE" "/carrito/items/1" "Eliminar item"

Write-Host "`n=== GRUPO 7: CHECKOUT ===" -ForegroundColor Magenta
Test-EP "POST" "/carrito/items" "Agregar para checkout" @{productoId=1;cantidad=1}
Test-EP "POST" "/checkout" "Procesar checkout" @{direccionEnvio="Av Test 123";metodoPago="TRANSFERENCIA";telefonoContacto="+56912345678"}

Write-Host "`n=== GRUPO 8: PEDIDOS ===" -ForegroundColor Magenta
Test-EP "GET" "/pedidos/mis-pedidos" "Mis pedidos"
Test-EP "GET" "/pedidos/1" "Detalle pedido ID=1"

Write-Host "`n=== GRUPO 9: ENVIOS ===" -ForegroundColor Magenta
Test-EP "GET" "/envios/mis-envios" "Mis envios"
Test-EP "GET" "/tracking/1" "Tracking envio ID=1"

Write-Host "`n=== GRUPO 10: ADMIN ===" -ForegroundColor Magenta
Test-EP "GET" "/admin/dashboard" "Dashboard admin"
Test-EP "GET" "/admin/pedidos" "Todos los pedidos"
Test-EP "GET" "/admin/envios" "Todos los envios"

Write-Host "`n`n========== RESUMEN FINAL ==========" -ForegroundColor Cyan
$total = $okCount + $failCount
Write-Host "Total: $total" -ForegroundColor White
Write-Host "OK: $okCount" -ForegroundColor Green
Write-Host "FAIL: $failCount" -ForegroundColor Red

if ($failCount -eq 0) {
    Write-Host "`n✓✓✓ TODOS LOS ENDPOINTS DEL BFF FUNCIONAN ✓✓✓" -ForegroundColor Green
    Write-Host "Backend listo para conectar con frontend" -ForegroundColor Green
} else {
    Write-Host "`n⚠ Hay endpoints fallidos que requieren corrección" -ForegroundColor Yellow
}
