/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.builtin.path.PathPlugin
 *  com.hypixel.hytale.component.ArchetypeChunk
 *  com.hypixel.hytale.component.CommandBuffer
 *  com.hypixel.hytale.component.Component
 *  com.hypixel.hytale.component.Ref
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.math.util.ChunkUtil
 *  com.hypixel.hytale.math.vector.Vector3d
 *  com.hypixel.hytale.server.core.modules.entity.component.ModelComponent
 *  com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
 *  com.hypixel.hytale.server.core.universe.PlayerRef
 *  com.hypixel.hytale.server.core.universe.world.World
 *  com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 *  com.hypixel.hytale.server.core.util.TargetUtil
 *  javax.annotation.Nullable
 */
package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.builtin.path.PathPlugin;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.jarhax.eyespy.api.context.Context;
import javax.annotation.Nullable;

public class EntityContext
extends Context {
    private final Ref<EntityStore> entity;

    public EntityContext(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, PlayerRef observer, WorldChunk chunk, Ref<EntityStore> entity) {
        super(delta, index, archetypeChunk, store, commandBuffer, observer, chunk);
        this.entity = entity;
    }

    public Ref<EntityStore> entity() {
        return this.entity;
    }

    @Nullable
    public static EntityContext create(PlayerRef player, float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        Component component;
        Ref playerRef = player.getReference();
        if (playerRef == null) {
            return null;
        }
        Store playerStore = playerRef.getStore();
        World world = ((EntityStore)store.getExternalData()).getWorld();
        Ref targetEntity = TargetUtil.getTargetEntity((Ref)archetypeChunk.getReferenceTo(index), commandBuffer);
        TransformComponent transform = (TransformComponent)playerStore.getComponent(playerRef, TransformComponent.getComponentType());
        if (transform == null) {
            return null;
        }
        Vector3d position = transform.getPosition();
        if (targetEntity != null && (component = store.getComponent(targetEntity, ModelComponent.getComponentType())) instanceof ModelComponent) {
            ModelComponent mc = (ModelComponent)component;
            if (mc.getModel().equals((Object)PathPlugin.get().getPathMarkerModel())) {
                return null;
            }
            int x = (int)Math.floor(position.x);
            int z = (int)Math.floor(position.z);
            long chunkIndex = ChunkUtil.indexChunkFromBlock((int)x, (int)z);
            WorldChunk chunk = world.getChunkIfLoaded(chunkIndex);
            return new EntityContext(dt, index, archetypeChunk, store, commandBuffer, player, chunk, (Ref<EntityStore>)targetEntity);
        }
        return null;
    }
}

