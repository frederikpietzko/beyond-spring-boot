resource "azurerm_virtual_network" "vnet" {
  name                = var.vnetName
  location            = var.location
  resource_group_name = var.resourceGroupName
  address_space       = var.addressSpace
}

resource "azurerm_subnet" "subnetAks" {
  name                 = var.subnetAksName
  resource_group_name  = var.resourceGroupName
  virtual_network_name = azurerm_virtual_network.vnet.name
  address_prefixes     = var.subnetAksPrefix
}

resource "azurerm_subnet" "subnetPostgres" {
  name                 = var.subnetPostgresName
  resource_group_name  = var.resourceGroupName
  virtual_network_name = azurerm_virtual_network.vnet.name
  address_prefixes     = var.subnetPostgresPrefix

  delegation {
    name = "delegation"
    service_delegation {
      name    = "Microsoft.DBforPostgreSQL/flexibleServers"
      actions = ["Microsoft.Network/virtualNetworks/subnets/join/action"]
    }
  }
}

resource "azurerm_private_dns_zone" "dnsZonePostgres" {
  name                = var.postgresDnsZoneName
  resource_group_name = var.resourceGroupName
}

resource "azurerm_private_dns_zone_virtual_network_link" "dnsLink" {
  name                  = "dnsLink"
  resource_group_name   = var.resourceGroupName
  private_dns_zone_name = azurerm_private_dns_zone.dnsZonePostgres.name
  virtual_network_id    = azurerm_virtual_network.vnet.id
}

output "vnetId" {
  value = azurerm_virtual_network.vnet.id
}

output "subnetAksId" {
  value = azurerm_subnet.subnetAks.id
}

output "subnetPostgresId" {
  value = azurerm_subnet.subnetPostgres.id
}

output "dnsZoneId" {
  value = azurerm_private_dns_zone.dnsZonePostgres.id
}

output "dnsZoneName" {
  value = azurerm_private_dns_zone.dnsZonePostgres.name
}
