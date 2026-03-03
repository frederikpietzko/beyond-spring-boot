variable "location" {
  type = string
}

variable "resourceGroupName" {
  type = string
}

variable "vnetName" {
  type = string
}

variable "addressSpace" {
  type    = list(string)
  default = ["10.0.0.0/16"]
}

variable "subnetAksName" {
  type    = string
  default = "subnetAks"
}

variable "subnetAksPrefix" {
  type    = list(string)
  default = ["10.0.1.0/24"]
}

variable "subnetPostgresName" {
  type    = string
  default = "subnetPostgres"
}

variable "subnetPostgresPrefix" {
  type    = list(string)
  default = ["10.0.2.0/24"]
}

variable "postgresDnsZoneName" {
  type    = string
  default = "beyond-spring-boot.postgres.database.azure.com"
}
