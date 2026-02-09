/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.component.ArchetypeChunk
 *  com.hypixel.hytale.component.CommandBuffer
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
 *  com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
 *  com.hypixel.hytale.server.core.universe.PlayerRef
 *  com.hypixel.hytale.server.core.universe.world.World
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 */
package com.marasca.mobcounter.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.marasca.mobcounter.api.context.BlockContext;
import com.marasca.mobcounter.api.context.Context;
import com.marasca.mobcounter.api.context.EntityContext;
import com.marasca.mobcounter.api.info.AnchorBuilder;
import com.marasca.mobcounter.api.info.InfoBuilder;
import com.marasca.mobcounter.api.info.InfoProvider;
import com.marasca.mobcounter.api.info.InfoValue;
import com.marasca.mobcounter.impl.info.VanillaBlockInfoProvider;
import com.marasca.mobcounter.impl.info.VanillaEntityInfoProvider;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MobCounterHud
extends CustomUIHud {
    private static final List<InfoProvider<BlockContext>> blockInfoProviders = new LinkedList<InfoProvider<BlockContext>>();
    private static final List<InfoProvider<EntityContext>> entityInfoProviders = new LinkedList<InfoProvider<EntityContext>>();
    @Nullable
    private InfoBuilder info;
    @Nullable
    private BlockContext blockContext;
    @Nullable
    private EntityContext entityContext;

    public MobCounterHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void updateHud(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        block2: {
            PlayerRef playerRef;
            block3: {
                this.info = new InfoBuilder();
                playerRef = (PlayerRef)archetypeChunk.getComponent(index, PlayerRef.getComponentType());
                World world = ((EntityStore)store.getExternalData()).getWorld();
                if (playerRef == null || world == null) break block2;
                this.entityContext = EntityContext.create(playerRef, dt, index, archetypeChunk, store, commandBuffer);
                if (this.entityContext == null) break block3;
                for (InfoProvider<EntityContext> provider : entityInfoProviders) {
                    provider.updateDescription(this.entityContext, this.info);
                }
                break block2;
            }
            this.blockContext = BlockContext.create(playerRef, dt, index, archetypeChunk, store, commandBuffer);
            if (this.blockContext == null) break block2;
            for (InfoProvider<BlockContext> provider : blockInfoProviders) {
                provider.updateDescription(this.blockContext, this.info);
            }
        }
    }

    protected void build(@Nonnull UICommandBuilder ui) {
        block4: {
            block3: {
                if (this.info != null && this.info.canDisplay()) {
                    ui.append("Hud/MobCounter/MobCounter.ui");
                    AnchorBuilder anchorBuilder = new AnchorBuilder().setTop(20).setRight(20);
                    this.info.values().filter(infoValue -> infoValue != InfoValue.EMPTY).forEach(infoValue -> infoValue.build(ui, anchorBuilder, "#Info"));
                    this.info.getIcon().build(ui, anchorBuilder, "#IconContainer");
                    ui.setObject("#MobCounterHud.Anchor", (Object)anchorBuilder.build());
                }
                if (this.entityContext == null) break block3;
                for (InfoProvider<EntityContext> infoProvider : entityInfoProviders) {
                    infoProvider.modifyUI(this.entityContext, ui);
                }
                break block4;
            }
            if (this.blockContext == null) break block4;
            for (InfoProvider<BlockContext> infoProvider : blockInfoProviders) {
                infoProvider.modifyUI(this.blockContext, ui);
            }
        }
    }

    static {
        blockInfoProviders.add(new VanillaBlockInfoProvider());
        entityInfoProviders.add(new VanillaEntityInfoProvider());
    }
}

