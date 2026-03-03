resource "azurerm_resource_group" "rg" {
  name     = var.resourceGroupName
  location = var.location
}

module "network" {
  source              = "./modules/network"
  resourceGroupName   = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  vnetName            = "vnet-beyond-spring-boot"
  postgresDnsZoneName = "beyond-spring-boot.postgres.database.azure.com"
}

resource "random_password" "postgresAdminPassword" {
  length           = 16
  special          = true
  override_special = "!@#$%"
}

module "database" {
  source            = "./modules/database"
  resourceGroupName = azurerm_resource_group.rg.name
  location          = azurerm_resource_group.rg.location
  serverName        = "pg-beyond-spring-boot"
  subnetId          = module.network.subnetPostgresId
  dnsZoneId         = module.network.dnsZoneId
  adminPassword     = random_password.postgresAdminPassword.result
}

module "aks" {
  source            = "./modules/aks"
  resourceGroupName = azurerm_resource_group.rg.name
  location          = azurerm_resource_group.rg.location
  clusterName       = "aks-beyond-spring-boot"
  subnetId          = module.network.subnetAksId
  depends_on        = [azurerm_resource_group.rg]
}
