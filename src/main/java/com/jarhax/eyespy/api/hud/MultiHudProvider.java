/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.buuz135.mhud.MultipleHUD
 *  com.hypixel.hytale.component.ArchetypeChunk
 *  com.hypixel.hytale.component.CommandBuffer
 *  com.hypixel.hytale.component.Holder
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.server.core.entity.EntityUtils
 *  com.hypixel.hytale.server.core.entity.entities.Player
 *  com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
 *  com.hypixel.hytale.server.core.universe.PlayerRef
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 *  org.checkerframework.checker.nullness.compatqual.NonNullDecl
 */
package com.jarhax.eyespy.api.hud;

// import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.hud.HudProvider;
import com.jarhax.eyespy.impl.hud.EyeSpyHud;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class MultiHudProvider
implements HudProvider {
    private final Map<PlayerRef, EyeSpyHud> huds = new HashMap<PlayerRef, EyeSpyHud>();

    @Override
    public void showHud(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        Holder holder = EntityUtils.toHolder((int)index, archetypeChunk);
        Player player = (Player)holder.getComponent(Player.getComponentType());
        PlayerRef playerRef = (PlayerRef)holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        if (!this.huds.containsKey(playerRef)) {
            EyeSpyHud value = new EyeSpyHud(playerRef);
            this.huds.put(playerRef, value);
            value.updateHud(dt, index, archetypeChunk, store, commandBuffer);
            // MultipleHUD.getInstance().setCustomHud(player, playerRef, "EyeSpy_HUD", (CustomUIHud)value);
        } else {
            EyeSpyHud customUIHud = this.huds.get(playerRef);
            customUIHud.updateHud(dt, index, archetypeChunk, store, commandBuffer);
            // MultipleHUD.getInstance().setCustomHud(player, playerRef, "EyeSpy_HUD", (CustomUIHud)customUIHud);
        }
    }
}

