package me.zircta.accuratecps.mixin;

import me.zircta.accuratecps.utils.CPSHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {
    @Unique private final ArrayList<Long> accurateCPS$leftClickTimes = new ArrayList<>();
    @Unique private final ArrayList<Long> accurateCPS$rightClickTimes = new ArrayList<>();

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void countClick(CallbackInfo ci) {
        CPSHandler.INSTANCE.leftCps += 1;
        accurateCPS$leftClickTimes.add(System.currentTimeMillis());
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    public void countRightClick(CallbackInfo ci) {
        CPSHandler.INSTANCE.rightCps += 1;
        accurateCPS$rightClickTimes.add(System.currentTimeMillis());
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void handleCPS(CallbackInfo ci) {
        accurateCPS$handleCps(accurateCPS$leftClickTimes, 1);
        accurateCPS$handleCps(accurateCPS$rightClickTimes, 2);
    }

    @Unique
    private void accurateCPS$handleCps(ArrayList<Long> clickTimes, int button) {
        long currentTime = System.currentTimeMillis();
        Iterator<Long> iterator = clickTimes.iterator();

        while (iterator.hasNext()) {
            long clickTime = iterator.next();
            if (currentTime - clickTime > 1000) {
                if (button == 1) {
                    CPSHandler.INSTANCE.leftCps--;
                } else {
                    CPSHandler.INSTANCE.rightCps--;
                }
                iterator.remove();
            }
        }
    }
}
