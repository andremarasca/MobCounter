/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 */
package com.marasca.mobcounter.api.info;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.marasca.mobcounter.api.context.Context;
import com.marasca.mobcounter.api.info.InfoBuilder;

public interface InfoProvider<CTX extends Context> {
    public void updateDescription(CTX var1, InfoBuilder var2);

    default public void modifyUI(CTX context, UICommandBuilder ui) {
    }
}

