terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.5.0"
    }
  }
  required_version = ">=0.14.9"

}

# Specify the GCP Provider
provider "google" {
  credentials = file("D:\\work\\learn_gcp\\git\\spring-cloud-storage-ex\\gcp-30032023-c32fb511ffec.json")
  project     = var.project_id
  region      = var.region
}

# Create a GCS Bucket
resource "google_storage_bucket" "tf_bucket" {
  name     = var.bucket_name
  location = var.region
}

