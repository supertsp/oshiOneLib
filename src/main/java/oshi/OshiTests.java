package oshi;

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
        
        
        for (int i = 0; i < 10_000; i++) {
            System.out.println(
//                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[0] + " " +
//                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[1] + " " +
//                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[2] + " " +
//                    SimpleSystemInfo.getCpuCoreCurrentFrequencyAsDouble()[3] + " "
                    SimpleSystemInfo.getOsBuildVersion() + " "
            );
            
            Util.sleep(100);
        }
        
        
        
        System.out.println("\n\n\n");
    }
    
}
