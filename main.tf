terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.5.0"
    }
  }
  required_version = ">=0.14.9"
  backend "gcs" {
	  bucket = "my-tfstate-bucket"   # GCS bucket name to store terraform tfstate
	  prefix = "first-app"           # Update to desired prefix name. Prefix name should be unique for each Terraform project having same remote state bucket.
  }
}

# Specify the GCP Provider
provider "google" {
  credentials = file("D:\\work\\learn_gcp\\git\\spring-cloud-storage-ex\\src\\main\\resources\\gcp-30032023-d58bfeb6860b.json")
  project     = var.project_id
  region      = var.region
}

# Create a GCS Bucket
resource "google_storage_bucket" "tf_bucket" {
  name     = var.bucket_name
  location = var.region
}