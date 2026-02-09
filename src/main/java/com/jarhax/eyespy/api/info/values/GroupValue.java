/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 *  javax.annotation.Nonnull
 *  org.checkerframework.checker.nullness.compatqual.NonNullDecl
 */
package com.jarhax.eyespy.api.info.values;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.api.info.InfoValue;
import java.util.List;
import javax.annotation.Nonnull;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class GroupValue
implements InfoValue {
    private final String id;
    private final List<InfoValue> values;

    public GroupValue(String id, List<InfoValue> values) {
        this.id = id;
        this.values = values;
    }

    @Override
    public void build(@NonNullDecl UICommandBuilder ui, @Nonnull AnchorBuilder anchorBuilder, String selector) {
        ui.appendInline(selector, "Group #%s {\n    LayoutMode: Top;\n}".formatted(this.id));
        for (InfoValue value : this.values) {
            value.build(ui, anchorBuilder, "#" + this.id);
        }
    }
}

