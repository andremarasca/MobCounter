/*
 * Mob Counter HUD - Shows count of mobs within 32 blocks of the player
 */
package com.marasca.mobcounter.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.marasca.mobcounter.api.context.PlayerContext;
import com.marasca.mobcounter.api.info.AnchorBuilder;
import com.marasca.mobcounter.api.info.InfoBuilder;
import com.marasca.mobcounter.api.info.InfoProvider;
import com.marasca.mobcounter.api.info.InfoValue;
import com.marasca.mobcounter.impl.info.MobCountInfoProvider;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MobCounterHud
extends CustomUIHud {
    private static final List<InfoProvider<PlayerContext>> playerInfoProviders = new LinkedList<InfoProvider<PlayerContext>>();
    @Nullable
    private InfoBuilder info;
    @Nullable
    private PlayerContext playerContext;

    public MobCounterHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void updateHud(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        this.info = new InfoBuilder();
        PlayerRef playerRef = (PlayerRef)archetypeChunk.getComponent(index, PlayerRef.getComponentType());
        
        if (playerRef == null) {
            return;
        }
        
        this.playerContext = PlayerContext.create(playerRef, dt, index, archetypeChunk, store, commandBuffer);
        if (this.playerContext != null) {
            for (InfoProvider<PlayerContext> provider : playerInfoProviders) {
                provider.updateDescription(this.playerContext, this.info);
            }
        }
    }

    protected void build(@Nonnull UICommandBuilder ui) {
        if (this.info != null && this.info.canDisplay()) {
            ui.append("Hud/MobCounter/MobCounter.ui");
            AnchorBuilder anchorBuilder = new AnchorBuilder().setTop(20).setRight(20);
            this.info.values().filter(infoValue -> infoValue != InfoValue.EMPTY).forEach(infoValue -> infoValue.build(ui, anchorBuilder, "#Info"));
            this.info.getIcon().build(ui, anchorBuilder, "#IconContainer");
            ui.setObject("#MobCounterHud.Anchor", (Object)anchorBuilder.build());
        }
        
        if (this.playerContext != null) {
            for (InfoProvider<PlayerContext> infoProvider : playerInfoProviders) {
                infoProvider.modifyUI(this.playerContext, ui);
            }
        }
    }

    static {
        playerInfoProviders.add(new MobCountInfoProvider());
    }
}

