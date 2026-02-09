package com.marasca.mobcounter.impl.info;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * System that scans for mobs and tracks them for each player.
 * Only counts entities with Health stat (actual mobs, not items).
 * Uses 3x3 chunk detection around each player for efficiency.
 */
public class MobCounterSystem extends EntityTickingSystem<EntityStore> {
    
    /**
     * Info about a detected mob including name and distances.
     */
    public static class MobInfo {
        public final String name;
        public final int dx;
        public final int dy;
        public final int dz;
        public final int distance;
        
        public MobInfo(String name, int dx, int dy, int dz, int distance) {
            this.name = name;
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
            this.distance = distance;
        }
    }
    
    // Static map to store mob info per player UUID
    private static final Map<UUID, List<MobInfo>> mobsByPlayer = new ConcurrentHashMap<>();
    
    // Map to store player positions for distance calculation
    private static final Map<UUID, Vector3d> playerPositions = new ConcurrentHashMap<>();
    
    // Map to store player chunk indices for 3x3 chunk detection
    private static final Map<UUID, Set<Long>> playerChunkAreas = new ConcurrentHashMap<>();
    
    // Health stat index for checking if entity is a mob
    private static int healthStatIndex = -1;
    
    @Nonnull
    private final Query<EntityStore> query;
    
    public MobCounterSystem() {
        // Query for all entities with TransformComponent and EntityStatMap (entities with stats = mobs)
        this.query = Query.and(TransformComponent.getComponentType(), EntityStatMap.getComponentType());
    }
    
    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, 
                     @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        
        // Initialize health stat index on first tick
        if (healthStatIndex < 0) {
            healthStatIndex = EntityStatType.getAssetMap().getIndex("Health");
        }
        
        Ref<EntityStore> entityRef = archetypeChunk.getReferenceTo(index);
        
        // Get transform component for this entity
        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform == null) {
            return;
        }
        
        Vector3d entityPos = transform.getPosition();
        int entityChunkX = (int) Math.floor(entityPos.x) >> 4;
        int entityChunkZ = (int) Math.floor(entityPos.z) >> 4;
        long entityChunkIndex = ChunkUtil.indexChunk(entityChunkX, entityChunkZ);
        
        // Check if this entity is a player - if so, register their position and chunk area
        PlayerRef playerRef = (PlayerRef) store.getComponent(entityRef, PlayerRef.getComponentType());
        if (playerRef != null) {
            UUID playerUuid = playerRef.getUuid();
            
            // Store player position
            playerPositions.put(playerUuid, entityPos);
            
            // Calculate 3x3 chunk area around player
            Set<Long> chunkArea = new HashSet<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    chunkArea.add(ChunkUtil.indexChunk(entityChunkX + dx, entityChunkZ + dz));
                }
            }
            playerChunkAreas.put(playerUuid, chunkArea);
            
            // Reset mob list for this player for this tick cycle
            mobsByPlayer.put(playerUuid, new ArrayList<>());
            return;
        }
        
        // This is a non-player entity
        // Check if it has Health stat (is a mob, not an item)
        EntityStatMap stats = archetypeChunk.getComponent(index, EntityStatMap.getComponentType());
        if (stats == null) {
            return;
        }
        
        EntityStatValue healthStat = stats.get(healthStatIndex);
        if (healthStat == null || healthStat.getMax() <= 0) {
            // Entity has no health = not a mob (probably an item or other non-mob entity)
            return;
        }
        
        // Get entity name from model
        String entityName = getEntityName(store, entityRef);
        
        // Check which players have this entity in their 3x3 chunk area
        for (Map.Entry<UUID, Set<Long>> playerEntry : playerChunkAreas.entrySet()) {
            UUID playerUuid = playerEntry.getKey();
            Set<Long> chunkArea = playerEntry.getValue();
            
            if (chunkArea.contains(entityChunkIndex)) {
                // Entity is in player's 3x3 chunk area - calculate distances
                Vector3d playerPos = playerPositions.get(playerUuid);
                if (playerPos != null) {
                    int dx = (int) Math.round(entityPos.x - playerPos.x);
                    int dy = (int) Math.round(entityPos.y - playerPos.y);
                    int dz = (int) Math.round(entityPos.z - playerPos.z);
                    int euclidean = (int) Math.round(Math.sqrt(dx*dx + dy*dy + dz*dz));
                    
                    MobInfo mobInfo = new MobInfo(entityName, dx, dy, dz, euclidean);
                    mobsByPlayer.computeIfAbsent(playerUuid, k -> new ArrayList<>()).add(mobInfo);
                }
            }
        }
    }
    
    private String getEntityName(Store<EntityStore> store, Ref<EntityStore> entityRef) {
        // Try to get entity name from ModelComponent
        ModelComponent modelComponent = (ModelComponent) store.getComponent(entityRef, ModelComponent.getComponentType());
        if (modelComponent != null && modelComponent.getModel() != null) {
            // Get the ModelReference and use its string representation
            Object modelRef = modelComponent.getModel().toReference();
            if (modelRef != null) {
                String refString = modelRef.toString();
                // The toString usually contains the asset key like "ModelReference{key=Trork}"
                // Try to extract just the name part
                if (refString.contains("=")) {
                    int eqIndex = refString.indexOf('=');
                    int endIndex = refString.indexOf('}', eqIndex);
                    if (endIndex < 0) endIndex = refString.length();
                    String modelName = refString.substring(eqIndex + 1, endIndex);
                    // Clean up path if present
                    int lastSlash = modelName.lastIndexOf('/');
                    if (lastSlash >= 0 && lastSlash < modelName.length() - 1) {
                        modelName = modelName.substring(lastSlash + 1);
                    }
                    return modelName;
                }
                return refString;
            }
        }
        return "Mob";
    }
    
    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return this.query;
    }
    
    /**
     * Get the list of mobs near a specific player.
     * @param playerUuid The player's UUID
     * @return List of MobInfo, or empty list if no data
     */
    public static List<MobInfo> getMobsForPlayer(UUID playerUuid) {
        return mobsByPlayer.getOrDefault(playerUuid, new ArrayList<>());
    }
}
