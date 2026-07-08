$body = @{
    client_id = "OmTPVf437mhIS422gzlQEiE3ftSqwFOc"
    client_secret = "LGu_pzPMxwjqkLr33MJo9wYCNzOmOOWLPkIgKh910WNh9v_pHEcv73Aijl8LsePs"
    audience = "https://smartlogix-api"
    grant_type = "client_credentials"
}

$json = $body | ConvertTo-Json
$response = Invoke-RestMethod -Uri "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/oauth/token" -Method POST -Body $json -ContentType "application/json"
Write-Output $response.access_token
