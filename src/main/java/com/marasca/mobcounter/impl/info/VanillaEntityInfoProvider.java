/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.hypixel.hytale.component.Store
 *  com.hypixel.hytale.server.core.Message
 *  com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent
 *  com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
 *  com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue
 *  com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType
 *  com.hypixel.hytale.server.core.universe.world.storage.EntityStore
 */
package com.marasca.mobcounter.impl.info;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.marasca.mobcounter.api.context.EntityContext;
import com.marasca.mobcounter.api.info.InfoBuilder;
import com.marasca.mobcounter.api.info.InfoProvider;
import com.marasca.mobcounter.api.info.values.LabelValue;

public class VanillaEntityInfoProvider
implements InfoProvider<EntityContext> {
    @Override
    public void updateDescription(EntityContext context, InfoBuilder infoBuilder) {
        int statIndex;
        EntityStatValue entityStatValue;
        EntityStatMap stats;
        Store<EntityStore> store = context.getStore();
        DisplayNameComponent displayName = (DisplayNameComponent)store.getComponent(context.entity(), DisplayNameComponent.getComponentType());
        if (displayName != null) {
            infoBuilder.set("Header", s -> new LabelValue((String)s, displayName.getDisplayName(), 24));
        }
        if ((stats = (EntityStatMap)store.getComponent(context.entity(), EntityStatMap.getComponentType())) != null && (entityStatValue = stats.get(statIndex = EntityStatType.getAssetMap().getIndex("Health"))) != null) {
            infoBuilder.set("Health", s -> new LabelValue((String)s, Message.join((Message[])new Message[]{Message.translation((String)"client.itemTooltip.stats.Health"), Message.raw((String)" %s/%s".formatted(Float.valueOf(entityStatValue.get()), Float.valueOf(entityStatValue.getMax())))})).setHeight(54));
        }
    }
}

