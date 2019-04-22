package oshi;

// <editor-fold defaultstate="collapsed" desc="imports...">
import java.util.*;
import oshi.*;
import oshi.data.windows.*;
import oshi.hardware.*;
import oshi.hardware.platform.linux.*;
import oshi.hardware.platform.mac.*;
import oshi.hardware.platform.unix.freebsd.*;
import oshi.hardware.platform.unix.solaris.*;
import oshi.hardware.platform.windows.*;
import oshi.hardware.common.*;
import oshi.jna.platform.linux.*;
import oshi.jna.platform.mac.*;
import oshi.jna.platform.unix.*;
import oshi.jna.platform.unix.freebsd.*;
import oshi.jna.platform.windows.*;
import oshi.software.common.*;
import oshi.software.os.*;
import oshi.software.os.linux.*;
import oshi.software.os.mac.*;
import oshi.software.os.unix.freebsd.*;
import oshi.software.os.unix.solaris.*;
import oshi.software.os.windows.*;
import oshi.util.*;
import oshi.util.platform.linux.*;
import oshi.util.platform.mac.*;
import oshi.util.platform.unix.freebsd.*;
import oshi.util.platform.unix.solaris.*;
import oshi.util.platform.windows.*;
// </editor-fold>

public class SimpleSystemInfo {

    private static SystemInfo systemInfo = new SystemInfo();

    private static String singleString;
    private static String[] arrayString;

    private static long singleLong;
    private static long[] arrayLongValues;
    private static long[] prevTicks;
    private static long[][] prevProcTicks;

    private static double singleDouble;
    private static double[] arrayDoubleValues;

    // <editor-fold defaultstate="collapsed" desc="Basic Methods... ">   
    public static SystemInfo getOshiSystemInfo() {
        return systemInfo;
    }

    public static OperatingSystem getOs() {
        return systemInfo.getOperatingSystem();
    }

    public static HardwareAbstractionLayer getHardware() {
        return systemInfo.getHardware();
    }

    public static CentralProcessor getCpu() {
        return getHardware().getProcessor();
    }

    public static NetworkIF[] getNetwork() {
        return getHardware().getNetworkIFs();
    }

    public static OSProcess getProcess(int processID) {
        return getOs().getProcess(processID);
    }

    public static GlobalMemory getMemory() {
        return getHardware().getMemory();
    }

    public static UsbDevice[] getUsbDevices(boolean showDevicesTree) {
        return getHardware().getUsbDevices(showDevicesTree);
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="OS Methods">   
    public static String getOsName() {
        return getOs().getFamily();
    }

    public static String getOsManufacturer() {
        return getOs().getManufacturer();
    }

    private static String[] getOsVersionDescription() {
        arrayString = getOs().getVersion().toString().split(" ");
        return arrayString;
    }

    public static String getOsVersion() {
        return getOsVersionDescription()[0];
    }
    
    public static String getOsBuildVersion(){
        return getOsVersionDescription()[3];
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="CPU Methods">
    public static String getCpuManufacturer(){
        arrayString = getCpu().getName().split(" ");
        return arrayString[0];
    }
    
    public static String getCpuModel(){
        arrayString = getCpu().getName().split(" ");
        return arrayString[2];
    }
    
    public static String getCpuUsedPercentageAsString() {
        prevTicks = getCpu().getSystemCpuLoadTicks();
        Util.sleep(100);
        getCpu().updateAttributes();
        return String.format(
                "%.2f%%",
                (getCpu().getSystemCpuLoadBetweenTicks(prevTicks) * 100)
        );
    }

    public static String[] getCpuCoreUsedPercentageAsString() {
        prevProcTicks = getCpu().getProcessorCpuLoadTicks();
        Util.sleep(100);
        getCpu().updateAttributes();
        double[] load = getCpu().getProcessorCpuLoadBetweenTicks(prevProcTicks);
        arrayString = new String[load.length];
        for (int count = 0; count < load.length; count++) {
            arrayString[count] = String.format(" %.2f%%", (load[count] * 100));
        }
        return arrayString;
    }

    public static String getCpuCoreUsedPercentageAsString(int indexOfCore) {
        return getCpuCoreUsedPercentageAsString()[indexOfCore];
    }

    public static String getCpuVendorFrequencyAsString() {
        long freq = getCpu().getVendorFreq();
        if (freq > 0) {
            return FormatUtil.formatHertz(freq);
        }
        return "0";
    }

    public static String getCpuCurrentFrequencyAsString() {
        Util.sleep(100);
        getCpu().updateAttributes();
        arrayLongValues = getCpu().getCurrentFreq();

        if (arrayLongValues[0] > 0) {
            singleLong = 0;
            for (int count = 0; count < arrayLongValues.length; count++) {
                singleLong += arrayLongValues[count];
            }
        }

        singleLong /= arrayLongValues.length;

        return FormatUtil.formatHertz(singleLong);
    }

    public static String[] getCpuCoreCurrentFrequencyAsString() {
        Util.sleep(100);
        getCpu().updateAttributes();
        arrayLongValues = getCpu().getCurrentFreq();
        arrayString = null;

        if (arrayLongValues[0] > 0) {
            arrayString = new String[arrayLongValues.length];
            for (int count = 0; count < arrayLongValues.length; count++) {
                arrayString[count] = FormatUtil.formatHertz(arrayLongValues[count]);
            }
        }

        return arrayString;
    }

    //--- DOUBLE 
    public static double getCpuUsedPercentageAsDouble() {
        singleString = getCpuUsedPercentageAsString();
        singleString = singleString.substring(0, (singleString.length() - 1));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }

    public static double[] getCpuCoreUsedPercentageAsDouble() {
        arrayString = getCpuCoreUsedPercentageAsString();
        arrayDoubleValues = new double[arrayString.length];
        for (int count = 0; count < arrayString.length; count++) {
            singleString = arrayString[count];
            singleString = singleString.substring(0, (singleString.length() - 1));
            singleString = singleString.replace(',', '.');
            arrayDoubleValues[count] = Double.parseDouble(singleString);
        }
        return arrayDoubleValues;
    }

    public static double getCpuCoreUsedPercentageAsDouble(int indexOfCore) {
        return getCpuCoreUsedPercentageAsDouble()[indexOfCore];
    }
    
    public static double getCpuVendorFrequencyAsDouble() {
        singleString = getCpuCurrentFrequencyAsString();
        singleString = singleString.substring(0, (singleString.length() - 4));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }
    
    public static double getCpuCurrentFrequencyAsDouble() {
        singleString = getCpuCurrentFrequencyAsString();
        singleString = singleString.substring(0, (singleString.length() - 4));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }

    public static double[] getCpuCoreCurrentFrequencyAsDouble() {
        arrayString = getCpuCoreCurrentFrequencyAsString();
        arrayDoubleValues = new double[arrayString.length];
        for (int count = 0; count < arrayString.length; count++) {
            singleString = arrayString[count];
            singleString = singleString.substring(0, (singleString.length() - 4));
            singleString = singleString.replace(',', '.');
            arrayDoubleValues[count] = Double.parseDouble(singleString);
        }
        return arrayDoubleValues;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="RAM Methods">

    // </editor-fold>

}
