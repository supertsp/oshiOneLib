

import oshi.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;
import oshi.util.Util;

public class OshiTests {

    public static void main(String[] args) {
        System.out.println("\n\n\n");

//        String[] response = SimpleSystemInfo.getOSVersionDescription();
//        System.out.print(" | ");
//        for (String item : response) {
//            System.out.print(item + " | ");
//        }
//        System.out.println();
//        for (int i = 0; i < 10_000; i++) {
        for (int i = 0; i < 10; i++) {
//            System.out.println(
//                    //                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[0] + " " +
//                    //                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[1] + " " +
//                    //                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[2] + " " +
//                    //                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[3] + " "
////                    SimpleSystemInfo.getDisksCapacityAsDouble()[1] + " "
//            //                    SimpleSystemInfo.getVirtualMemoryUsedAsString() + " "
//            );
//
//            for (String[] item : SimpleSystemInfo.getProcessesAsStringTable()) {
//                System.out.println(item[0].substring(0, item[0].length() > 6 ? 6 : item[0].length()) + "\t\t" + item[1] + "\t\t" + item[2] + "\t\t" + item[3] + "\t\t" + item[4]);
//            }
//            
//            for (String[] item : SimpleSystemInfo.getProcessesWithHeaderAsStringTable()) {
//                System.out.println(item[0].substring(0, item[0].length() > 6 ? 6 : item[0].length()) + "\t\t" + item[1] + "\t\t" + item[2] + "\t\t" + item[3] + "\t\t" + item[4]);
//            }

            System.out.println(SimpleSystemInfo.getNetworkInterfaceIp4(3));


//            
//            
//            
//            
            Util.sleep(100);
        }
        
//        System.out.println("##"+SimpleSystemInfo.getMemoryManufacturer()+"##");
        
        System.out.println("\n\n\n");
    }
    
    public static String getRamManufacturer(){
        String text = "";
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            text += String.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
        return text;
    }
    
    
    public static String getHardDriveSerail() {
        String data = "";
        String temp = "";

        String[][] commands = new String[][]{
//        {"CMD", "/C", "WMIC OS GET Installdate,SerialNumber"},
//        {"CMD", "/C", "WMIC BASEBOARD GET SerialNumber"},
//        {"CMD", "/C", "WMIC CPU GET ProcessorId"},
//        {"CMD", "/C", "WMIC COMPUTERSYSTEM GET Name"},
//        {"CMD", "/C", "WMIC diskdrive GET Name, Manufacturer, Model, InterfaceType, MediaLoaded, MediaType"},
        {"CMD", "/C", "WMIC memorychip GET Manufacturer"}
        };
        try {
            for (int i = 0; i < commands.length; i++) {
                String[] com = commands[i];
                Process process = Runtime.getRuntime().exec(com);
                //Closing output stream of the process
                process.getOutputStream().close();

                //Reading sucessful output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;
                
                while ((s = reader.readLine()) != null) {
                    temp = s.trim();
                    
                    if (!temp.equalsIgnoreCase("Manufacturer") && !data.equalsIgnoreCase(temp)) {
                        data += temp;
                    }
                }
                //data = reader.lines().collect(Collectors.joining());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Print Data
        return data.trim().replaceAll(" +", "");
    }

}
