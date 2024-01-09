module "ecs_autoscaling" {
  app_min_count = 1
  app_max_count = 3
}

module "ecs_service" {
  app_desired_count = 1
}
