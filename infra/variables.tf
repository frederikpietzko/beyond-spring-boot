variable "location" {
  type        = string
  description = "The location for all resources."
  default     = "West Europe"
}

variable "resourceGroupName" {
  type        = string
  description = "The name of the resource group."
  default     = "rg-beyond-spring-boot"
}
