package com.fimi.kernel;

import android.content.Context;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class AppBlockCanaryContext extends BlockCanaryContext {
    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String provideQualifier() {
        return "unknown";
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String provideUid() {
        return BlockInfo.KEY_UID;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String provideNetworkType() {
        return "unknown";
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public int provideMonitorDuration() {
        return -1;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public int provideBlockThreshold() {
        return 500;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public int provideDumpInterval() {
        return provideBlockThreshold();
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String providePath() {
        return "/blockcanary/";
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean displayNotification() {
        return true;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean zip(File[] src, File dest) {
        return false;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public void upload(File zippedFile) {
        throw new UnsupportedOperationException();
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public List<String> concernPackages() {
        return null;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean filterNonConcernStack() {
        return false;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public List<String> provideWhiteList() {
        LinkedList<String> whiteList = new LinkedList<>();
        whiteList.add("org.chromium");
        return whiteList;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean deleteFilesInWhiteList() {
        return true;
    }

    @Override
    // com.github.moduth.blockcanary.BlockCanaryContext, com.github.moduth.blockcanary.BlockInterceptor
    public void onBlock(Context context, BlockInfo blockInfo) {
    }
}
