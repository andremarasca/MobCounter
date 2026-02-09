/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 */
package com.jarhax.eyespy.api.info;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.context.Context;
import com.jarhax.eyespy.api.info.InfoBuilder;

public interface InfoProvider<CTX extends Context> {
    public void updateDescription(CTX var1, InfoBuilder var2);

    default public void modifyUI(CTX context, UICommandBuilder ui) {
    }
}

