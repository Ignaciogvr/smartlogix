$body = '{"client_id":"OmTPVf437mhIS422gzlQEiE3ftSqwFOc","client_secret":"LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs","audience":"https://smartlogix-api","grant_type":"client_credentials"}'
$token = (Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json").access_token
$h = @{Authorization="Bearer $token"; "Content-Type"="application/json"}

Write-Host "=== ENDPOINTS RESTANTES ===" -ForegroundColor Cyan

# Grupo: Pedidos detallados
Write-Host "`n--- PEDIDOS ---" -ForegroundColor Yellow
@(
    @("GET", "/api/pedidos"),
    @("GET", "/api/pedidos/mis-pedidos"),
    @("GET", "/api/pedidos/usuario/test123")
) | ForEach-Object {
    try {
        $r = Invoke-WebRequest -Uri "http://localhost:8080$($_[1])" -Method $_[0] -Headers @{Authorization="Bearer $token"} -UseBasicParsing -TimeoutSec 5
        Write-Host "[OK $($r.StatusCode)] $($_[0]) $($_[1])" -ForegroundColor Green
    } catch {
        $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
        Write-Host "[FAIL $code] $($_[0]) $($_[1])" -ForegroundColor Red
    }
}

# Grupo: Envíos y tracking
Write-Host "`n--- ENVIOS Y TRACKING ---" -ForegroundColor Yellow
@(
    @("GET", "/api/envios/mis-envios"),
    @("GET", "/api/tracking/1"),
    @("GET", "/api/envios/1/seguimiento"),
    @("GET", "/api/envios/cotizar?region=RM&peso=1")
) | ForEach-Object {
    try {
        $r = Invoke-WebRequest -Uri "http://localhost:8080$($_[1])" -Method $_[0] -Headers @{Authorization="Bearer $token"} -UseBasicParsing -TimeoutSec 5
        Write-Host "[OK $($r.StatusCode)] $($_[0]) $($_[1])" -ForegroundColor Green
    } catch {
        $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
        Write-Host "[FAIL $code] $($_[0]) $($_[1])" -ForegroundColor Red
        if ($code -eq 500 -or $code -eq 400) {
            try {
                $stream = $_.Exception.Response.GetResponseStream()
                $reader = [System.IO.StreamReader]::new($stream)
                $errBody = $reader.ReadToEnd()
                Write-Host "  Error: $($errBody.Substring(0, [Math]::Min(150, $errBody.Length)))" -ForegroundColor Gray
            } catch {}
        }
    }
}

# Grupo: Carrito completo
Write-Host "`n--- CARRITO OPERACIONES ---" -ForegroundColor Yellow

# Agregar item
try {
    $r = Invoke-WebRequest -Uri "http://localhost:8080/api/carrito/items" -Method POST -Body '{"productoId":2,"cantidad":1}' -Headers $h -UseBasicParsing -TimeoutSec 5
    Write-Host "[OK $($r.StatusCode)] POST /api/carrito/items" -ForegroundColor Green
} catch {
    $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
    Write-Host "[FAIL $code] POST /api/carrito/items" -ForegroundColor Red
}

# Actualizar item
try {
    $r = Invoke-WebRequest -Uri "http://localhost:8080/api/carrito/items/2?cantidad=3" -Method PUT -Headers @{Authorization="Bearer $token"} -UseBasicParsing -TimeoutSec 5
    Write-Host "[OK $($r.StatusCode)] PUT /api/carrito/items/2" -ForegroundColor Green
} catch {
    $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
    Write-Host "[FAIL $code] PUT /api/carrito/items/2" -ForegroundColor Red
}

# Eliminar item
try {
    $r = Invoke-WebRequest -Uri "http://localhost:8080/api/carrito/items/2" -Method DELETE -Headers @{Authorization="Bearer $token"} -UseBasicParsing -TimeoutSec 5
    Write-Host "[OK $($r.StatusCode)] DELETE /api/carrito/items/2" -ForegroundColor Green
} catch {
    $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
    Write-Host "[FAIL $code] DELETE /api/carrito/items/2" -ForegroundColor Red
}

# Grupo: Admin
Write-Host "`n--- ADMIN ---" -ForegroundColor Yellow
@(
    @("GET", "/api/admin/dashboard"),
    @("GET", "/api/admin/pedidos"),
    @("GET", "/api/admin/envios")
) | ForEach-Object {
    try {
        $r = Invoke-WebRequest -Uri "http://localhost:8080$($_[1])" -Method $_[0] -Headers @{Authorization="Bearer $token"} -UseBasicParsing -TimeoutSec 5
        Write-Host "[OK $($r.StatusCode)] $($_[0]) $($_[1])" -ForegroundColor Green
    } catch {
        $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
        Write-Host "[FAIL $code] $($_[0]) $($_[1])" -ForegroundColor Red
    }
}

Write-Host "`n=== FIN ===" -ForegroundColor Cyan
