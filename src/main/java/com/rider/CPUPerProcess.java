package rider;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.software.os.OSProcess;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.GlobalMemory;
import oshi.util.Util;
import oshi.util.FormatUtil;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CPUPerProcess extends Thread {

	private SystemInfo si;
	private PrintWriter pw;
	private DateFormat df;

	public CPUPerProcess(String logPath) {
        this.si = new SystemInfo();
		try {
			this.pw = new PrintWriter(new FileOutputStream(
						new File(logPath), true));
		} catch(Exception e) {
			System.out.println("opening log file failed");
		}

		this.df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	private static void printProcesses(OperatingSystem os, GlobalMemory memory) {
        System.out.println("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
        // Sort by highest CPU
        List<OSProcess> procs = Arrays.asList(os.getProcesses(5, ProcessSort.CPU));

        System.out.println("   PID  %CPU %MEM       VSZ       RSS Name");
        for (int i = 0; i < procs.size() && i < 5; i++) {
            OSProcess p = procs.get(i);
            System.out.format(" %5d %5.1f %4.1f %9s %9s %s%n", p.getProcessID(),
                    100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                    100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                    FormatUtil.formatBytes(p.getResidentSetSize()), p.getName());
        }
    }

	public void run() {
		HardwareAbstractionLayer hal = this.si.getHardware();
		GlobalMemory gm = hal.getMemory();
		CentralProcessor cp = hal.getProcessor();
		OperatingSystem os = si.getOperatingSystem();

		while (true) {
			printProcesses(os, gm);
			Util.sleep(1000);
		}
	}
}
