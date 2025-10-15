package io.github.techtastic.computerized_vehicles.mixin;

import minecrafttransportsimulator.jsondefs.JSONPart;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(JSONPart.InteractableComponentType.class)
public class MixinInteractableComponentType {
    @Shadow
    @Final
    private static JSONPart.InteractableComponentType[] $VALUES;

    @Invoker(value="<init>") // You may need to be more specific if there's more than one constructor
    private static JSONPart.InteractableComponentType create(String name, int ordinal) {
        throw new IllegalStateException("Unreachable");
    }

    static {
        var entry = create("COMPUTER", $VALUES.length); // subsequent additions would be length+1, +2, ...

        //noinspection ShadowFinalModification
        $VALUES = ArrayUtils.add($VALUES, entry); // use addAll if you have more than one entry to be added, and make sure they are precisely in the order you defined the ordinals, so length before length+1, before length+2, ...
    }
}
