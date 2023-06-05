package com.fimi.host.Entity;

import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.animutils.IOUtils;
import com.fimi.network.entity.UpfirewareDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DownloadFwSelectInfo implements Serializable, Cloneable {
    private long fileSize;
    private boolean isForceSign;
    private ProductEnum product;
    private String title;
    private final List<UpfirewareDto> mUpfirewareDtoList = new ArrayList();
    private boolean isSelect = true;
    private int index = 0;
    private final StringBuffer detailBuffer = new StringBuffer();

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }

    public ProductEnum getProduct() {
        return this.product;
    }

    public void setProduct(ProductEnum product) {
        this.product = product;
    }

    public boolean isForceSign() {
        return this.isForceSign;
    }

    public void setForceSign(boolean forceSign) {
        this.isForceSign = forceSign;
    }

    public void addDetail(String detail) {
        this.index++;
        this.detailBuffer.append(detail + IOUtils.LINE_SEPARATOR_UNIX);
    }

    public String getDetail() {
        return this.detailBuffer.toString();
    }

    public boolean hasData() {
        return this.index > 0;
    }

    public Object clone() {
        try {
            DownloadFwSelectInfo obj = (DownloadFwSelectInfo) super.clone();
            return obj;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void add2List(UpfirewareDto dto) {
        this.mUpfirewareDtoList.add(dto);
    }

    public List<UpfirewareDto> getDtoList() {
        return this.mUpfirewareDtoList;
    }

    public void addAll2List(List<UpfirewareDto> list) {
        this.mUpfirewareDtoList.addAll(list);
    }
}
