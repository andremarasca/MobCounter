/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.component.ArchetypeChunk
 *  com.hypixel.hytale.component.CommandBuffer
 *  com.hypixel.hytale.component.Ref
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.math.util.ChunkUtil
 *  com.hypixel.hytale.math.vector.Vector3i
 *  com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType
 *  com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
 *  com.hypixel.hytale.server.core.universe.PlayerRef
 *  com.hypixel.hytale.server.core.universe.world.World
 *  com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk
 *  com.hypixel.hytale.server.core.universe.world.meta.BlockState
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 *  com.hypixel.hytale.server.core.util.FillerBlockUtil
 *  com.hypixel.hytale.server.core.util.TargetUtil
 *  javax.annotation.Nullable
 */
package com.marasca.mobcounter.api.context;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.marasca.mobcounter.api.context.Context;
import javax.annotation.Nullable;

public class BlockContext
extends Context {
    private final BlockType block;
    private final BlockState state;
    private final Vector3i targetPos;
    private final Vector3i offsetPos;

    public BlockContext(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, PlayerRef observer, WorldChunk chunk, Vector3i targetPos, Vector3i offsetPos, BlockType block, BlockState state) {
        super(delta, index, archetypeChunk, store, commandBuffer, observer, chunk);
        this.targetPos = targetPos;
        this.offsetPos = offsetPos;
        this.block = block;
        this.state = state;
    }

    public BlockType getBlock() {
        return this.block;
    }

    @Nullable
    public BlockState getState() {
        return this.state;
    }

    public Vector3i getTargetPos() {
        return this.targetPos;
    }

    public Vector3i getOffsetPos() {
        return this.offsetPos;
    }

    @Nullable
    public static BlockContext create(PlayerRef player, float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        Ref playerRef = player.getReference();
        if (playerRef == null) {
            return null;
        }
        Store playerStore = playerRef.getStore();
        TransformComponent transform = (TransformComponent)playerStore.getComponent(playerRef, TransformComponent.getComponentType());
        World world = ((EntityStore)store.getExternalData()).getWorld();
        Vector3i targetBlockPos = TargetUtil.getTargetBlock((Ref)archetypeChunk.getReferenceTo(index), (double)5.0, commandBuffer);
        if (transform != null && targetBlockPos != null) {
            WorldChunk baseChunk;
            int x = targetBlockPos.x;
            int y = targetBlockPos.y;
            int z = targetBlockPos.z;
            long chunkIndex = ChunkUtil.indexChunkFromBlock((int)x, (int)z);
            WorldChunk chunk = world.getChunkIfLoaded(chunkIndex);
            if (chunk == null) {
                return null;
            }
            Vector3i basePos = BlockContext.resolveBaseBlock(chunk, x, y, z);
            long baseChunkIndex = ChunkUtil.indexChunkFromBlock((int)basePos.x, (int)basePos.z);
            WorldChunk worldChunk = baseChunk = baseChunkIndex == chunkIndex ? chunk : world.getChunkIfLoaded(baseChunkIndex);
            if (baseChunk == null) {
                return null;
            }
            BlockType block = baseChunk.getBlockType(basePos.x, basePos.y, basePos.z);
            BlockState state = baseChunk.getState(basePos.x, basePos.y, basePos.z);
            return new BlockContext(dt, index, archetypeChunk, store, commandBuffer, player, baseChunk, targetBlockPos, basePos, block, state);
        }
        return null;
    }

    public static Vector3i resolveBaseBlock(WorldChunk chunk, int x, int y, int z) {
        int filler = chunk.getFiller(x, y, z);
        if (filler == 0) {
            return new Vector3i(x, y, z);
        }
        return new Vector3i(x - FillerBlockUtil.unpackX((int)filler), y - FillerBlockUtil.unpackY((int)filler), z - FillerBlockUtil.unpackZ((int)filler));
    }
}

