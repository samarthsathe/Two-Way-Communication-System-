package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class ExecuteDOSCommand {

	public static final String SERVER_IP = "192.168.0.108";

	public static void main(String[] args) {

		increaseBrightness(10);

	}

    public static void increaseBrightness(int value){
        ExecuteDOSCommand.getCommandOutput("D:\\work\\utility\\nircmd\\nircmd.exe setbrightness "+value,1)	;
    }
	public static StringBuffer getCommandOutput(String cmd, int skipLine) {
		StringBuffer sb = new StringBuffer();
		ArrayList arr = new ArrayList();

		try {
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			// read the output from the command

			String s = null;
			//System.out.println("Here is the standard output of the
			 //command:\n");

			for (int i = 0; i < skipLine; i++) {
				s = stdInput.readLine();
				System.out.println("skipline o/p:" + s);
			}
			while ((s = stdInput.readLine()) != null) {
				if (s.trim().length() > 0) {
					s = s.replace("\\", "");
					sb.append(s);
					System.out.println("Command Output " + s);
					sb.append('\n');
				}
			}
			// read any errors from the attempted command
			System.out.println("Execute cmd Done");
			// System.out.println("Here is the standard error of the command (if
			// any):\n");

			while ((s = stdError.readLine()) != null) {
				if (s.trim().length() > 0) {
					System.out.println("ERROR Output " + s);
				}
			}
			
		}

		catch (NullPointerException e) {
			System.out.println("I am in null catch");
			e.printStackTrace();
		}

		catch (Exception e) {
			System.out.println("I am in catch");
			e.printStackTrace();
		}

		return sb;
	}
}
