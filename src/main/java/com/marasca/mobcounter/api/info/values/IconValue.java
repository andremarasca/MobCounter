/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 *  javax.annotation.Nonnull
 */
package com.marasca.mobcounter.api.info.values;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.marasca.mobcounter.api.info.AnchorBuilder;
import com.marasca.mobcounter.api.info.InfoValue;
import javax.annotation.Nonnull;

public class IconValue
implements InfoValue {
    private final String iconId;

    public IconValue(String iconId) {
        this.iconId = iconId;
    }

    @Override
    public void build(@Nonnull UICommandBuilder ui, @Nonnull AnchorBuilder anchorBuilder, String selector) {
        ui.insertBeforeInline(selector, "ItemIcon #Icon {\n    Anchor: (Width: 64, Height: 64);\n    ItemId: \"%s\";\n}".formatted(this.iconId));
        anchorBuilder.ensureHeight(100);
    }
}

