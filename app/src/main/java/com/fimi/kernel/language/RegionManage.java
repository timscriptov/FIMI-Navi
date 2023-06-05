package com.fimi.kernel.language;

import com.fimi.kernel.Constants;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;


public class RegionManage {
    private String mCountrySelect;
    private int mServiceType = -1;

    public int getCountryType() {
        ServiceItem serviceItem = SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
        int i = 0;
        while (true) {
            if (i >= ServiceItem.getServicename().length) {
                break;
            } else if (ServiceItem.getServicename()[i] != serviceItem.getInfo()) {
                i++;
            } else {
                this.mServiceType = i;
                break;
            }
        }
        if (this.mServiceType == -1) {
            this.mServiceType = 0;
        }
        return this.mServiceType;
    }
}
