$token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5mWDVfR2dabWw1dkktRk56QWhXciJ9.eyJpc3MiOiJodHRwczovL2Rldi1ub21udjBmaG4zenB6dDR0LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJPbVRQVmY0MzdtaElTNDIyZ3psUUVpRTNmdFNxd0ZPY0BjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9zbWFydGxvZ2l4LWFwaSIsImlhdCI6MTc4MTE5MDg2MiwiZXhwIjoxNzgxMjc3MjYyLCJzY29wZSI6IkFETUlOIENMSUVOVEUiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMiLCJhenAiOiJPbVRQVmY0MzdtaElTNDIyZ3psUUVpRTNmdFNxd0ZPYyIsInBlcm1pc3Npb25zIjpbIkFETUlOIiwiQ0xJRU5URSJdfQ.CaISZFtVmI4RTnRKci8h36-vGF9AMSrgZwhl2B5csO8SXJlnfbdcsQxZFI3K-wGNDBnduJaKFyXAFKZg4a5sTOdvLrtN8kNMvmekUqy_vKb-W6Gh16E8gB9MONWAznOMK8gEK_HNYA2tqUiha1tFu76rOk_azfhyMtIsxEXPSsI4X3zHUrtSg-_IZ1CsCXAo9iDm-3uRLksRd-GQBDfIfT6CT1K084d4ZjJiZiP30gKpPpHcTfQ3Ux_6yJGtrq1ZXj7cV5asBWSl4I-zgMp62PYbRn-BdS1Bcra_JmdXXsH1pamZ5dFJENoaIOUPZDs4rgO54DTMRgbyJgHaLVaazA"
$body = @{
    usuarioId="OmTPVf437mhIS422gzlQEiE3ftSqwFOc@clients"
    direccionDestino="Calle Verdadera 456"
    productos=@(
        @{productoId=1; cantidad=1; precioUnitario=1200.0}
    )
} | ConvertTo-Json -Depth 10

try {
    Write-Host ">>> CREANDO PEDIDO"
    $response = Invoke-RestMethod -Uri "http://localhost:8082/pedidos" -Method Post -Headers @{Authorization="Bearer $token"} -Body $body -ContentType "application/json"
    $response | ConvertTo-Json -Depth 10
    $pedidoId = $response.data.id

    Write-Host "`n>>> PAGANDO PEDIDO $pedidoId"
    $res2 = Invoke-RestMethod -Uri "http://localhost:8082/pedidos/$pedidoId/pagar" -Method Put -Headers @{Authorization="Bearer $token"}
    $res2 | ConvertTo-Json -Depth 10

    Write-Host "`n>>> ESPERANDO EVENTOS KAFKA..."
    Start-Sleep -Seconds 5

    Write-Host "`n>>> ENVIOS DEL USUARIO:"
    $res3 = Invoke-RestMethod -Uri "http://localhost:8084/api/envios/usuario/OmTPVf437mhIS422gzlQEiE3ftSqwFOc@clients" -Method Get -Headers @{Authorization="Bearer $token"}
    $res3 | ConvertTo-Json -Depth 10
} catch {
    $streamReader = [System.IO.StreamReader]::new($_.Exception.Response.GetResponseStream())
    $ErrResp = $streamReader.ReadToEnd()
    $streamReader.Close()
    Write-Host "Error: " $ErrResp
}
