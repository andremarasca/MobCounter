/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.component.ArchetypeChunk
 *  com.hypixel.hytale.component.CommandBuffer
 *  com.hypixel.hytale.component.Holder
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.component.query.Query
 *  com.hypixel.hytale.component.system.tick.EntityTickingSystem
 *  com.hypixel.hytale.server.core.entity.EntityUtils
 *  com.hypixel.hytale.server.core.entity.entities.Player
 *  com.hypixel.hytale.server.core.universe.PlayerRef
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 *  javax.annotation.Nonnull
 */
package com.jarhax.eyespy.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.EyeSpy;
import com.jarhax.eyespy.impl.hud.EyeSpyHud;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public class PlayerTickSystem
extends EntityTickingSystem<EntityStore> {
    @Nonnull
    private final Query<EntityStore> query;
    private final Map<PlayerRef, EyeSpyHud> huds = new HashMap<PlayerRef, EyeSpyHud>();

    public PlayerTickSystem() {
        this.query = Query.and((Query[])new Query[]{Player.getComponentType()});
    }

    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Holder holder = EntityUtils.toHolder((int)index, archetypeChunk);
        Player player = (Player)holder.getComponent(Player.getComponentType());
        PlayerRef playerRef = (PlayerRef)holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        EyeSpy.provider.showHud(dt, index, archetypeChunk, store, commandBuffer);
    }

    @Nonnull
    public Query<EntityStore> getQuery() {
        return this.query;
    }
}

