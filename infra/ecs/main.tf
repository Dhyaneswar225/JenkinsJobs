terraform {
  backend "s3" {}
}

provider "aws" {
  region = var.aws_region
}

locals {
  business_unit = "Consumer"
  managed_by    = "MyTeam"
}

module "ecs_alb" {
  source           = "git::ssh://git@github.com/sflyinc-shutterfly/tf_ecs_service_deploy//alb"
  app              = var.app
  aws_account      = var.aws_account
  aws_vpc          = var.aws_vpc
  env              = var.env
  ecs_cluster      = var.ecs_cluster
  business_unit    = local.business_unit
  managed_by       = local.managed_by
  app_health_check = "/_internal/liveness"

  app_health_check_timeout = "3"
}

module "ecs_service" {
  source                                = "git::ssh://git@github.com/sflyinc-shutterfly/tf_ecs_service_deploy//service"
  app                                   = var.app
  env                                   = var.env
  aws_account                           = var.aws_account
  ecs_cluster                           = var.ecs_cluster
  ecs_task_definition_version           = var.ecs_task_definition_version
  target_group_arn                      = module.ecs_alb.aws_alb_target_group_arn
  business_unit                         = local.business_unit
  managed_by                            = local.managed_by
}

module "ecs_autoscaling" {
  source      = "git::ssh://git@github.com/sflyinc-shutterfly/tf_ecs_service_deploy//autoscaling"
  app         = var.app
  env         = var.env
  aws_account = var.aws_account
  ecs_cluster = module.ecs_service.aws_ecs_cluster
  service_arn = module.ecs_service.aws_ecs_service_arn
}
