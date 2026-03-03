variable "location" {
  type = string
}

variable "resourceGroupName" {
  type = string
}

variable "serverName" {
  type = string
}

variable "subnetId" {
  type = string
}

variable "dnsZoneId" {
  type = string
}

variable "adminLogin" {
  type    = string
  default = "benchmarkUser"
}

variable "adminPassword" {
  type      = string
  sensitive = true
}

variable "dbName" {
  type    = string
  default = "benchmark"
}
