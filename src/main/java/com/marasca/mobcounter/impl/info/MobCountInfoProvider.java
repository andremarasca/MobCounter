package com.marasca.mobcounter.impl.info;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.marasca.mobcounter.api.context.PlayerContext;
import com.marasca.mobcounter.api.info.InfoBuilder;
import com.marasca.mobcounter.api.info.InfoProvider;
import com.marasca.mobcounter.api.info.values.LabelValue;

import java.util.List;
import java.util.UUID;

/**
 * Info provider that displays nearby mobs with their distances.
 */
public class MobCountInfoProvider implements InfoProvider<PlayerContext> {
    
    @Override
    public void updateDescription(PlayerContext context, InfoBuilder infoBuilder) {
        PlayerRef playerRef = context.getObserver();
        if (playerRef == null) {
            return;
        }
        
        UUID playerUuid = playerRef.getUuid();
        List<MobCounterSystem.MobInfo> mobs = MobCounterSystem.getMobsForPlayer(playerUuid);
        
        // Build the display
        infoBuilder.set("Header", s -> new LabelValue(s, Message.raw("Mobs Nearby (3x3 chunks)"), 20));
        
        if (mobs.isEmpty()) {
            infoBuilder.set("NoMobs", s -> new LabelValue(s, Message.raw("No mobs nearby"), 16).setHeight(40));
        } else {
            infoBuilder.set("Total", s -> new LabelValue(s, Message.raw("Total: " + mobs.size()), 18).setHeight(44));
            
            // Show each mob with distances (limit to first 10 to avoid UI overflow)
            int index = 0;
            for (MobCounterSystem.MobInfo mob : mobs) {
                if (index >= 10) break;
                final int lineIndex = index;
                // Format: Name X:dx Y:dy Z:dz (distance)
                String line = String.format("%s X:%d Y:%d Z:%d (%d)", 
                        mob.name, mob.dx, mob.dy, mob.dz, mob.distance);
                infoBuilder.set("Mob" + lineIndex, s -> new LabelValue(s, Message.raw(line), 14).setHeight(32));
                index++;
            }
        }
    }
}
