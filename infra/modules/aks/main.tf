module "aks" {
  source              = "Azure/aks/azurerm"
  version             = "~> 9.0"
  prefix              = var.clusterName
  cluster_name        = var.clusterName
  resource_group_name = var.resourceGroupName
  location            = var.location
  vnet_subnet_id      = var.subnetId

  # Default node pool (system)
  agents_pool_name                  = "system"
  agents_size                       = "Standard_D2s_v3"
  agents_count                      = 1
  identity_type                     = "SystemAssigned"
  network_plugin                    = "azure"
  load_balancer_sku                 = "standard"
  log_analytics_workspace_enabled   = false
  role_based_access_control_enabled = true
}

resource "azurerm_kubernetes_cluster_node_pool" "apps" {
  name                  = "apps"
  kubernetes_cluster_id = module.aks.aks_id
  vm_size               = "Standard_D8s_v5"
  node_count            = 1
  vnet_subnet_id        = var.subnetId

  node_labels = {
    pool = "apps"
  }
}

resource "azurerm_kubernetes_cluster_node_pool" "loadtest" {
  name                  = "loadtest"
  kubernetes_cluster_id = module.aks.aks_id
  vm_size               = "Standard_D8s_v5"
  node_count            = 2
  vnet_subnet_id        = var.subnetId

  node_labels = {
    pool = "loadtest"
  }
}

output "clusterName" {
  value = module.aks.aks_name
}

output "resourceGroupName" {
  value = var.resourceGroupName
}
