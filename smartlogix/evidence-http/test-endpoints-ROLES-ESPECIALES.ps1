# ============================================================================
# TEST DE ENDPOINTS CON ROLES ESPECÍFICOS (CHOFER, VENDEDOR) Y NO PROBADOS
# ============================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TEST ENDPOINTS - ROLES ESPECIALES" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"
$resultados = @()
$exitosos = 0
$fallidos = 0

# ============================================================================
# OBTENER TOKEN JWT ESTÁNDAR
# ============================================================================
Write-Host "=== OBTENIENDO TOKEN JWT ESTÁNDAR ===" -ForegroundColor Yellow
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
        [object]$Body = $null,
        [string]$Categoria = "General"
    )
    
    Write-Host "--- Testing: $Method $Url ---" -ForegroundColor Cyan
    Write-Host "Descripcion: $Descripcion"
    Write-Host "Categoria: $Categoria"
    
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
            Categoria = $Categoria
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
            Categoria = $Categoria
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
# GRUPO 1: BÚSQUEDA Y FILTRADO
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 1: BÚSQUEDA Y FILTRADO" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/busqueda?q=laptop" -Descripcion "Búsqueda texto libre" -Categoria "Búsqueda"
Test-Endpoint -Method "GET" -Url "/busqueda/filtrar?precioMin=10000&precioMax=100000" -Descripcion "Filtrado avanzado" -Categoria "Búsqueda"

# ============================================================================
# GRUPO 2: COMPARADOR
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 2: COMPARADOR" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/comparador?ids=1,2,3" -Descripcion "Comparar productos" -Categoria "Comparador"

# ============================================================================
# GRUPO 3: PRODUCTOS - ENDPOINTS AVANZADOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 3: PRODUCTOS - AVANZADOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/productos/1/relacionados-marca" -Descripcion "Relacionados por marca" -Categoria "Catálogo"
Test-Endpoint -Method "GET" -Url "/api/menos-vendidos" -Descripcion "Productos menos vendidos" -Categoria "Catálogo"
Test-Endpoint -Method "GET" -Url "/api/vistos" -Descripcion "Vistos recientemente" -Categoria "Catálogo"
Test-Endpoint -Method "GET" -Url "/api/recomendados" -Descripcion "Productos recomendados" -Categoria "Catálogo"
Test-Endpoint -Method "GET" -Url "/api/categorias" -Descripcion "Listar categorías" -Categoria "Catálogo"

# ============================================================================
# GRUPO 4: COMENTARIOS (CRUD)
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 4: COMENTARIOS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

$comentarioBody = @{
    contenido = "Excelente producto de prueba"
    calificacion = 5
}
Test-Endpoint -Method "POST" -Url "/api/productos/1/comentarios" -Descripcion "Agregar comentario" -Body $comentarioBody -Categoria "Comentarios"

# ============================================================================
# GRUPO 5: VISTAS DE PRODUCTOS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 5: VISTAS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "POST" -Url "/api/productos/1/vistas" -Descripcion "Registrar vista" -Categoria "Analytics"

# ============================================================================
# GRUPO 6: PEDIDOS - API V1
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 6: PEDIDOS V1" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/v1/pedidos/1/estado-completo" -Descripcion "Estado completo pedido" -Categoria "Pedidos"

# ============================================================================
# GRUPO 7: ENVÍOS - OPERACIONES ADICIONALES
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 7: ENVÍOS - ADICIONALES" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Test-Endpoint -Method "GET" -Url "/api/envios/pedido/1" -Descripcion "Envíos por pedido" -Categoria "Envíos"
Test-Endpoint -Method "GET" -Url "/api/envios/usuario/test-user" -Descripcion "Envíos por usuario" -Categoria "Envíos"

# ============================================================================
# GRUPO 8: ADMIN - OPERACIONES AVANZADAS
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 8: ADMIN - OPERACIONES" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Write-Host "NOTA: Estos endpoints pueden fallar si no hay pedidos con ID específico" -ForegroundColor Yellow

# ============================================================================
# GRUPO 9: USUARIOS - GESTIÓN AVANZADA
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 9: USUARIOS - GESTIÓN" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

# Primero obtenemos un ID de usuario válido
try {
    $usuarios = Invoke-RestMethod -Uri "$baseUrl/api/usuarios" -Method GET -Headers $headers
    if ($usuarios -and $usuarios.Count -gt 0) {
        $primerUsuarioId = $usuarios[0].id
        Write-Host "Usuario encontrado: $primerUsuarioId" -ForegroundColor Green
        
        Test-Endpoint -Method "GET" -Url "/api/usuarios/$primerUsuarioId" -Descripcion "Obtener usuario por ID" -Categoria "Usuarios"
    }
} catch {
    Write-Host "No se pudo obtener lista de usuarios" -ForegroundColor Yellow
}

# ============================================================================
# GRUPO 10: CHOFER - ENDPOINTS (requiere rol)
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 10: CHOFER - ENDPOINTS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Write-Host "NOTA: Estos endpoints requieren rol CHOFER - Se espera 403 Forbidden" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "/chofer/envios" -Descripcion "Envíos asignados (CHOFER)" -Categoria "Chofer"
Test-Endpoint -Method "PUT" -Url "/chofer/envios/1/estado" -Descripcion "Actualizar estado envío (CHOFER)" -Body @{estado="EN_RUTA"} -Categoria "Chofer"

# ============================================================================
# GRUPO 11: VENDEDOR - ENDPOINTS (requiere rol)
# ============================================================================
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "GRUPO 11: VENDEDOR - ENDPOINTS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

Write-Host "NOTA: Estos endpoints requieren rol VENDEDOR - Se espera 403 Forbidden" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "/vendedor/dashboard" -Descripcion "Dashboard vendedor" -Categoria "Vendedor"
Test-Endpoint -Method "GET" -Url "/vendedor/productos" -Descripcion "Productos del vendedor" -Categoria "Vendedor"
Test-Endpoint -Method "GET" -Url "/vendedor/pedidos" -Descripcion "Pedidos del vendedor" -Categoria "Vendedor"
Test-Endpoint -Method "GET" -Url "/vendedor/envios" -Descripcion "Envíos del vendedor" -Categoria "Vendedor"

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
$resultados | Group-Object Categoria | ForEach-Object {
    Write-Host "`n=== $($_.Name) ===" -ForegroundColor Cyan
    $_.Group | Format-Table Metodo, Endpoint, Status, Resultado -AutoSize
}

$resultados | Export-Csv -Path "test-bff-ROLES-ESPECIALES-results.csv" -NoTypeInformation
Write-Host "`nResultados exportados a: test-bff-ROLES-ESPECIALES-results.csv" -ForegroundColor Green
Write-Host ""

# ============================================================================
# ESTADÍSTICAS POR CATEGORÍA
# ============================================================================
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "ESTADÍSTICAS POR CATEGORÍA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$resultados | Group-Object Categoria | ForEach-Object {
    $total = $_.Count
    $ok = ($_.Group | Where-Object { $_.Resultado -eq "OK" }).Count
    $fail = $total - $ok
    $porcentaje = if ($total -gt 0) { [math]::Round(($ok / $total) * 100, 2) } else { 0 }
    
    $color = if ($porcentaje -ge 80) { "Green" } elseif ($porcentaje -ge 50) { "Yellow" } else { "Red" }
    $mensaje = "{0}: {1}/{2} OK ({3})" -f $_.Name, $ok, $total, $porcentaje
    Write-Host $mensaje -ForegroundColor $color
}

Write-Host ""

if ($fallidos -eq 0) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "OK - TODOS LOS ENDPOINTS PROBADOS FUNCIONAN" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    exit 0
} else {
    $porcentajeTotal = [math]::Round(($exitosos / ($exitosos + $fallidos)) * 100, 2)
    Write-Host "========================================" -ForegroundColor Yellow
    $mensajeTotal = "COMPLETADO - Tasa de exito: {0}" -f $porcentajeTotal
    Write-Host $mensajeTotal -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Yellow
    exit 0
}
