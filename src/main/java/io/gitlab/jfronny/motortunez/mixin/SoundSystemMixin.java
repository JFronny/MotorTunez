package io.gitlab.jfronny.motortunez.mixin;

import io.gitlab.jfronny.motortunez.ISoundManager;
import io.gitlab.jfronny.motortunez.MotorTunez;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin implements ISoundManager {
    @Shadow protected abstract float getSoundVolume(@Nullable SoundCategory soundCategory);

    @Inject(at = @At("TAIL"), method = "updateSoundVolume(Lnet/minecraft/sound/SoundCategory;F)V")
    public void updateSoundVolume(SoundCategory category, float volume, CallbackInfo info) {
        if (category == SoundCategory.MUSIC) {
            MotorTunez.trackScheduler.setVolume(volume);
        }
    }

    @Override
    public float getMusicVolume() {
        return this.getSoundVolume(SoundCategory.MUSIC);
    }
}
