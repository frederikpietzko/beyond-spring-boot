resource "azurerm_public_ip" "jumphost" {
  name                = "pip-jumphost"
  location            = var.location
  resource_group_name = var.resourceGroupName
  allocation_method   = "Static"
  sku                 = "Standard"
}

resource "azurerm_network_security_group" "jumphost" {
  name                = "nsg-jumphost"
  location            = var.location
  resource_group_name = var.resourceGroupName

  security_rule {
    name                       = "SSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

resource "azurerm_network_interface" "jumphost" {
  name                = "nic-jumphost"
  location            = var.location
  resource_group_name = var.resourceGroupName

  ip_configuration {
    name                          = "internal"
    subnet_id                     = var.subnetId
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.jumphost.id
  }
}

resource "azurerm_network_interface_security_group_association" "jumphost" {
  network_interface_id      = azurerm_network_interface.jumphost.id
  network_security_group_id = azurerm_network_security_group.jumphost.id
}

resource "azurerm_linux_virtual_machine" "jumphost" {
  name                = "vm-jumphost"
  resource_group_name = var.resourceGroupName
  location            = var.location
  size                = "Standard_D2s_v3"
  admin_username      = "azureuser"
  network_interface_ids = [
    azurerm_network_interface.jumphost.id,
  ]

  admin_ssh_key {
    username   = "azureuser"
    public_key = var.sshPublicKey
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts"
    version   = "latest"
  }

  tags = {
    managed_by  = "opentofu"
    environment = "prod"
  }
}

output "publicIp" {
  value = azurerm_public_ip.jumphost.ip_address
}
