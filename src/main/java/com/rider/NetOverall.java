package rider;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.util.Util;
import oshi.util.FormatUtil;

import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NetOverall extends Thread {

	private SystemInfo si;
	private PrintWriter pw;
	private DateFormat df;

	private long waitTime;

	public NetOverall(String logPath, long waitTime) {
        this.si = new SystemInfo();
		this.df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		this.waitTime = waitTime;

		try {
			this.pw = new PrintWriter(new FileOutputStream(
				new File(logPath+"/"+df.format(new Date())+"_net_overall.log"),
				true));
			this.pw.println("DATE-TIME IF Rx Tx");
		} catch(Exception e) {
			System.out.println(e.toString());
		}

	}

    private void printNetworkInterfaces(NetworkIF[] networkIFs) {
		for (NetworkIF net : networkIFs) {
			boolean hasData = net.getBytesRecv() > 0 || net.getBytesSent() > 0
				|| net.getPacketsRecv() > 0 || net.getPacketsSent() > 0;
			try {
				this.pw.println(String.format("%s %s %s %s",
							df.format(new Date()),
							net.getDisplayName(),
							hasData ? FormatUtil.formatBytes(net.getBytesRecv()) : "?",
							hasData ? FormatUtil.formatBytes(net.getBytesSent()) : "?"));
				this.pw.flush();
			} catch(Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public void run() {
		HardwareAbstractionLayer hal = this.si.getHardware();

		while (true) {
			printNetworkInterfaces(hal.getNetworkIFs());
			Util.sleep(this.waitTime);
		}
	}

}
