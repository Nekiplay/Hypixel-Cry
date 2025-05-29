package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;


public class Dark_Monolith {
    private BlockPos egg = null;
    private boolean found = false;

    // Массив всех возможных позиций для проверки
    private static final BlockPos[] MONOLITH_POSITIONS = {
            new BlockPos(-15, 236, -92),
            new BlockPos(49, 202, -162),
            new BlockPos(56, 214, -25),
            new BlockPos(128, 187, 58),
            new BlockPos(150, 196, 190),
            new BlockPos(61, 204, 181),
            new BlockPos(91, 187, 131),
            new BlockPos(77, 160, 162),
            new BlockPos(-10, 162, 109),
            new BlockPos(1, 183, 25),
            new BlockPos(0, 170, 0),
            new BlockPos(-94, 201, -30),
            new BlockPos(-91, 221, -53),
            new BlockPos(-64, 206, -63)
    };

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START || Main.mc.theWorld == null || !Main.config.esp.dwardenMines.darkMonolith.enabled) {
            return;
        }

        // Проверяем все позиции до нахождения яйца
        BlockPos foundEgg = null;
        for (BlockPos pos : MONOLITH_POSITIONS) {
            BlockPos currentEgg = findEgg(pos);
            if (currentEgg != null) {
                foundEgg = currentEgg;
                break;
            }
        }

        // Обновляем состояние
        if (foundEgg == null && found) {
            found = false;
        } else if (foundEgg != null && !found) {
            found = true;
        }
        egg = foundEgg;
    }

    private BlockPos findEgg(BlockPos start) {
        Vec3i vec3i = new Vec3i(5, 5, 5);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(start.add(vec3i), start.subtract(vec3i));
        for (BlockPos block : blocks) {
            IBlockState state = Main.mc.theWorld.getBlockState(block);
            if (state != null && state.getBlock() == Blocks.dragon_egg) {
                return block;
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.config.esp.dwardenMines.darkMonolith.enabled && egg !=null)
        {
            if (Main.config.esp.dwardenMines.darkMonolith.features.contains(ESPFeatures.Box)) {
                RenderUtils.drawBlockBox(egg, toSpecialColor(Main.config.esp.dwardenMines.darkMonolith.colour), 1, event.partialTicks);
            }
            if (Main.config.esp.dwardenMines.darkMonolith.features.contains(ESPFeatures.Text)) {
                RenderUtils.renderWaypointText("Dark Monolith", new BlockPos(egg.getX() + 0.5, egg.getY() + 1.8, egg.getZ() + 0.5), event.partialTicks, false, toSpecialColor(Main.config.esp.dwardenMines.darkMonolith.colour));
            }
            if (Main.config.esp.dwardenMines.darkMonolith.features.contains(ESPFeatures.Tracer)) {
                RenderUtils.drawTracer(egg, toSpecialColor(Main.config.esp.dwardenMines.darkMonolith.colour), 1, event.partialTicks);
            }
        }
    }
}
