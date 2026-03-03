resource "azurerm_postgresql_flexible_server" "postgres" {
  name                          = var.serverName
  resource_group_name           = var.resourceGroupName
  location                      = var.location
  version                       = "16"
  delegated_subnet_id           = var.subnetId
  private_dns_zone_id           = var.dnsZoneId
  public_network_access_enabled = false

  administrator_login    = var.adminLogin
  administrator_password = var.adminPassword

  storage_mb = 32768
  sku_name   = "GP_Standard_D2ds_v4"
}

resource "azurerm_postgresql_flexible_server_database" "db" {
  name      = var.dbName
  server_id = azurerm_postgresql_flexible_server.postgres.id
  collation = "en_US.utf8"
  charset   = "utf8"
}

output "host" {
  value = azurerm_postgresql_flexible_server.postgres.fqdn
}

output "username" {
  value = azurerm_postgresql_flexible_server.postgres.administrator_login
}

output "password" {
  value     = azurerm_postgresql_flexible_server.postgres.administrator_password
  sensitive = true
}

output "database" {
  value = azurerm_postgresql_flexible_server_database.db.name
}
