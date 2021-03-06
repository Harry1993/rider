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
	private long prevTime, currTime;
	private int numOfCPU;

	private long waitTime;

	public CPUPerProcess(String logPath, long waitTime) {
		this.si = new SystemInfo();
		this.df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		this.waitTime = waitTime;
		this.prevTime = 0;

		try {
			this.pw = new PrintWriter(new FileOutputStream(
				new File(logPath+"/"+df.format(new Date())+"_cpu_per_process.log"),
				true));
			this.pw.println("DATE-TIME PID %CPU");
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	private void printProcesses(OperatingSystem os, GlobalMemory memory) {
		int pid = os.getProcessId(); // this pid
		OSProcess p = os.getProcess(pid);
		this.currTime = p.getKernelTime() + p.getUserTime();
		long timeDiff = currTime - prevTime;
		prevTime = currTime;
		
		try {
			this.pw.println(String.format("%s %d %.1f",
						df.format(new Date()),
						pid,
						100d * timeDiff / this.numOfCPU));
			this.pw.flush();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
    }

	public void run() {
		HardwareAbstractionLayer hal = this.si.getHardware();
		GlobalMemory gm = hal.getMemory();
		CentralProcessor cp = hal.getProcessor();
		OperatingSystem os = si.getOperatingSystem();
		this.numOfCPU = cp.getLogicalProcessorCount();

		while (true) {
			printProcesses(os, gm);
			Util.sleep(this.waitTime);
		}
	}
}
