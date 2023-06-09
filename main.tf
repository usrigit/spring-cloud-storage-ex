terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.5.0"
    }
  }
  required_version = ">=0.14.9"
  backend "gcs" {
    bucket = "tf_state_storage_gcp2023"
    prefix = "terraform/state"
  }

}

# Specify the GCP Provider
provider "google" {
  credentials = file("gcp-cc.json")
  project     = var.project_id
  region      = var.region
}

# Create a GCS Bucket
resource "google_storage_bucket" "tf_bucket" {
  name     = var.bucket_name
  location = var.region
}

