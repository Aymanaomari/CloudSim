# CloudSim Simulation Project

![Java](https://img.shields.io/badge/Java-8+-blue) ![Maven](https://img.shields.io/badge/Maven-3+-green)

## Overview
This project simulates cloud computing environments using **CloudSim 3**. It models **data centers, virtual machines (VMs), and tasks (cloudlets)** to study resource allocation, scheduling, and performance in a cloud environment.  

The simulation helps understand how tasks are executed, how resources are utilized, and how different policies affect overall cloud system performance.

## Features
- Creation of multiple data centers and hosts.  
- VM provisioning and allocation.  
- Cloudlet creation and scheduling.  
- Analysis of simulation results including execution times and resource usage.  

## Prerequisites
- **Java 8+**  
- **Maven 3+**  
- IDE (IntelliJ, Eclipse, VS Code)  

## Setup Instructions

### 1. Download CloudSim
The project uses **CloudSim 3**. Download the release from:  
[CloudSim GitHub Releases](https://github.com/cloudslab/cloudsim/releases)

### 2. Install CloudSim JAR in Local Maven Repository
Run the following command to install the CloudSim JAR locally:  

```bash
mvn install:install-file \
    -Dfile=/path/to/cloudsim-3.x.x.jar \
    -DgroupId=org.cloudbus \
    -DartifactId=cloudsim \
    -Dversion=3.x.x \
    -Dpackaging=jar
```

### 3. Add Dependency in `pom.xml`
```xml
<dependency>
    <groupId>org.cloudbus</groupId>
    <artifactId>cloudsim</artifactId>
    <version>3.x.x</version>
</dependency>
```

## Project Structure
```bash
src/
 ├─ main/java/           # Simulation code (data centers, VMs, cloudlets)
 ├─ main/resources/      # Configuration files (if any)
 └─ test/java/           # Unit tests
pom.xml                  # Maven project configuration
```
