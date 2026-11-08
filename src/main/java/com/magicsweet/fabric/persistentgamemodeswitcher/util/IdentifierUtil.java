package com.magicsweet.fabric.persistentgamemodeswitcher.util;

//? if >= 1.21.11 {
import net.minecraft.resources.Identifier;
import static net.minecraft.resources.Identifier.parse;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
import static net.minecraft.resources.ResourceLocation.parse;
*///?}

public class IdentifierUtil {
    public static
    //? if >= 1.21.11 {
    Identifier
    //?} else {
    /*ResourceLocation
    *///?}
    parseIdentifier(String identifier) {
        return parse(identifier);
    }
}
