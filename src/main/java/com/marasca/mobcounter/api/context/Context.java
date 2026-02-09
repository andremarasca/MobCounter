/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.component.ArchetypeChunk
 *  com.hypixel.hytale.component.CommandBuffer
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.server.core.universe.PlayerRef
 *  com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 */
package com.marasca.mobcounter.api.context;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class Context {
    private final float delta;
    private final int index;
    private final ArchetypeChunk<EntityStore> archetypeChunk;
    private final Store<EntityStore> store;
    private final CommandBuffer<EntityStore> commandBuffer;
    private final PlayerRef observer;
    private final WorldChunk chunk;

    public Context(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, PlayerRef observer, WorldChunk chunk) {
        this.delta = delta;
        this.index = index;
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.observer = observer;
        this.chunk = chunk;
    }

    public float getDelta() {
        return this.delta;
    }

    public ArchetypeChunk<EntityStore> getArchetypeChunk() {
        return this.archetypeChunk;
    }

    public int getIndex() {
        return this.index;
    }

    public Store<EntityStore> getStore() {
        return this.store;
    }

    public PlayerRef getObserver() {
        return this.observer;
    }

    public CommandBuffer<EntityStore> getCommandBuffer() {
        return this.commandBuffer;
    }

    public PlayerRef observer() {
        return this.observer;
    }

    public WorldChunk getChunk() {
        return this.chunk;
    }
}

