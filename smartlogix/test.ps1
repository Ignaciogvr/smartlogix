# Script para ejecutar todas las pruebas unitarias y generar reportes de cobertura
# Este script se encarga de:
# 1. Ejecutar pruebas en todos los servicios
# 2. Generar reportes de cobertura con JaCoCo
# 3. Consolidar métricas

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " EVALUACIÓN PARCIAL 3 - PRUEBAS UNITARIAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$services = @(
    "usuarios-service",
    "pedidos-service",
    "inventory-service",
    "envio-service",
    "bff-service"
)

$testResults = @()

foreach ($service in $services) {
    Write-Host "📦 Ejecutando pruebas en $service..." -ForegroundColor Yellow
    Write-Host "-----------------------------" -ForegroundColor Gray
    
    Push-Location $service
    
    try {
        # Limpiar y compilar
        & mvnw.cmd clean test | Out-Host
        
        $exitCode = $LASTEXITCODE
        
        if ($exitCode -eq 0) {
            Write-Host "✅ Pruebas exitosas en $service" -ForegroundColor Green
            $testResults += [PSCustomObject]@{
                Service = $service
                Status = "PASS"
                ExitCode = $exitCode
            }
        } else {
            Write-Host "⚠️  Algunas pruebas fallaron en $service" -ForegroundColor Yellow
            $testResults += [PSCustomObject]@{
                Service = $service
                Status = "FAIL"
                ExitCode = $exitCode
            }
        }
    }
    catch {
        Write-Host "❌ Error al ejecutar pruebas en $service" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        $testResults += [PSCustomObject]@{
            Service = $service
            Status = "ERROR"
            ExitCode = -1
        }
    }
    finally {
        Pop-Location
    }
    
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " RESUMEN DE PRUEBAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$testResults | Format-Table -AutoSize

Write-Host ""
Write-Host "📊 Los reportes de cobertura están disponibles en:" -ForegroundColor Cyan
foreach ($service in $services) {
    Write-Host "   - $service/target/site/jacoco/index.html" -ForegroundColor Gray
}

Write-Host ""
Write-Host "✅ Pruebas completadas" -ForegroundColor Green
