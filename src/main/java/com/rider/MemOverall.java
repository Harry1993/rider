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

public class MemOverall extends Thread {

	private SystemInfo si;
	private PrintWriter pw;
	private DateFormat df;

	private long waitTime;

	public MemOverall(String logPath, long waitTime) {
        this.si = new SystemInfo();
		this.df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		this.waitTime = waitTime;

		try {
			this.pw = new PrintWriter(new FileOutputStream(
				new File(logPath+"/"+df.format(new Date())+"_mem_overall.log"),
				true));
			this.pw.println("DATE-TIME AVAILABLE TOTAL");
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	private void printProcesses(GlobalMemory memory) {
		try {
			this.pw.println(String.format("%s %s %s",
						df.format(new Date()),
						FormatUtil.formatBytes(memory.getAvailable()),
						FormatUtil.formatBytes(memory.getTotal())));
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

		while (true) {
			printProcesses(gm);
			Util.sleep(this.waitTime);
		}
	}
}
