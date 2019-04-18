package oshi;

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

public class SimpleSystemInfo {

    private static SystemInfo systemInfo = new SystemInfo();
    
    // <editor-fold defaultstate="collapsed" desc="Basic Methods... ">   
    public static SystemInfo getOshiSystemInfo() {
        return systemInfo;
    }

    public static OperatingSystem getOS() {
        return systemInfo.getOperatingSystem();
    }

    public static OperatingSystemVersion getOSVersion() {
        return getOS().getVersion();
    }

    public static HardwareAbstractionLayer getHardware() {
        return systemInfo.getHardware();
    }

    public static CentralProcessor getCPU() {
        return getHardware().getProcessor();
    }

    public static NetworkIF[] getNetwork() {
        return getHardware().getNetworkIFs();
    }

    public static OSProcess getProcess(int processID) {
        return getOS().getProcess(processID);
    }
    
    public static GlobalMemory getMemory() {
        return getHardware().getMemory();
    }

    public static UsbDevice[] getUsbDevices(boolean showDevicesTree) {
        return getHardware().getUsbDevices(showDevicesTree);
    }
    // </editor-fold>  
    
    
    
}
