package rider;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CPUOverall extends Thread {

	private SystemInfo si;
	private PrintWriter pw;
	private DateFormat df;

	private long waitTime;

	public CPUOverall(String logPath, long waitTime) {
        this.si = new SystemInfo();
		this.df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		this.waitTime = waitTime;

		try {
			this.pw = new PrintWriter(new FileOutputStream(
				new File(logPath+"/"+df.format(new Date())+"_cpu_overall.log"),
				true));
			this.pw.println("DATE-TIME %CORE1 %CORE2 ...");
		} catch(Exception e) {
			System.out.println(e.toString());
		}

	}

    private void printCPU(CentralProcessor processor) {
		Util.sleep(waitTime); // if not wait, reported cpu usage is wrong.

        StringBuilder procCpu = new StringBuilder("");
        double[] load = processor.getProcessorCpuLoadBetweenTicks();
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }

		try {
        	this.pw.println(df.format(new Date())+procCpu.toString());
			this.pw.flush();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	public void run() {
		HardwareAbstractionLayer hal = this.si.getHardware();
		CentralProcessor cp = hal.getProcessor();

		while (true) {
			printCPU(cp);
		}
	}

}
