package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.cc_vehicles.util.LuaConversions;
import minecrafttransportsimulator.entities.instances.PartGun;
import minecrafttransportsimulator.items.instances.ItemBullet;
import minecrafttransportsimulator.mcinterface.IWrapperItemStack;

public class GunPart extends BasePart<PartGun> {
    protected GunPart(PartGun part) {
        super(part);
    }

    @LuaFunction
    public final void setAmmo(IArguments args) throws LuaException {
        ItemBullet bullet = LuaConversions.getMTSItem(args, 0, ItemBullet.class);
        this.getPart().setReloadVars(bullet, args.optInt(1, 0));
    }

    @LuaFunction
    public final boolean tryToReload(IArguments args) throws LuaException {
        ItemBullet bullet = LuaConversions.getMTSItem(args, 0, ItemBullet.class);
        IWrapperItemStack stack = bullet.getNewStack(null);
        return this.getPart().tryToReload(stack, args.optBoolean(1, false));
    }
}
