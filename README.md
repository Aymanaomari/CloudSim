# CloudSim v3.0 DataCenter Simulation

This repository contains a CloudSim v3.0 simulation that demonstrates **8 cloudlets running on 4 virtual machines (VMs)** in a single datacenter environment.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [CloudSim v3.0 Installation](#cloudsim-v30-installation)
- [Project Setup](#project-setup)
- [Running the Simulation](#running-the-simulation)
- [Simulation Configuration](#simulation-configuration)
- [Expected Output](#expected-output)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## Overview

This project demonstrates a basic CloudSim simulation with the following configuration:

- **1 Datacenter** with 4 hosts
- **4 Virtual Machines (VMs)** - Each VM has:
  - 1000 MIPS
  - 512 MB RAM
  - 10 GB storage
  - 1000 Mbps bandwidth
  - 1 CPU core
- **8 Cloudlets (tasks)** - Each cloudlet has:
  - 40,000 MI (Million Instructions) length
  - 300 MB file size
  - 300 MB output size

The simulation uses CloudSim v3.0.3 from the CloudSim project (https://github.com/cloudslab/cloudsim).

## Prerequisites

Before running this simulation, ensure you have:

- **Java Development Kit (JDK) 8 or higher**
- **Apache Maven 3.x**
- **CloudSim v3.0.3 JAR file** (downloaded from [CloudSim releases](https://github.com/cloudslab/cloudsim/releases))

## CloudSim v3.0 Installation

CloudSim v3.0 is not available in Maven Central Repository, so you need to install it manually as a local Maven dependency.

### Step 1: Download CloudSim v3.0

Download CloudSim v3.0.3 from the official releases:
```bash
wget https://github.com/cloudslab/cloudsim/releases/download/cloudsim-3.0.3/cloudsim-3.0.3-sources.jar
```

Or manually download from: https://github.com/cloudslab/cloudsim/releases/tag/cloudsim-3.0.3

### Step 2: Install CloudSim to Local Maven Repository

Once you have the CloudSim JAR file, install it to your local Maven repository using the following command:

```bash
mvn install:install-file \
    -Dfile=/path/to/cloudsim-3.0.3.jar \
    -DgroupId=org.cloudbus \
    -DartifactId=cloudsim \
    -Dversion=3.0.3 \
    -Dpackaging=jar
```

**Replace `/path/to/cloudsim-3.0.3.jar` with the actual path to your downloaded CloudSim JAR file.**

Example for Linux/Mac:
```bash
mvn install:install-file \
    -Dfile=~/Downloads/cloudsim-3.0.3.jar \
    -DgroupId=org.cloudbus \
    -DartifactId=cloudsim \
    -Dversion=3.0.3 \
    -Dpackaging=jar
```

Example for Windows:
```bash
mvn install:install-file ^
    -Dfile=C:\Users\YourName\Downloads\cloudsim-3.0.3.jar ^
    -DgroupId=org.cloudbus ^
    -DartifactId=cloudsim ^
    -Dversion=3.0.3 ^
    -Dpackaging=jar
```

### Step 3: Verify Installation

Verify that CloudSim has been installed in your local Maven repository:
```bash
ls ~/.m2/repository/org/cloudbus/cloudsim/3.0.3/
```

You should see the `cloudsim-3.0.3.jar` file and associated metadata files.

## Project Setup

1. **Clone this repository:**
   ```bash
   git clone https://github.com/Aymanaomari/CloudSim.git
   cd CloudSim
   ```

2. **Ensure CloudSim v3.0.3 is installed** (see [CloudSim v3.0 Installation](#cloudsim-v30-installation) above)

3. **Build the project:**
   ```bash
   mvn clean compile
   ```

## Running the Simulation

### Option 1: Using Maven

Run the simulation directly using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.cloudsim.simulation.DataCenterSimulation"
```

Or simply:
```bash
mvn exec:java
```

### Option 2: Using Maven Package

Build the JAR and run it:
```bash
mvn clean package
java -cp target/cloudsim-simulation-1.0.0.jar com.cloudsim.simulation.DataCenterSimulation
```

### Option 3: From IDE

Import the project into your favorite IDE (Eclipse, IntelliJ IDEA, NetBeans) as a Maven project and run the `DataCenterSimulation.java` main class.

## Simulation Configuration

The simulation is configured as follows:

### Datacenter Configuration
- **Number of Hosts:** 4
- **Host Specifications:**
  - RAM: 2048 MB
  - Storage: 1,000,000 MB (1 TB)
  - Bandwidth: 10,000 Mbps
  - Processing Elements (PEs): 1 per host
  - MIPS per PE: 1000

### Virtual Machine Configuration
- **Number of VMs:** 4
- **VM Specifications:**
  - MIPS: 1000
  - RAM: 512 MB
  - Storage: 10,000 MB (10 GB)
  - Bandwidth: 1000 Mbps
  - Number of CPUs: 1
  - VMM: Xen
  - Scheduler: Time-Shared

### Cloudlet Configuration
- **Number of Cloudlets:** 8
- **Cloudlet Specifications:**
  - Length: 40,000 MI (Million Instructions)
  - File Size: 300 MB
  - Output Size: 300 MB
  - Number of PEs required: 1
  - Utilization Model: Full

## Expected Output

When you run the simulation, you should see output similar to:

```
Starting CloudSim Simulation...
Starting CloudSim version 3.0
Datacenter_0 is starting...
Broker is starting...
Entities started.
0.0: Broker: Cloud Resource List received with 1 resource(s)
0.0: Broker: Trying to Create VM #0 in Datacenter_0
0.0: Broker: Trying to Create VM #1 in Datacenter_0
0.0: Broker: Trying to Create VM #2 in Datacenter_0
0.0: Broker: Trying to Create VM #3 in Datacenter_0
...

========== OUTPUT ==========
Cloudlet ID    STATUS    Data center ID    VM ID    Time    Start Time    Finish Time
    0          SUCCESS        2               0      40.0      0.1          40.1
    1          SUCCESS        2               1      40.0      0.1          40.1
    2          SUCCESS        2               2      40.0      0.1          40.1
    3          SUCCESS        2               3      40.0      0.1          40.1
    4          SUCCESS        2               0      40.0     40.1          80.1
    5          SUCCESS        2               1      40.0     40.1          80.1
    6          SUCCESS        2               2      40.0     40.1          80.1
    7          SUCCESS        2               3      40.0     40.1          80.1

Simulation finished!
```

The output shows that all 8 cloudlets have been successfully executed across the 4 VMs, with details about execution time, start time, and finish time.

## Project Structure

```
CloudSim/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── cloudsim/
│                   └── simulation/
│                       └── DataCenterSimulation.java    # Main simulation class
├── pom.xml                                               # Maven configuration
├── .gitignore                                            # Git ignore rules
└── README.md                                             # This file
```

## Troubleshooting

### Issue: "Package org.cloudbus.cloudsim does not exist"

**Solution:** Make sure you have installed CloudSim v3.0.3 to your local Maven repository using the `mvn install:install-file` command described in the [CloudSim v3.0 Installation](#cloudsim-v30-installation) section.

### Issue: Maven cannot find CloudSim dependency

**Solution:** 
1. Check that the JAR was installed correctly:
   ```bash
   ls ~/.m2/repository/org/cloudbus/cloudsim/3.0.3/
   ```
2. Verify the groupId, artifactId, and version in `pom.xml` match what you used in the install command.

### Issue: Java version compatibility

**Solution:** CloudSim v3.0 requires Java 8 or higher. Check your Java version:
```bash
java -version
```

If needed, update your Java installation or configure Maven to use the correct Java version in `pom.xml`.

### Issue: OutOfMemoryError

**Solution:** Increase the heap size when running the simulation:
```bash
mvn exec:java -Dexec.mainClass="com.cloudsim.simulation.DataCenterSimulation" -Dexec.args="-Xmx512m"
```

## Additional Resources

- [CloudSim Official Website](http://www.cloudbus.org/cloudsim/)
- [CloudSim GitHub Repository](https://github.com/cloudslab/cloudsim)
- [CloudSim Documentation](https://github.com/cloudslab/cloudsim/tree/master/docs)
- [CloudSim Examples](https://github.com/cloudslab/cloudsim/tree/master/modules/cloudsim-examples)

## License

This project is provided as an educational example for CloudSim simulation. Please refer to the [CloudSim license](https://github.com/cloudslab/cloudsim/blob/master/LICENSE) for details about CloudSim library usage.

## Author

Created for CloudSim simulation demonstration purposes.

## Contributing

Feel free to fork this repository and submit pull requests for improvements or additional simulation examples.
