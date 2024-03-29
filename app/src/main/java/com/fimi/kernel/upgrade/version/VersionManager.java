package com.fimi.kernel.upgrade.version;

import java.util.HashMap;
import java.util.Map;


public class VersionManager {
    private static final VersionManager mVersionManager = new VersionManager();
    private final Map<Integer, FirmwareBean> hasMap = new HashMap();

    public static VersionManager getInstance() {
        return mVersionManager;
    }

    public void saveFimwareBean(FirmwareBean b) {
        this.hasMap.put(Integer.valueOf(b.getSysId()), b);
    }

    public FirmwareBean getFimwareBeanBySysId(int sysid) {
        return this.hasMap.get(Integer.valueOf(sysid));
    }

    public void clear() {
        this.hasMap.clear();
    }
}
