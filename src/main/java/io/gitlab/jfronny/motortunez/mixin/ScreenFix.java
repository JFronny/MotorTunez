package io.gitlab.jfronny.motortunez.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;
import java.util.Set;

//Forces the screen to be initialized before currentScreen is set and removes the second initialization
@Mixin(MinecraftClient.class)
public class ScreenFix {
    @Shadow @Final private Window window;
    private static final Set<Screen> motortunez$passingScreens = new HashSet<>();
    @Redirect(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", opcode = Opcodes.PUTFIELD))
    public void preInitScreen(MinecraftClient client, Screen screen) {
        if (screen != null) {
            motortunez$passingScreens.add(screen);
            screen.init((MinecraftClient)(Object)this, window.getScaledWidth(), window.getScaledHeight());
        }
        client.currentScreen = screen;
    }
    
    @Redirect(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;init(Lnet/minecraft/client/MinecraftClient;II)V"))
    private void disableInit(Screen screen, MinecraftClient client, int width, int height) {
        if (motortunez$passingScreens.remove(screen))
            screen.init(client, width, height);
    }

    //TODO this should probably be removed once it is no longer needed because it is quite hacky
}
