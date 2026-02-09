/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 *  javax.annotation.Nonnull
 */
package com.jarhax.eyespy.api.info;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import javax.annotation.Nonnull;

public interface InfoValue {
    public static final InfoValue EMPTY = (uICommandBuilder, anchorBuilder, string) -> {};

    public void build(@Nonnull UICommandBuilder var1, @Nonnull AnchorBuilder var2, String var3);
}

