output "resourceGroupName" {
  value = azurerm_resource_group.rg.name
}

output "aksClusterName" {
  value = module.aks.clusterName
}

output "postgresHost" {
  value = module.database.host
}

output "postgresUsername" {
  value = module.database.username
}

output "postgresPassword" {
  value     = module.database.password
  sensitive = true
}

output "postgresDatabase" {
  value = module.database.database
}

output "aksConnectCommand" {
  value = "az aks get-credentials --resource-group ${azurerm_resource_group.rg.name} --name ${module.aks.clusterName} --overwrite-existing"
}

output "jumphostPublicIp" {
  value = module.jumphost.publicIp
}
