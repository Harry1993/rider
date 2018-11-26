package rider;

import oshi.util.Util;

public class test {
	public static void main(String[] args) {
		CPULoad cl = new CPULoad();

		while(true) {
			cl.printCpu();
			Util.sleep(1000);
		}
	}
}
