/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.builtin.crafting.state.BenchState
 *  com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState
 *  com.hypixel.hytale.server.core.Message
 *  com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType
 *  com.hypixel.hytale.server.core.asset.type.item.config.Item
 *  com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties
 *  com.hypixel.hytale.server.core.inventory.ItemStack
 *  com.hypixel.hytale.server.core.universe.world.meta.BlockState
 *  com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState
 */
package com.marasca.mobcounter.impl.info;

import com.hypixel.hytale.builtin.crafting.state.BenchState;
import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.marasca.mobcounter.MobCounter;
import com.marasca.mobcounter.api.MessageHelpers;
import com.marasca.mobcounter.api.context.BlockContext;
import com.marasca.mobcounter.api.info.InfoBuilder;
import com.marasca.mobcounter.api.info.InfoProvider;
import com.marasca.mobcounter.api.info.InfoValue;
import com.marasca.mobcounter.api.info.values.GroupValue;
import com.marasca.mobcounter.api.info.values.IconValue;
import com.marasca.mobcounter.api.info.values.ItemGridValue;
import com.marasca.mobcounter.api.info.values.LabelValue;
import com.marasca.mobcounter.api.info.values.ProgressBarValue;
import java.awt.Color;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VanillaBlockInfoProvider
implements InfoProvider<BlockContext> {
    private static final Color color = new Color(85, 85, 255);
    private static final Color color2 = new Color(170, 170, 170);

    @Override
    public void updateDescription(BlockContext context, InfoBuilder infoBuilder) {
        if (!context.getBlock().getId().equals("Empty")) {
            String owner;
            infoBuilder.set("Header", s -> new LabelValue((String)s, VanillaBlockInfoProvider.getDisplayName(context.getBlock()), 24));
            Item item = context.getBlock().getItem();
            if (item != null) {
                infoBuilder.setIcon(new IconValue(item.getId()));
            }
            if (context.getState() != null) {
                infoBuilder.set("Body", s -> {
                    BlockState blockState = context.getState();
                    Objects.requireNonNull(blockState);
                    
                    if (blockState instanceof ProcessingBenchState) {
                        ProcessingBenchState processor = (ProcessingBenchState)blockState;
                        ArrayList<InfoValue> values = new ArrayList<InfoValue>();
                        values.add(new LabelValue(s + "Tier", MessageHelpers.tier(processor.getTierLevel()).color(color2)));
                        if (processor.isActive() && processor.getRecipe() != null && !Float.isNaN(processor.getInputProgress()) && !Float.isNaN(processor.getRecipe().getTimeSeconds())) {
                            float value = processor.getInputProgress() / processor.getRecipe().getTimeSeconds();
                            values.add(new ProgressBarValue(s + "Progress", (float)Math.round(value * 1000.0f) / 1000.0f));
                        }
                        return new GroupValue((String)s, (List<InfoValue>)values);
                    } else if (blockState instanceof BenchState) {
                        BenchState bench = (BenchState)blockState;
                        return new LabelValue((String)s, MessageHelpers.tier(bench.getTierLevel()).color(color2));
                    } else if (blockState instanceof ItemContainerState) {
                        ItemContainerState container = (ItemContainerState)blockState;
                        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                        block5: for (short i = 0; i < container.getItemContainer().getCapacity(); i = (short)(i + 1)) {
                            ItemStack stack = container.getItemContainer().getItemStack(i);
                            if (stack == null) continue;
                            for (int j = 0; j < stacks.size(); ++j) {
                                ItemStack itemStack = (ItemStack)stacks.get(j);
                                if (!itemStack.isEquivalentType(stack)) continue;
                                stacks.set(j, itemStack.withQuantity(itemStack.getQuantity() + stack.getQuantity()));
                                continue block5;
                            }
                            ItemStack safeStack = stack.withMetadata(null).withDurability(stack.getMaxDurability() > 0.0 ? Math.max(1.0, stack.getDurability()) : stack.getDurability());
                            stacks.add(safeStack);
                        }
                        return new ItemGridValue((String)s, (List<ItemStack>)stacks);
                    } else {
                        return InfoValue.EMPTY;
                    }
                });
            }
            if ((owner = MobCounter.OWNERSHIP.get(context.getBlock().getId())) != null) {
                infoBuilder.set("Footer", s -> new LabelValue((String)s, Message.raw((String)owner).color(color).bold(true)));
            }
        }
    }

    private static Message getDisplayName(BlockType type) {
        String nameKey;
        ItemTranslationProperties translations;
        Item item = type.getItem();
        if (item != null && (translations = item.getTranslationProperties()) != null && (nameKey = translations.getName()) != null) {
            return Message.translation((String)nameKey);
        }
        return Message.raw((String)type.getId());
    }
}

