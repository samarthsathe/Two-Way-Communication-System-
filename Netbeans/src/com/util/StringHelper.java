
package com.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Scanner;


/**
 * 
 * @author user
 */
public class StringHelper {
	public static String n2s(Object d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
		return dual;
	}
	public static String nullObjectToStringEmpty(Object d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
		return dual;
	}
	public static float nullObjectToFloatEmpty(Object d){
		float i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Float(dual).floatValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}	
	public static int nullObjectToIntegerEmpty(Object d){
		int i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Integer(dual).intValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static String result[][] = new String[500][];
	public static int count = -1;

	public static boolean checkConnectivityServer(String ip, int port) {
		boolean success = false;
		try {
			Socket soc = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			soc.connect(socketAddress, 3000);
			System.out.println(socketAddress.toString());
			success = true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(" Connecting to server " + success);
		return success;

	}
	public static void main(String args[]) {
	connect2Server("http://192.168.0.102:9988/?method%3Dpath%26value%3DD%3A%2FBmonitor+to+dasda+sir%2F");
//		checkConnectivityServer("ADMIN-0D61A3371", 9988);
		
//		String s=null;
//		s=StringHelper.n2s(s);
//		s=StringHelper.n2s(s);
//		s=StringHelper.n2s(s);
//		s=StringHelper.n2s(s);
//		s=StringHelper.n2s(s);
//		s=StringHelper.n2s(s);
//		
//		System.out.println(s.length());
//
//		URL u;
//		try {
//			u = new URL("http://192.168.0.101:9988/?method%3Dpath%26value%3DD%3A%2FBmonitor+to+asd+sir%2F");
//			Scanner scanner=new Scanner(u.openStream());
//			while(scanner.hasNext()){
//				String row=scanner.nextLine();
//				
//				String cols[] = row.split(",");	
//				for(int i=0;i<cols.length&&cols[i]!=null;i++){	cols[i]=cols[i].trim(); System.out.println(cols[i]);}
//				result[++count] = cols;
//				
//			}
//			scanner.close();
//			u=null;
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
		
		
	}
	
public static void connect2Server(String url) {
		System.out.println(new Date());
	
		URL u;
		try {
	
			for (int i = 0; i < result.length; i++) {
				result[i] = null;
			}
			u = new URL(url);
			URLConnection uc= u.openConnection();
			uc.setConnectTimeout(5000);
			
			Scanner scanner=new Scanner(uc.getInputStream());
			
			while (scanner.hasNext()) {
				String row = StringHelper.n2s(scanner.nextLine());
				if (row.length() > 0) {
					
					String cols[] = row.split(",");
					for (int i = 0; i < cols.length && cols[i] != null; i++) {
						cols[i] = cols[i].trim();
					}
					
				}
			}
			scanner.close();
			u=null;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(new Date());
		
	}
}
