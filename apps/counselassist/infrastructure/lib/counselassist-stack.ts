import * as cdk from 'aws-cdk-lib';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecs_patterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as ecr_assets from 'aws-cdk-lib/aws-ecr-assets';
import { Construct } from 'constructs';

export class CounselAssistStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const vpc = new ec2.Vpc(this, 'CounselAssistVpc', {
      maxAzs: 2,
      natGateways: 1,
    });

    // Create ECS Cluster with EC2 capacity
    const cluster = new ecs.Cluster(this, 'CounselAssistCluster', {
      vpc,
    });

    // Add EC2 Capacity using t2.micro
    const asg = cluster.addCapacity('DefaultAutoScalingGroup', {
      instanceType: ec2.InstanceType.of(ec2.InstanceClass.T2, ec2.InstanceSize.MICRO),
      minCapacity: 1,
      maxCapacity: 2,
      desiredCapacity: 1,
    });

    // Set instance to stop when idle (after 30 minutes of no activity)
    asg.scaleOnCpuUtilization('KeepCpuHot', {
      targetUtilizationPercent: 30,
      scaleInCooldown: cdk.Duration.minutes(30),
      scaleOutCooldown: cdk.Duration.minutes(2),
    });

    // Build Docker image
    const image = new ecr_assets.DockerImageAsset(this, 'CounselAssistImage', {
      directory: '../', // Path to your Dockerfile
    });

    // Create Fargate Service
    const fargateService = new ecs_patterns.ApplicationLoadBalancedFargateService(this, 'CounselAssistService', {
      cluster,
      memoryLimitMiB: 2048,
      cpu: 1024,
      desiredCount: 1,
      taskImageOptions: {
        image: ecs.ContainerImage.fromDockerImageAsset(image),
        containerPort: 8000,
        environment: {
          PYTHONUNBUFFERED: '1',
        },
      },
      assignPublicIp: true,
    });

    // Auto-scaling
    const scaling = fargateService.service.autoScaleTaskCount({
      maxCapacity: 4,
      minCapacity: 1,
    });

    scaling.scaleOnCpuUtilization('CpuScaling', {
      targetUtilizationPercent: 70,
      scaleInCooldown: cdk.Duration.seconds(60),
      scaleOutCooldown: cdk.Duration.seconds(60),
    });

    // Output the Load Balancer DNS
    new cdk.CfnOutput(this, 'LoadBalancerDNS', {
      value: fargateService.loadBalancer.loadBalancerDnsName,
    });
  }
}