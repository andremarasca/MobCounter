package com.marasca.mobcounter.api.context;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nullable;

/**
 * Context for the player that is used to count mobs around them.
 * Unlike EntityContext, this doesn't require the player to be looking at an entity.
 */
public class PlayerContext extends Context {
    private final World world;
    private final Vector3d playerPosition;
    private final Ref<EntityStore> playerEntityRef;

    public PlayerContext(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, 
                         Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, 
                         PlayerRef observer, World world, Vector3d playerPosition,
                         Ref<EntityStore> playerEntityRef) {
        super(delta, index, archetypeChunk, store, commandBuffer, observer, null);
        this.world = world;
        this.playerPosition = playerPosition;
        this.playerEntityRef = playerEntityRef;
    }

    public World getWorld() {
        return this.world;
    }

    public Vector3d getPlayerPosition() {
        return this.playerPosition;
    }

    public Ref<EntityStore> getPlayerEntityRef() {
        return this.playerEntityRef;
    }

    @Nullable
    public static PlayerContext create(PlayerRef player, float dt, int index, 
                                        ArchetypeChunk<EntityStore> archetypeChunk, 
                                        Store<EntityStore> store, 
                                        CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> playerRef = player.getReference();
        if (playerRef == null) {
            return null;
        }
        
        Store<EntityStore> playerStore = playerRef.getStore();
        World world = ((EntityStore)store.getExternalData()).getWorld();
        if (world == null) {
            return null;
        }
        
        TransformComponent transform = (TransformComponent)playerStore.getComponent(playerRef, TransformComponent.getComponentType());
        if (transform == null) {
            return null;
        }
        
        Vector3d position = transform.getPosition();
        
        return new PlayerContext(dt, index, archetypeChunk, store, commandBuffer, player, world, position, playerRef);
    }
}
