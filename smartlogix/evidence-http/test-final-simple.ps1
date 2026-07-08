$body = '{"client_id":"OmTPVf437mhIS422gzlQEiE3ftSqwFOc","client_secret":"LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs","audience":"https://smartlogix-api","grant_type":"client_credentials"}'
$token = (Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json").access_token
$h = @{Authorization="Bearer $token"; "Content-Type"="application/json"}

$endpoints = @(
    @("GET", "/api/home"),
    @("GET", "/api/productos"),
    @("GET", "/api/productos/destacados"),
    @("GET", "/api/productos/ofertas"),
    @("GET", "/api/productos/nuevos"),
    @("GET", "/api/productos/buscar?q=laptop"),
    @("GET", "/api/banners"),
    @("GET", "/api/usuarios/me"),
    @("GET", "/api/carrito"),
    @("POST", "/api/checkout")
)

$ok = 0
$fail = 0

foreach ($ep in $endpoints) {
    try {
        $params = @{Uri="http://localhost:8080$($ep[1])"; Method=$ep[0]; Headers=$h; TimeoutSec=5; UseBasicParsing=$true}
        if ($ep[0] -eq "POST") { $params.Body = '{"direccionEnvio":"Calle 123","metodoPago":"TRANSFERENCIA"}' }
        $r = Invoke-WebRequest @params
        Write-Host "[OK $($r.StatusCode)] $($ep[0]) $($ep[1])" -ForegroundColor Green
        $ok++
    } catch {
        $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
        Write-Host "[FAIL $code] $($ep[0]) $($ep[1])" -ForegroundColor Red
        $fail++
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "RESUMEN: $ok OK | $fail FAIL | Total: $($ok+$fail)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
