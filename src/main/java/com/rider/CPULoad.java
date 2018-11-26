package rider;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

public class CPULoad {

	private SystemInfo si;
	private HardwareAbstractionLayer hal;

	CPULoad() {
        this.si = new SystemInfo();
	}

    public void printCpu() {
		HardwareAbstractionLayer hal = this.si.getHardware();
		CentralProcessor processor = hal.getProcessor();

        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks();
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        System.out.println(procCpu.toString());
	}

}
