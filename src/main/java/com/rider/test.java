package rider;

import oshi.util.Util;

public class test {

	public static void main(String[] args) {
		(new MemPerProcess(args[0], 1000)).start();
	}

}
