package com.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


public class MachineName2IP {
	static boolean progress = false;
	static long endtime = 0;
	static long starttime = 0;
	static String ip = "";

	public static void main(String[] args) {
		System.out.println(new Date());
		String ip1 = getIpAddress("IBS-PC",3000);
//		try {
////			ip = InetAddress.getAllByName("SACHINLAPTOP")[0].getHostAddress();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(new Date());
		System.out.println(" ip1 " + ip);
	}

	public static String getIpAddress(final String hostname,final int timeout) {

		final Thread checkIp=new Thread(){
			@Override
			public void run() {
				super.run();
				progress = false;
//				System.out.println("In checkIp Thread");
				try {
					ip = InetAddress.getAllByName(hostname)[0].getHostAddress();	// 5 seco
					progress = true;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(new Date());
				}
			}
		};
		progress = false;
		starttime = System.currentTimeMillis();
		checkIp.start();
		
		Thread timerThread = new Thread() {
			public void run() {
				while (!progress) {	// check ip is running 

					endtime = System.currentTimeMillis();
					if (endtime - starttime > timeout) {
						ip = "";
						checkIp.stop();
//						System.out.println("Cancelling task");
						break;
					}
				}
//				System.out.println("Exiting thread");
			};
		};
		timerThread.start();
		try {
			timerThread.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println("In Main End");
		return ip;
	}
}
