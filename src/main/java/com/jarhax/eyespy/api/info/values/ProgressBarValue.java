/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 *  javax.annotation.Nonnull
 */
package com.jarhax.eyespy.api.info.values;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.api.info.InfoValue;
import javax.annotation.Nonnull;

public class ProgressBarValue
implements InfoValue {
    private final String id;
    private final float value;

    public ProgressBarValue(String id, float value) {
        this.id = id;
        this.value = Math.clamp(value, 0.0f, 1.0f);
    }

    @Override
    public void build(@Nonnull UICommandBuilder ui, @Nonnull AnchorBuilder anchorBuilder, String selector) {
        ui.appendInline(selector, "Group {\n  Anchor: (Height: 12);\n  Background: \"Hud/EyeSpy/ProcessingBar.png\";\n\n  ProgressBar #%s {\n    Value: %s;\n    BarTexturePath: \"Hud/EyeSpy/ProcessingBarFill.png\";\n    EffectTexturePath: \"Hud/EyeSpy/ProcessingBarEffect.png\";\n    EffectWidth: 102;\n    EffectHeight: 58;\n    EffectOffset: 74;\n  }\n}".formatted(this.id, Float.valueOf(this.value)));
        anchorBuilder.addHeight(12);
    }
}

