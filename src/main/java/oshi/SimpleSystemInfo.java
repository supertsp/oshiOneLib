package oshi;

// <editor-fold defaultstate="collapsed" desc="imports...">
import com.sun.jna.platform.win32.WinBase.SYSTEM_INFO;
import com.sun.jna.platform.win32.WinNT.OSVERSIONINFO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private static String[] arrayString2;
    private static String[][] arrayStringTable;

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

    public static OSProcess[] getProcesses() {
        return getOs().getProcesses(0, OperatingSystem.ProcessSort.MEMORY);
    }

    public static OSProcess[] getProcesses(int numberOfProcesses) {
        return getOs().getProcesses(numberOfProcesses, OperatingSystem.ProcessSort.MEMORY);
    }

    public static GlobalMemory getMemory() {
        return getHardware().getMemory();
    }

    public static HWDiskStore[] getDisks() {
        return getHardware().getDiskStores();
    }

    public static UsbDevice[] getUsbDevices(boolean showDevicesTree) {
        return getHardware().getUsbDevices(showDevicesTree);
    }

    public static String getCurrentUserName() {
        return System.getenv("USERNAME");
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="OS Methods">   
    public static String getOsName() {
        return getOs().getFamily();
    }

    public static String getOsManufacturer() {
        return getOs().getManufacturer();
    }

    public static String getOsVersion() {
        return getOs().getVersion().getVersion() + " " + getOs().getVersion().getCodeName();
    }

    public static String getOsBuildVersion() {
        return getOs().getVersion().getBuildNumber();
    }

    public static String getOsSystemTypeArchitecture() {
        //full list
//        System.getProperties().list(System.out);        
        return System.getProperty("os.arch");
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="CPU Methods">
    public static String getCpuManufacturer() {
        arrayString = getCpu().getName().split(" ");
        return arrayString[0];
    }

    public static String getCpuModel() {
        arrayString = getCpu().getName().split(" ");
        return arrayString[2];
    }

    public static int getCpuNumberOfCores() {
        return getCpu().getPhysicalProcessorCount();
    }

    public static int getCpuNumberOfLogicalCores() {
        return getCpu().getLogicalProcessorCount();
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

    public static String getCpuManufacturerFrequencyAsString() {
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

    public static double getCpuManufacturerFrequencyAsDouble() {
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
    public static String getMemoryManufacturer() {
        String data = "";
        String temp = "";

        String[][] commands = new String[][]{{"CMD", "/C", "WMIC memorychip GET Manufacturer"}};
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String getMemoryCapacityAsString() {
        return FormatUtil.formatBytes(getMemory().getTotal());
    }

    public static String getMemoryUsedAsString() {
        getMemory().updateAttributes();
        return FormatUtil.formatBytes(getMemory().getTotal() - getMemory().getAvailable());
    }

    public static String getMemoryUsedPercentageAsString() {
        singleDouble = ((double) (getMemory().getTotal() - getMemory().getAvailable()) / (double) getMemory().getTotal()) * 100.0;
        return String.format("%.2f%%", singleDouble);
    }

    //--- DOUBLE 
    public static double getMemoryCapacityAsDouble() {
        singleString = getMemoryCapacityAsString();
        singleString = singleString.substring(0, (singleString.length() - 3));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }

    public static double getMemoryUsedAsDouble() {
        singleString = getMemoryUsedAsString();
        singleString = singleString.substring(0, (singleString.length() - 3));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }

    public static double getMemoryUsedPercentageAsDouble() {
        singleString = getMemoryUsedPercentageAsString();
        singleString = singleString.substring(0, (singleString.length() - 1));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Virtual Memory Methods">
    public static String getVirtualMemoryCapacityAsString() {
        return FormatUtil.formatBytes(getMemory().getVirtualMemory().getSwapTotal());
    }

    public static String getVirtualMemoryUsedAsString() {
        getMemory().updateAttributes();
        return FormatUtil.formatBytes(getMemory().getVirtualMemory().getSwapUsed());
    }

    public static String getVirtualMemoryUsedPercentageAsString() {
        singleDouble = ((double) (getMemory().getVirtualMemory().getSwapUsed()) / (double) getMemory().getVirtualMemory().getSwapTotal()) * 100.0;
        return String.format("%.2f%%", singleDouble);
    }

    //--- DOUBLE 
    public static double getVirtualMemoryCapacityAsDouble() {
        singleString = getVirtualMemoryCapacityAsString();
        singleString = singleString.substring(0, (singleString.length() - 3));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }

    public static double getVirtualMemoryUsedAsDouble() {
        singleString = getVirtualMemoryUsedAsString();
        singleString = singleString.substring(0, (singleString.length() - 3));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }

    public static double getVirtualMemoryUsedPercentageAsDouble() {
        singleString = getVirtualMemoryUsedPercentageAsString();
        singleString = singleString.substring(0, (singleString.length() - 1));
        singleString = singleString.replace(',', '.');
        return Double.parseDouble(singleString);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Disk Methods">
    public static String[] getDisksManufacturer() {
        arrayString = new String[getDisks().length];
        for (int count = 0; count < arrayString.length; count++) {
            singleString = getDisks()[count].getModel();
            arrayString2 = singleString.split(" ");
            arrayString[count] = arrayString2[0];
        }
        return arrayString;
    }

    public static String[] getDisksModel() {
        arrayString = new String[getDisks().length];
        for (int count = 0; count < arrayString.length; count++) {
            singleString = getDisks()[count].getModel();
            arrayString2 = singleString.split(" ");

            if (arrayString2.length >= 3) {
                arrayString[count] = arrayString2[1];
            } else {
                arrayString[count] = arrayString2[0];
            }

        }
        return arrayString;
    }

    public static String[] getDisksCapacityAsString() {
        OSFileStore[] fsArray = getOs().getFileSystem().getFileStores();
        arrayString = new String[fsArray.length];

        for (int count = 0; count < fsArray.length; count++) {
            arrayString[count] = FormatUtil.formatBytes(fsArray[count].getTotalSpace());
        }

        return arrayString;
    }

    public static String[] getDisksUsedAsString() {
        OSFileStore[] fsArray = getOs().getFileSystem().getFileStores();
        arrayString = new String[fsArray.length];

        for (int count = 0; count < fsArray.length; count++) {
            if (fsArray[count].getTotalSpace() > 0) {
                arrayString[count] = FormatUtil.formatBytes(fsArray[count].getTotalSpace() - fsArray[count].getUsableSpace());
            } else {
                arrayString[count] = FormatUtil.formatBytes(0);
            }
        }

        return arrayString;
    }

    public static String[] getDisksUsedPercentageAsString() {
        OSFileStore[] fsArray = getOs().getFileSystem().getFileStores();
        arrayString = new String[fsArray.length];

        for (int count = 0; count < fsArray.length; count++) {
            if (fsArray[count].getTotalSpace() > 0) {
                singleDouble = ((double) (fsArray[count].getTotalSpace() - fsArray[count].getUsableSpace()) / (double) fsArray[count].getTotalSpace()) * 100.0;
                arrayString[count] = String.format("%.2f%%", singleDouble);
            } else {
                arrayString[count] = String.format("%.2f%%", 0.0);
            }
        }

        return arrayString;
    }

    //--- DOUBLE 
    public static double[] getDisksCapacityAsDouble() {
        arrayString = getDisksCapacityAsString();
        arrayDoubleValues = new double[arrayString.length];

        for (int count = 0; count < arrayString.length; count++) {
            if (arrayString[count].equalsIgnoreCase("0 bytes")) {
                arrayDoubleValues[count] = 0;
            } else {
                singleString = arrayString[count].substring(0, (arrayString[count].length() - 4));
                singleString = singleString.replace(',', '.');
                arrayDoubleValues[count] = Double.parseDouble(singleString);
            }

        }

        return arrayDoubleValues;
    }

    public static double[] getDisksUsedAsDouble() {
        arrayString = getDisksUsedAsString();
        arrayDoubleValues = new double[arrayString.length];

        for (int count = 0; count < arrayString.length; count++) {
            if (arrayString[count].equalsIgnoreCase("0 bytes")) {
                arrayDoubleValues[count] = 0;
            } else {
                singleString = arrayString[count].substring(0, (arrayString[count].length() - 4));
                singleString = singleString.replace(',', '.');
                arrayDoubleValues[count] = Double.parseDouble(singleString);
            }

        }

        return arrayDoubleValues;
    }

    public static double[] getDisksUsedPercentageAsDouble() {
        arrayString = getDisksUsedPercentageAsString();
        arrayDoubleValues = new double[arrayString.length];

        for (int count = 0; count < arrayString.length; count++) {
            if (arrayString[count].equalsIgnoreCase("NaN%")) {
                arrayDoubleValues[count] = 0;
            } else {
                singleString = arrayString[count].substring(0, (arrayString[count].length() - 1));
                singleString = singleString.replace(',', '.');
                arrayDoubleValues[count] = Double.parseDouble(singleString);
            }

        }

        return arrayDoubleValues;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Processes Methods">
    public static String[][] getProcessesWithHeaderAsStringTable() {
        //order: [0] Name    [1] PID    [2] Using %CPU     [3] Using RAM    [4] Using %RAM 
        arrayStringTable = new String[getOs().getProcessCount() + 1][5];
        arrayString = new String[getOs().getProcessCount() + 1];

        arrayStringTable[0][0] = "Name";
        arrayStringTable[0][1] = "PID";
        arrayStringTable[0][2] = "Using %CPU";
        arrayStringTable[0][3] = "Using RAM";
        arrayStringTable[0][4] = "Using %RAM";

        OSProcess[] procs = getProcesses();

        for (int count = 1; count < arrayString.length; count++) {
            arrayStringTable[count][0] = procs[count - 1].getName();
            arrayStringTable[count][1] = String.valueOf(procs[count - 1].getProcessID());
            arrayStringTable[count][2] = String.format("%.2f%%", 100d * (procs[count - 1].getKernelTime() + procs[count - 1].getUserTime()) / procs[count - 1].getUpTime());
            arrayStringTable[count][3] = FormatUtil.formatBytes(procs[count - 1].getResidentSetSize());
            arrayStringTable[count][4] = String.format("%.2f%%", 100d * procs[count - 1].getResidentSetSize() / getMemory().getTotal());
        }

        return arrayStringTable;
    }

    public static String[][] getProcessesAsStringTable() {
        //order: [0] Name    [1] PID    [2] Using %CPU     [3] Using RAM    [4] Using %RAM 
        arrayStringTable = new String[getOs().getProcessCount()][5];
        arrayString = new String[getOs().getProcessCount()];

        OSProcess[] procs = getProcesses();

        for (int count = 0; count < arrayString.length; count++) {
            arrayStringTable[count][0] = procs[count].getName();
            arrayStringTable[count][1] = String.valueOf(procs[count].getProcessID());
            arrayStringTable[count][2] = String.format("%.2f%%", 100d * (procs[count].getKernelTime() + procs[count].getUserTime()) / procs[count].getUpTime());
            arrayStringTable[count][3] = FormatUtil.formatBytes(procs[count].getResidentSetSize());
            arrayStringTable[count][4] = String.format("%.2f%%", 100d * procs[count].getResidentSetSize() / getMemory().getTotal());
        }

        return arrayStringTable;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Network Methods">
    public static String getNetworkHostName() {
        return getOs().getNetworkParams().getHostName();
    }

    public static String getNetworkDomainName() {
        return getOs().getNetworkParams().getDomainName();
    }

    public static String getNetworkDnsServers() {
        return Arrays.toString(getOs().getNetworkParams().getDnsServers()).replace('[', ' ').replace(']', ' ').trim();
    }

    public static String getNetworkGatewayIp4() {
        return getOs().getNetworkParams().getIpv4DefaultGateway();
    }

    public static String getNetworkGatewayIp6() {
        return getOs().getNetworkParams().getIpv6DefaultGateway();
    }

    public static int getNetworkInterfaceLength() {
        return getNetwork().length;
    }

    public static String getNetworkInterfaceName(int indexOfNetworkInterface) {
        return getNetwork()[indexOfNetworkInterface].getName();
    }

    public static String getNetworkInterfaceManufacturer(int indexOfNetworkInterface) {
        return getNetwork()[indexOfNetworkInterface].getDisplayName();
    }

    public static String getNetworkInterfaceMacAdress(int indexOfNetworkInterface) {
        return getNetwork()[indexOfNetworkInterface].getMacaddr();
    }

    public static String getNetworkInterfaceIp4(int indexOfNetworkInterface) {
        return Arrays.toString(getNetwork()[indexOfNetworkInterface].getIPv4addr()).replace('[', ' ').replace(']', ' ').trim();
    }

    public static String getNetworkInterfaceIp6(int indexOfNetworkInterface) {
        return Arrays.toString(getNetwork()[indexOfNetworkInterface].getIPv6addr()).replace('[', ' ').replace(']', ' ').trim();
    }

    public static String getNetworkInterfaceBytesReceivedAsString(int indexOfNetworkInterface) {
        boolean hasData
                = getNetwork()[indexOfNetworkInterface].getBytesRecv() > 0
                || getNetwork()[indexOfNetworkInterface].getBytesSent() > 0
                || getNetwork()[indexOfNetworkInterface].getPacketsRecv() > 0
                || getNetwork()[indexOfNetworkInterface].getPacketsSent() > 0;

        return hasData ? FormatUtil.formatBytes(getNetwork()[indexOfNetworkInterface].getBytesRecv()) : "0";
    }

    public static String getNetworkInterfaceBytesSentAsString(int indexOfNetworkInterface) {
        boolean hasData
                = getNetwork()[indexOfNetworkInterface].getBytesRecv() > 0
                || getNetwork()[indexOfNetworkInterface].getBytesSent() > 0
                || getNetwork()[indexOfNetworkInterface].getPacketsRecv() > 0
                || getNetwork()[indexOfNetworkInterface].getPacketsSent() > 0;

        return hasData ? FormatUtil.formatBytes(getNetwork()[indexOfNetworkInterface].getBytesSent()) : "0";
    }

    //--- DOUBLE 
    public static double getNetworkInterfaceBytesReceivedAsDouble(int indexOfNetworkInterface) {
        singleString = getNetworkInterfaceBytesReceivedAsString(indexOfNetworkInterface);
        
        if (singleString.equalsIgnoreCase("0 bytes")) {
            singleDouble = 0;
        } else {
            singleString = singleString.substring(0, (singleString.length() - 4));
            singleString = singleString.replace(',', '.');
            singleDouble = Double.parseDouble(singleString);
        }

        return singleDouble;
    }
    
    public static double getNetworkInterfaceBytesSentAsDouble(int indexOfNetworkInterface) {
        singleString = getNetworkInterfaceBytesSentAsString(indexOfNetworkInterface);
        
        if (singleString.equalsIgnoreCase("0 bytes")) {
            singleDouble = 0;
        } else {
            singleString = singleString.substring(0, (singleString.length() - 4));
            singleString = singleString.replace(',', '.');
            singleDouble = Double.parseDouble(singleString);
        }

        return singleDouble;
    }
    // </editor-fold>

}
