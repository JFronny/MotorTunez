package io.gitlab.jfronny.motortunez.mixin;

import io.gitlab.jfronny.motortunez.ISoundManager;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements ISoundManager {
    @Shadow @Final private SoundSystem soundSystem;

    @Override
    public float getMusicVolume() {
        return ((ISoundManager)(this.soundSystem)).getMusicVolume();
    }
}
