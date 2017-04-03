import java.io.*;
import java.util.*;

public class w1 {
  //This directory created by 1-wire kernel modules
  static String w1DirPath = "/sys/bus/w1/devices";

  public static void main(String[] argv) throws Exception {
    File dir = new File(w1DirPath);
    File[] files = dir.listFiles(new DirectoryFileFilter());
    if (files != null) {
      while(true) {
        for(File file: files) {
          System.out.print(file.getName() + ": ");
   // Device data in w1_slave file
          String filePath = w1DirPath + "/" + file.getName() + "/w1_slave";
          File f = new File(filePath);
          try(BufferedReader 
              br = new BufferedReader(new FileReader(f))) {
            String output;
            while((output = br.readLine()) != null) {
              int idx = output.indexOf("t=");
              if(idx > -1) {
                // Temp data (multiplied by 1000) in 5 chars after t=
                float tempC = Float.parseFloat(
                    output.substring(output.indexOf("t=") + 2));
                // Divide by 1000 to get degrees Celsius
  tempC /= 1000;
  System.out.print(String.format("%.3f ", tempC));
  float tempF = tempC * 9 / 5 + 32;
  System.out.println(String.format("%.3f", tempF));
              }
            }
          }
          catch(Exception ex) {
            System.out.println(ex.getMessage());
		}
          }
        }
      }
   } 
}

// This FileFilter selects subdirs with name beginning with 28-
// Kernel module gives each 1-wire temp sensor name starting with 28-
class DirectoryFileFilter implements FileFilter
{
   public boolean accept(File file) {
     String dirName = file.getName();
     String startOfName = dirName.substring(0, 3);
     return (file.isDirectory() && startOfName.equals("28-"));
   }
}  
