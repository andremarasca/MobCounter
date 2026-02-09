/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.inventory.ItemStack
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 *  javax.annotation.Nonnull
 */
package com.jarhax.eyespy.api.info.values;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.api.info.InfoValue;
import java.util.List;
import javax.annotation.Nonnull;

public class ItemGridValue
implements InfoValue {
    private final String id;
    private final List<ItemStack> stacks;

    public ItemGridValue(String id, List<ItemStack> stacks) {
        this.id = id;
        this.stacks = stacks;
    }

    @Override
    public void build(@Nonnull UICommandBuilder ui, @Nonnull AnchorBuilder anchorBuilder, String selector) {
        ui.appendInline(selector, "ItemGrid #%s {\n        SlotsPerRow: 9;\n        RenderItemQualityBackground: true;\n        InfoDisplay: None;\n        Style: (\n          SlotSize: 32,\n          SlotSpacing: 0,\n          SlotIconSize: 32\n        );\n      }".formatted(this.id));
        ui.set("#%s.ItemStacks".formatted(this.id), this.stacks);
        int rows = (int)Math.ceil((double)this.stacks.size() / 9.0);
        anchorBuilder.addHeight(rows * 40);
    }
}

