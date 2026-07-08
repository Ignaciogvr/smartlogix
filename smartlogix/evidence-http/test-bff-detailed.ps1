# TEST BFF CON DETALLES COMPLETOS DE ERRORES
Write-Host "=== OBTENIENDO TOKEN ===" -ForegroundColor Cyan
$body = '{"client_id":"OmTPVf437mhIS422gzlQEiE3ftSqwFOc","client_secret":"LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs","audience":"https://smartlogix-api","grant_type":"client_credentials"}'
$response = Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $body -ContentType "application/json"
$TOKEN = $response.access_token
Write-Host "Token OK" -ForegroundColor Green

$h = @{"Authorization"="Bearer $TOKEN"; "Content-Type"="application/json"}
$BASE = "http://localhost:8080/api"

function Test-Detailed {
    param($method, $path, $desc, $body=$null)
    
    Write-Host "`n========================================" -ForegroundColor Yellow
    Write-Host "ENDPOINT: $method $path" -ForegroundColor Yellow
    Write-Host "DESCRIPCION: $desc" -ForegroundColor Gray
    Write-Host "URL COMPLETA: $BASE$path" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = "$BASE$path"
            Method = $method
            Headers = $h
            TimeoutSec = 10
            UseBasicParsing = $true
            ErrorAction = "Stop"
        }
        
        if ($body) {
            $jsonBody = $body | ConvertTo-Json -Depth 5 -Compress
            $params.Body = $jsonBody
            Write-Host "BODY: $jsonBody" -ForegroundColor Gray
        }
        
        $resp = Invoke-WebRequest @params
        $statusCode = $resp.StatusCode
        $content = $resp.Content
        
        Write-Host "✓ STATUS CODE: $statusCode" -ForegroundColor Green
        Write-Host "✓ RESPONSE (primeros 200 chars): $($content.Substring(0, [Math]::Min(200, $content.Length)))" -ForegroundColor Green
        
        return @{ok=$true; code=$statusCode}
        
    } catch {
        $statusCode = "N/A"
        $errorMsg = $_.Exception.Message
        $responseBody = ""
        
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
            try {
                $stream = $_.Exception.Response.GetResponseStream()
                $reader = [System.IO.StreamReader]::new($stream)
                $responseBody = $reader.ReadToEnd()
            } catch {
                $responseBody = "No se pudo leer el cuerpo de la respuesta"
            }
        }
        
        Write-Host "✗ STATUS CODE: $statusCode" -ForegroundColor Red
        Write-Host "✗ ERROR MESSAGE: $errorMsg" -ForegroundColor Red
        Write-Host "✗ RESPONSE BODY: $responseBody" -ForegroundColor Red
        
        # Mostrar logs del BFF si es error 500
        if ($statusCode -eq 500 -or $statusCode -eq 502 -or $statusCode -eq 503) {
            Write-Host "`n--- LOGS DEL BFF (últimas 10 líneas) ---" -ForegroundColor Magenta
            $logs = docker logs bff-service --tail 10 2>&1 | Out-String
            Write-Host $logs -ForegroundColor Gray
        }
        
        return @{ok=$false; code=$statusCode; error=$errorMsg; body=$responseBody}
    }
}

# TEST ENDPOINTS UNO POR UNO CON DETALLES
Write-Host "`n`n========================================" -ForegroundColor Cyan
Write-Host "INICIANDO TESTS DETALLADOS" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$results = @()

# HOME
$r = Test-Detailed "GET" "/home" "Home page"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter para continuar al siguiente endpoint..." }

$r = Test-Detailed "GET" "/home/resumen" "Home resumen"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter..." }

# PRODUCTOS
$r = Test-Detailed "GET" "/productos" "Listar productos"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter..." }

$r = Test-Detailed "GET" "/productos/destacados" "Productos destacados"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter..." }

# USUARIOS
$r = Test-Detailed "POST" "/usuarios/me" "Crear usuario"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter..." }

$r = Test-Detailed "GET" "/usuarios/me" "Mi perfil"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter..." }

# CARRITO
$r = Test-Detailed "GET" "/carrito" "Obtener carrito"
$results += $r
if (-not $r.ok) { Read-Host "Presiona Enter..." }

# RESUMEN
Write-Host "`n`n========================================" -ForegroundColor Cyan
Write-Host "RESUMEN FINAL" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$ok = ($results | Where-Object { $_.ok -eq $true }).Count
$fail = ($results | Where-Object { $_.ok -eq $false }).Count

Write-Host "`nTotal: $($results.Count)"
Write-Host "OK: $ok" -ForegroundColor Green
Write-Host "FAIL: $fail" -ForegroundColor Red
