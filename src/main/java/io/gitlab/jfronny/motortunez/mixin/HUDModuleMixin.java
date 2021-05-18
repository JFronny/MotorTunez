package io.gitlab.jfronny.motortunez.mixin;

import io.gitlab.jfronny.motortunez.hud.TunezHud;
import minegame159.meteorclient.systems.modules.render.hud.HUD;
import minegame159.meteorclient.systems.modules.render.hud.HudElementLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HUD.class)
public abstract class HUDModuleMixin {
    @Shadow protected abstract void align();

    @Shadow @Final private HudElementLayer topRight;

    @Inject(at = @At("TAIL"), method = "<init>()V")
    void init(CallbackInfo info) {
        topRight.add(new TunezHud((HUD)(Object)this));
        align();
    }
}
