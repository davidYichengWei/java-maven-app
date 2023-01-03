variable "vpc_cidr_block" {
    description = "IP range for the VPC"
    default = "10.0.0.0/16"
}

variable "subnet_cidr_block" {
    description = "IP range for the subnet"
    default = "10.0.10.0/24"
}

variable "az" {
    description = "Availability zone"
    default = "us-east-1a"
}

variable "environment" {
    description = "Type of the environment"
    default = "dev"
}

variable "my-ip" {
    description = "IP address allowed to connect to the EC2 instance"
    default = "184.148.154.235/32"
}

variable "instance_type" {
    description = "Type of the EC2 instance"
    default = "t2.micro"
}


variable "image_name" {
    description = "Name of the image"
    default = "amzn2-ami-kernel-*-x86_64-gp2"
}