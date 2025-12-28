package com.fsts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class App {


    private static List<Vm> vmList;
    private static List<Cloudlet> cloudletList;

    public static void main(String[] args) {

        try {
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            CloudSim.init(numUsers, calendar, traceFlag);

            Datacenter datacenter = createDatacenter("Datacenter_1");

            DatacenterBroker broker = new DatacenterBroker("Broker_1");
            int brokerId = broker.getId();

            vmList = createVMs(brokerId);
            cloudletList = createCloudlets(brokerId);

            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            CloudSim.startSimulation();

            List<Cloudlet> resultList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printResults(resultList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DATACENTER =================
    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        int ram = 16384; // 16GB
        long storage = 1_000_000; // 1TB
        int bw = 10000;

        for (int i = 0; i < 2; i++) {
            List<Pe> peList = new ArrayList<>();
            peList.add(new Pe(0, new PeProvisionerSimple(2000)));
            peList.add(new Pe(1, new PeProvisionerSimple(2000)));

            hostList.add(
                    new Host(
                            i,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList,
                            new VmSchedulerTimeShared(peList)
                    )
            );
        }

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double timeZone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        arch, os, vmm, hostList, timeZone,
                        cost, costPerMem, costPerStorage, costPerBw
                );

        try {
            return new Datacenter(
                    name,
                    characteristics,
                    new VmAllocationPolicySimple(hostList),
                    new LinkedList<>(),
                    0
            );
        } catch (Exception e) {
            return null;
        }
    }

    // ================= VMs =================
    private static List<Vm> createVMs(int brokerId) {

        List<Vm> list = new ArrayList<>();

        int mips = 1000;
        long size = 10000;
        int ram = 2048;
        long bw = 1000;
        int pes = 1;
        String vmm = "Xen";

        for (int i = 0; i < 4; i++) {
            list.add(
                    new Vm(
                            i, brokerId, mips, pes,
                            ram, bw, size,
                            vmm, new CloudletSchedulerTimeShared()
                    )
            );
        }

        return list;
    }

    // ================= CLOUDLETS =================
    private static List<Cloudlet> createCloudlets(int brokerId) {

        List<Cloudlet> list = new ArrayList<>();

        long length = 40000;
        long fileSize = 300;
        long outputSize = 300;
        int pes = 1;
        UtilizationModel model = new UtilizationModelFull();

        for (int i = 0; i < 8; i++) {
            Cloudlet cloudlet = new Cloudlet(
                    i, length, pes,
                    fileSize, outputSize,
                    model, model, model
            );
            cloudlet.setUserId(brokerId);
            list.add(cloudlet);
        }

        return list;
    }

    // ================= RESULTS =================
    private static void printResults(List<Cloudlet> list) {

        DecimalFormat df = new DecimalFormat("###.##");

        System.out.println("\n========== CLOUDLET RESULTS ==========");
        System.out.println("ID\tSTATUS\tDATACENTER\tVM\tTIME\tSTART\tFINISH");

        for (Cloudlet cloudlet : list) {
            if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
                System.out.println(
                        cloudlet.getCloudletId() + "\tSUCCESS\t" +
                                cloudlet.getResourceId() + "\t\t" +
                                cloudlet.getVmId() + "\t" +
                                df.format(cloudlet.getActualCPUTime()) + "\t" +
                                df.format(cloudlet.getExecStartTime()) + "\t" +
                                df.format(cloudlet.getFinishTime())
                );
            }
        }
    }


}
