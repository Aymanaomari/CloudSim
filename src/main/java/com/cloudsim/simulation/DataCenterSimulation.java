package com.cloudsim.simulation;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * CloudSim Simulation Example
 * This simulation demonstrates 8 cloudlets running on 4 VMs in a single datacenter.
 * 
 * Simulation Configuration:
 * - 1 Datacenter with 4 hosts
 * - 4 Virtual Machines (VMs)
 * - 8 Cloudlets (tasks) distributed across the VMs
 * 
 * @author CloudSim Simulation
 * @version 1.0
 */
public class DataCenterSimulation {
    
    /** The cloudlet list. */
    private static List<Cloudlet> cloudletList;
    
    /** The vmlist. */
    private static List<Vm> vmlist;
    
    /**
     * Creates main() to run this simulation.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Log.printLine("Starting CloudSim Simulation...");
        
        try {
            // Step 1: Initialize the CloudSim package
            int num_user = 1;   // number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // trace events
            
            // Initialize CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);
            
            // Step 2: Create Datacenter
            Datacenter datacenter0 = createDatacenter("Datacenter_0");
            
            // Step 3: Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();
            
            // Step 4: Create 4 VMs
            vmlist = new ArrayList<Vm>();
            
            // VM configuration parameters
            int vmid = 0;
            int mips = 1000;
            long size = 10000; // image size (MB)
            int ram = 512; // vm memory (MB)
            long bw = 1000;
            int pesNumber = 1; // number of cpus
            String vmm = "Xen"; // VMM name
            
            // Create 4 VMs
            for (int i = 0; i < 4; i++) {
                vmlist.add(new Vm(vmid++, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared()));
            }
            
            // Submit vm list to the broker
            broker.submitVmList(vmlist);
            
            // Step 5: Create 8 Cloudlets
            cloudletList = new ArrayList<Cloudlet>();
            
            // Cloudlet configuration parameters
            int id = 0;
            long length = 40000;
            long fileSize = 300;
            long outputSize = 300;
            UtilizationModel utilizationModel = new UtilizationModelFull();
            
            // Create 8 Cloudlets
            for (int i = 0; i < 8; i++) {
                Cloudlet cloudlet = new Cloudlet(id++, length, pesNumber, fileSize, outputSize, 
                                                  utilizationModel, utilizationModel, utilizationModel);
                cloudlet.setUserId(brokerId);
                cloudletList.add(cloudlet);
            }
            
            // Submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);
            
            // Step 6: Start the simulation
            CloudSim.startSimulation();
            
            // Step 7: Stop simulation and print results
            CloudSim.stopSimulation();
            
            // Final step: Print results when simulation is over
            List<Cloudlet> finishedCloudlets = broker.getCloudletReceivedList();
            printCloudletList(finishedCloudlets);
            
            Log.printLine("\nSimulation finished!");
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happened");
        }
    }
    
    /**
     * Creates the datacenter.
     * 
     * @param name the name of the datacenter
     * @return the datacenter
     */
    private static Datacenter createDatacenter(String name) {
        
        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store our machine
        List<Host> hostList = new ArrayList<Host>();
        
        // 2. A Machine contains one or more PEs or CPUs/Cores.
        List<Pe> peList = new ArrayList<Pe>();
        
        int mips = 1000;
        
        // 3. Create PEs and add these into a list.
        peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        
        // 4. Create Host with its id and list of PEs and add them to the list of machines
        int hostId = 0;
        int ram = 2048; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;
        
        // Create 4 Hosts
        for (int i = 0; i < 4; i++) {
            hostList.add(
                new Host(
                    hostId++,
                    new RamProvisionerSimple(ram),
                    new BwProvisionerSimple(bw),
                    storage,
                    new ArrayList<Pe>(peList),
                    new VmSchedulerTimeShared(new ArrayList<Pe>(peList))
                )
            );
        }
        
        // 5. Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.001;	// the cost of using storage in this resource
        double costPerBw = 0.0;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	// we are not adding SAN devices
        
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
        
        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return datacenter;
    }
    
    /**
     * Creates the broker.
     * 
     * @return the datacenter broker
     */
    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }
    
    /**
     * Prints the Cloudlet objects with detailed information.
     * 
     * @param list  list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;
        
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
        
        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
            
            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
                        indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent +
                        dft.format(cloudlet.getExecStartTime()) + indent + indent +
                        dft.format(cloudlet.getFinishTime()));
            }
        }
    }
}
