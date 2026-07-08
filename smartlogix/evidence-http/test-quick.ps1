# TEST RÁPIDO CON DETALLES
$body = '{"client_id":"OmTPVf437mhIS422gzlQEiE3ftSqwFOc","client_secret":"LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs","audience":"https://smartlogix-api","grant_type":"client_credentials"}'
$token = (Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json").access_token
$h = @{"Authorization"="Bearer $token"}

$endpoints = @(
    @("GET", "/api/home"),
    @("GET", "/api/productos"),
    @("GET", "/api/usuarios/me"),
    @("GET", "/api/carrito")
)

foreach ($ep in $endpoints) {
    try {
        $r = Invoke-WebRequest -Uri "http://localhost:8080$($ep[1])" -Method $($ep[0]) -Headers $h -UseBasicParsing -TimeoutSec 5
        Write-Host "[OK $($r.StatusCode)] $($ep[0]) $($ep[1])" -ForegroundColor Green
    } catch {
        $code = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "ERR" }
        Write-Host "[FAIL $code] $($ep[0]) $($ep[1])" -ForegroundColor Red
        if ($code -eq 500) {
            Write-Host "  Logs BFF:" -ForegroundColor Yellow
            docker logs bff-service --tail 5 2>&1 | ForEach-Object { Write-Host "    $_" -ForegroundColor Gray }
        }
    }
}
