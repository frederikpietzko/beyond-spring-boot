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

variable "sshPublicKey" {
  type        = string
  description = "The public key for the jumphost."
  default     = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQC+GMxXzXSqRTwcLeZNbET0V1uEyAI/z2/ay1Z5VaIE0gEA4RaRcMuCY4hHbBX9M4VAnI5a6907KIv2Qmbf/fzX71KydrZZzdPnb1q4A/+afErGpc9p3XUQjwmDH2gkKPRKsY1dPqdHAdZZLw18ZkpTC5Ag2mCtR+ZZinwa32h8Ro0gC6E4jUhyuqROvZS4/auiwQlOoAviNgkxOTXes4NXqy1fsGOvsrYpyYCIVsAyn2aCbNYFJZlmMy5MoRzXNYZiQ1nQ/i5BdhozdnZZP+wQVSt9rj+YksGLyVxcSD20PUTHDrEN+Vid4TdZiYPHPTbZpe/flHYT8YBlJEg7YDfIdduzwiPvhYEUdWmCANHS3mx4QXIvA++QyblaBJHjyjxut17C5SpYiaia+gckzh9gQDYi6j5GCBjpkgANFOVlZTJhQ1groN605LXq2ajFJzcZFU6Xdx3tgtJir5+nIGFh3Mbi4EmJwZMVUeQhpA3gQhbsM/N8PoDZahKIPpLHqpO5Sx1LNIPZzj9lD4DekOhozOUMuEztTWBKqAJl6Uzg3NGfMyUGvACdVEGXHONqNhtnyxPyVDKENbgWA7RDm2G8ZJfXhwh3IqHTL3M9qK7rVTzT3i2sw7c2QvKETCtzYjNNY+N93QSqbDCEYDktDvKC5bQi4t4GTBB1Brt9bxgq0Q== frederikpietzko@Frederiks-MacBook-Pro.local"
}
