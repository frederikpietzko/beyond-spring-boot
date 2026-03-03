terraform {
  required_providers {
    azapi = {
      source = "azure/azapi"
    }
  }
}

data "azurerm_subscription" "current" {}

resource "azapi_resource" "aks" {
  type      = "Microsoft.ContainerService/managedClusters@2024-09-01"
  name      = var.clusterName
  location  = var.location
  parent_id = "${data.azurerm_subscription.current.id}/resourceGroups/${var.resourceGroupName}"

  schema_validation_enabled = false

  body = {
    identity = {
      type = "SystemAssigned"
    }
    properties = {
      dnsPrefix = var.clusterName
      agentPoolProfiles = [
        {
          name         = "system"
          count        = 1
          vmSize       = "Standard_D2s_v6"
          osType       = "Linux"
          mode         = "System"
          vnetSubnetID = var.subnetId
          type         = "VirtualMachineScaleSets"
        }
      ]
      networkProfile = {
        networkPlugin   = "azure"
        loadBalancerSku = "standard"
        serviceCidr     = "172.16.0.0/16"
        dnsServiceIP    = "172.16.0.10"
      }
      enableRBAC = true
    }
    tags = {
      managed_by  = "opentofu"
      environment = "prod"
    }
  }
}

resource "azurerm_kubernetes_cluster_node_pool" "apps" {
  name                  = "apps"
  kubernetes_cluster_id = azapi_resource.aks.id
  vm_size               = "Standard_D4s_v6"
  node_count            = 1
  vnet_subnet_id        = var.subnetId

  node_labels = {
    pool = "apps"
  }

  tags = {
    managed_by  = "opentofu"
    environment = "prod"
  }
}

resource "azurerm_kubernetes_cluster_node_pool" "loadtest" {
  name                  = "loadtest"
  kubernetes_cluster_id = azapi_resource.aks.id
  vm_size               = "Standard_D2s_v6"
  node_count            = 2
  vnet_subnet_id        = var.subnetId

  node_labels = {
    pool = "loadtest"
  }

  tags = {
    managed_by  = "opentofu"
    environment = "prod"
  }
}

output "clusterName" {
  value = var.clusterName
}

output "resourceGroupName" {
  value = var.resourceGroupName
}
