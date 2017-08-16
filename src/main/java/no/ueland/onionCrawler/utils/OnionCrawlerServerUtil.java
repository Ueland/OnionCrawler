package no.ueland.onionCrawler.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

public class OnionCrawlerServerUtil {
	static Logger logger = Logger.getLogger(OnionCrawlerServerUtil.class);

	public static List<String> getServerIP() {
		ArrayList<String> IPs = new ArrayList<>();
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					String IP = i.getHostAddress();
					if (IP.startsWith("127.0.0") || IP.startsWith("0:0:0:0:0:0:0")) {
						continue;
					}
					IPs.add(IP);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return IPs;
	}
}
