variable "vpc_cidr_block" {
    default = "10.0.0.0/16"
}

variable "private_subnet_cidr_blocks" {
    default = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "public_subnet_cidr_blocks" {
    default = ["10.0.7.0/24", "10.0.8.0/24", "10.0.9.0/24"]
}