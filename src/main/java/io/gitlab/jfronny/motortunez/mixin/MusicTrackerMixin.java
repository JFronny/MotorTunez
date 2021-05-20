package io.gitlab.jfronny.motortunez.mixin;

import io.gitlab.jfronny.motortunez.MotorTunez;
import net.minecraft.client.sound.MusicTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    public void tick(CallbackInfo info) {
        if (MotorTunez.trackScheduler.isPlaying())
            info.cancel();
    }
}
