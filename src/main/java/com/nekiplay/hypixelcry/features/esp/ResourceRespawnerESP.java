package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class ResourceRespawnerESP {
    private int oneWood = 0;
    private int twoWood = 0;
    private int threeWood = 0;
    private int chitiryWood = 0;
    private int fiveWood = 0;
    private int sixWood = 0;
    private int sevenWood = 0;
    private int vosemWood = 0;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        oneWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-150, 73, -20));
            add(new BlockPos(-150, 73, -19));
            add(new BlockPos(-150, 73, -18));
            add(new BlockPos(-150, 73, -17));
            add(new BlockPos(-150, 73, -16));
            add(new BlockPos(-150, 73, -15));
            add(new BlockPos(-150, 73, -14));
            add(new BlockPos(-150, 73, -13));

            add(new BlockPos(-151, 73, -19));
            add(new BlockPos(-151, 73, -18));
            add(new BlockPos(-151, 73, -17));
            add(new BlockPos(-151, 73, -16));
            add(new BlockPos(-151, 73, -15));
            add(new BlockPos(-151, 73, -14));
            add(new BlockPos(-151, 73, -13));

            add(new BlockPos(-151, 74, -18));
            add(new BlockPos(-151, 74, -17));
            add(new BlockPos(-151, 74, -16));
            add(new BlockPos(-151, 74, -15));
            add(new BlockPos(-151, 74, -14));
            add(new BlockPos(-151, 74, -13));
            add(new BlockPos(-151, 74, -12));

            add(new BlockPos(-152, 73, -18));
            add(new BlockPos(-152, 73, -17));
            add(new BlockPos(-152, 73, -16));
            add(new BlockPos(-152, 73, -15));
            add(new BlockPos(-152, 73, -14));
            add(new BlockPos(-152, 73, -13));
            add(new BlockPos(-152, 73, -12));
        }});
        twoWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-142, 73, -20));
            add(new BlockPos(-142, 73, -19));
            add(new BlockPos(-142, 73, -18));
            add(new BlockPos(-142, 73, -17));
            add(new BlockPos(-142, 73, -16));
            add(new BlockPos(-142, 73, -15));
            add(new BlockPos(-142, 73, -14));

            add(new BlockPos(-143, 73, -18));
            add(new BlockPos(-143, 73, -17));
            add(new BlockPos(-143, 73, -16));
            add(new BlockPos(-143, 73, -15));
            add(new BlockPos(-143, 73, -14));

            add(new BlockPos(-143, 74, -18));
            add(new BlockPos(-143, 74, -17));
            add(new BlockPos(-143, 74, -16));
            add(new BlockPos(-143, 74, -15));
            add(new BlockPos(-143, 74, -14));

            add(new BlockPos(-144, 73, -19));
            add(new BlockPos(-144, 73, -18));
            add(new BlockPos(-144, 73, -17));
            add(new BlockPos(-144, 73, -16));
            add(new BlockPos(-144, 73, -15));
            add(new BlockPos(-144, 73, -14));
            add(new BlockPos(-144, 73, -13));
        }});
        threeWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-131, 73, -24));
            add(new BlockPos(-131, 73, -23));
            add(new BlockPos(-131, 73, -22));
            add(new BlockPos(-131, 73, -21));
            add(new BlockPos(-131, 73, -20));

            add(new BlockPos(-132, 73, -23));
            add(new BlockPos(-132, 73, -22));
            add(new BlockPos(-132, 73, -21));
            add(new BlockPos(-132, 73, -20));
            add(new BlockPos(-132, 73, -19));

            add(new BlockPos(-132, 74, -25));
            add(new BlockPos(-132, 74, -24));
            add(new BlockPos(-132, 74, -23));
            add(new BlockPos(-132, 74, -22));
            add(new BlockPos(-132, 74, -21));
            add(new BlockPos(-132, 74, -20));

            add(new BlockPos(-133, 73, -24));
            add(new BlockPos(-133, 73, -23));
            add(new BlockPos(-133, 73, -22));
            add(new BlockPos(-133, 73, -21));
            add(new BlockPos(-133, 73, -20));
        }});

        chitiryWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-134, 73, -42));
            add(new BlockPos(-134, 73, -43));
            add(new BlockPos(-134, 73, -44));
            add(new BlockPos(-134, 73, -45));
            add(new BlockPos(-134, 73, -46));
            add(new BlockPos(-134, 73, -47));
            add(new BlockPos(-134, 73, -48));

            add(new BlockPos(-133, 73, -43));
            add(new BlockPos(-133, 73, -44));
            add(new BlockPos(-133, 73, -45));
            add(new BlockPos(-133, 73, -46));
            add(new BlockPos(-133, 73, -47));
            add(new BlockPos(-133, 73, -48));
            add(new BlockPos(-133, 73, -49));

            add(new BlockPos(-133, 74, -42));
            add(new BlockPos(-133, 74, -43));
            add(new BlockPos(-133, 74, -44));
            add(new BlockPos(-133, 74, -45));
            add(new BlockPos(-133, 74, -46));
            add(new BlockPos(-133, 74, -47));
            add(new BlockPos(-133, 74, -48));

            add(new BlockPos(-132, 73, -43));
            add(new BlockPos(-132, 73, -44));
            add(new BlockPos(-132, 73, -45));
            add(new BlockPos(-132, 73, -46));
            add(new BlockPos(-132, 73, -47));
            add(new BlockPos(-132, 73, -48));
        }});

        fiveWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-119, 74, -29));
            add(new BlockPos(-118, 74, -29));
            add(new BlockPos(-117, 74, -29));
            add(new BlockPos(-116, 74, -29));
            add(new BlockPos(-115, 74, -29));
            add(new BlockPos(-114, 74, -29));

            add(new BlockPos(-121, 74, -28));
            add(new BlockPos(-120, 74, -28));
            add(new BlockPos(-119, 74, -28));
            add(new BlockPos(-118, 74, -28));
            add(new BlockPos(-117, 74, -28));
            add(new BlockPos(-116, 74, -28));
            add(new BlockPos(-115, 74, -28));
            add(new BlockPos(-114, 74, -28));
            add(new BlockPos(-113, 74, -28));

            add(new BlockPos(-120, 75, -28));
            add(new BlockPos(-119, 75, -28));
            add(new BlockPos(-118, 75, -28));
            add(new BlockPos(-117, 75, -28));
            add(new BlockPos(-116, 75, -28));
            add(new BlockPos(-115, 75, -28));
            add(new BlockPos(-114, 75, -28));

            add(new BlockPos(-119, 74, -27));
            add(new BlockPos(-118, 74, -27));
            add(new BlockPos(-117, 74, -27));
            add(new BlockPos(-116, 74, -27));
            add(new BlockPos(-115, 74, -27));
            add(new BlockPos(-114, 74, -27));
        }});

        sixWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-122, 74, -37));
            add(new BlockPos(-121, 74, -37));
            add(new BlockPos(-120, 74, -37));
            add(new BlockPos(-119, 74, -37));

            add(new BlockPos(-121, 74, -36));
            add(new BlockPos(-120, 74, -36));
            add(new BlockPos(-119, 74, -36));
            add(new BlockPos(-118, 74, -36));
            add(new BlockPos(-117, 74, -36));
            add(new BlockPos(-116, 74, -36));

            add(new BlockPos(-123, 75, -36));
            add(new BlockPos(-122, 75, -36));
            add(new BlockPos(-121, 75, -36));
            add(new BlockPos(-120, 75, -36));
            add(new BlockPos(-119, 75, -36));
            add(new BlockPos(-118, 75, -36));
            add(new BlockPos(-117, 75, -36));

            add(new BlockPos(-120, 74, -35));
            add(new BlockPos(-119, 74, -35));
            add(new BlockPos(-118, 74, -35));
            add(new BlockPos(-117, 74, -35));
        }});

        sevenWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-151, 74, -43));
            add(new BlockPos(-150, 74, -43));
            add(new BlockPos(-149, 74, -43));
            add(new BlockPos(-148, 74, -43));
            add(new BlockPos(-147, 74, -43));
            add(new BlockPos(-146, 74, -43));
            add(new BlockPos(-145, 74, -43));
            add(new BlockPos(-144, 74, -43));
            add(new BlockPos(-143, 74, -43));
            add(new BlockPos(-142, 74, -43));

            add(new BlockPos(-152, 74, -42));
            add(new BlockPos(-151, 74, -42));
            add(new BlockPos(-150, 74, -42));
            add(new BlockPos(-149, 74, -42));
            add(new BlockPos(-148, 74, -42));
            add(new BlockPos(-147, 74, -42));
            add(new BlockPos(-146, 74, -42));
            add(new BlockPos(-145, 74, -42));
            add(new BlockPos(-144, 74, -42));
            add(new BlockPos(-143, 74, -42));
            add(new BlockPos(-142, 74, -42));
            add(new BlockPos(-141, 74, -42));

            add(new BlockPos(-151, 75, -42));
            add(new BlockPos(-150, 75, -42));
            add(new BlockPos(-149, 75, -42));
            add(new BlockPos(-148, 75, -42));
            add(new BlockPos(-147, 75, -42));
            add(new BlockPos(-146, 75, -42));
            add(new BlockPos(-145, 75, -42));
            add(new BlockPos(-144, 75, -42));
            add(new BlockPos(-143, 75, -42));
            add(new BlockPos(-142, 75, -42));

            add(new BlockPos(-151, 74, -41));
            add(new BlockPos(-150, 74, -41));
            add(new BlockPos(-149, 74, -41));
            add(new BlockPos(-148, 74, -41));
            add(new BlockPos(-147, 74, -41));
            add(new BlockPos(-146, 74, -41));
            add(new BlockPos(-145, 74, -41));
            add(new BlockPos(-144, 74, -41));
            add(new BlockPos(-143, 74, -41));
            add(new BlockPos(-142, 74, -41));
        }});

        vosemWood = GetWoodCount(new ArrayList<BlockPos>(){{
            add(new BlockPos(-152, 77, -43));
            add(new BlockPos(-151, 77, -43));
            add(new BlockPos(-150, 77, -43));
            add(new BlockPos(-149, 77, -43));
            add(new BlockPos(-148, 77, -43));
            add(new BlockPos(-147, 77, -43));
            add(new BlockPos(-146, 77, -43));
            add(new BlockPos(-145, 77, -43));
            add(new BlockPos(-144, 77, -43));
            add(new BlockPos(-143, 77, -43));
            add(new BlockPos(-142, 77, -43));
            add(new BlockPos(-141, 77, -43));

            add(new BlockPos(-153, 77, -42));
            add(new BlockPos(-152, 77, -42));
            add(new BlockPos(-151, 77, -42));
            add(new BlockPos(-150, 77, -42));
            add(new BlockPos(-149, 77, -42));
            add(new BlockPos(-148, 77, -42));
            add(new BlockPos(-147, 77, -42));
            add(new BlockPos(-146, 77, -42));
            add(new BlockPos(-145, 77, -42));
            add(new BlockPos(-144, 77, -42));
            add(new BlockPos(-143, 77, -42));
            add(new BlockPos(-142, 77, -42));
            add(new BlockPos(-141, 77, -42));
            add(new BlockPos(-140, 77, -42));

            add(new BlockPos(-152, 78, -42));
            add(new BlockPos(-151, 78, -42));
            add(new BlockPos(-150, 78, -42));
            add(new BlockPos(-149, 78, -42));
            add(new BlockPos(-148, 78, -42));
            add(new BlockPos(-147, 78, -42));
            add(new BlockPos(-146, 78, -42));
            add(new BlockPos(-145, 78, -42));
            add(new BlockPos(-144, 78, -42));
            add(new BlockPos(-143, 78, -42));
            add(new BlockPos(-142, 78, -42));
            add(new BlockPos(-141, 78, -42));

            add(new BlockPos(-153, 77, -41));
            add(new BlockPos(-152, 77, -41));
            add(new BlockPos(-151, 77, -41));
            add(new BlockPos(-150, 77, -41));
            add(new BlockPos(-149, 77, -41));
            add(new BlockPos(-148, 77, -41));
            add(new BlockPos(-147, 77, -41));
            add(new BlockPos(-146, 77, -41));
            add(new BlockPos(-145, 77, -41));
            add(new BlockPos(-144, 77, -41));
            add(new BlockPos(-143, 77, -41));
            add(new BlockPos(-142, 77, -41));
            add(new BlockPos(-141, 77, -41));
        }});
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {

        if (myConfigFile != null && myConfigFile.resourceRespawnerMainPage.EnableESP) {

            Color colorOne = Color.RED;
            if (oneWood == 29) {
                colorOne = Color.GREEN;
            }
            else if (oneWood >= 29 / 2) {
                colorOne = Color.ORANGE;
            }
            if (oneWood != 0) {
                RenderUtils.renderWaypointText(oneWood + "/29", new BlockPos(-150.5, 75.7, -15.5), event.partialTicks, false, colorOne);
            }

            Color colorTwo = Color.RED;
            if (twoWood == 24) {
                colorTwo = Color.GREEN;
            }
            else if (twoWood >= 24 / 2) {
                colorTwo = Color.ORANGE;
            }
            if (twoWood != 0) {
                RenderUtils.renderWaypointText(twoWood + "/24", new BlockPos(-142.5, 75.7, -15.5), event.partialTicks, false, colorTwo);
            }

            Color colorThree = Color.RED;
            if (threeWood == 21) {
                colorThree = Color.GREEN;
            }
            else if (threeWood >= 21 / 2) {
                colorThree = Color.ORANGE;
            }
            if (threeWood != 0) {
                RenderUtils.renderWaypointText(threeWood + "/21", new BlockPos(-132.5, 75.7, -21.5), event.partialTicks, false, colorThree);
            }

            Color colorChitiry = Color.RED;
            if (chitiryWood == 27) {
                colorChitiry = Color.GREEN;
            }
            else if (chitiryWood >= 27 / 2) {
                colorChitiry = Color.ORANGE;
            }
            if (chitiryWood != 0) {
                RenderUtils.renderWaypointText(chitiryWood + "/27", new BlockPos(-132.5, 75.7, -44.5), event.partialTicks, false, colorChitiry);
            }

            Color colorFive = Color.RED;
            if (fiveWood == 28) {
                colorFive = Color.GREEN;
            }
            else if (fiveWood >= 28 / 2) {
                colorFive = Color.ORANGE;
            }
            if (fiveWood != 0) {
                RenderUtils.renderWaypointText(fiveWood + "/28", new BlockPos(-116.5, 76.7, -27.5), event.partialTicks, false, colorFive);
            }

            Color colorSix = Color.RED;
            if (sixWood == 21) {
                colorSix = Color.GREEN;
            }
            else if (sixWood >= 21 / 2) {
                colorSix = Color.ORANGE;
            }
            if (sixWood != 0) {
                RenderUtils.renderWaypointText(sixWood + "/21", new BlockPos(-119.5, 76.7, -35.5), event.partialTicks, false, colorSix);
            }

            Color colorSeven = Color.RED;
            if (sevenWood == 42) {
                colorSeven = Color.GREEN;
            }
            else if (sevenWood >= 42 / 2) {
                colorSeven = Color.ORANGE;
            }
            if (sevenWood != 0) {
                RenderUtils.renderWaypointText(sevenWood + "/42", new BlockPos(-146.5, 76.7, -41.5), event.partialTicks, false, colorSeven);
            }

            Color colorVosem = Color.RED;
            if (vosemWood == 51) {
                colorVosem = Color.GREEN;
            }
            else if (vosemWood >= 51 / 2) {
                colorVosem = Color.ORANGE;
            }
            if (vosemWood != 0) {
                RenderUtils.renderWaypointText(vosemWood + "/51", new BlockPos(-146.5, 79.7, -41.5), event.partialTicks, false, colorVosem);
            }
        }

    }

    public boolean IsLog(int x, int y, int z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.log;
    }
    public int GetWoodCount(ArrayList<BlockPos> list) {
        int count = 0;
        if (mc.theWorld == null) {
            return count;
        }

        for (BlockPos pos : list) {
            if (IsLog(pos.getX(), pos.getY(), pos.getZ())) {
                count++;
            }
        }
        return count;
    }

    public int GetWoodThreeCount() {
        int count = 0;
        if (mc.theWorld == null) {
            return count;
        }
        if (IsLog(-131, 73, -24)) {
            count++;
        }
        if (IsLog(-131, 73, -23)) {
            count++;
        }
        if (IsLog(-131, 73, -22)) {
            count++;
        }
        if (IsLog(-131, 73, -21)) {
            count++;
        }
        if (IsLog(-131, 73, -20)) {
            count++;
        }

        if (IsLog(-132, 73, -23)) {
            count++;
        }
        if (IsLog(-132, 73, -22)) {
            count++;
        }
        if (IsLog(-132, 73, -21)) {
            count++;
        }
        if (IsLog(-132, 73, -20)) {
            count++;
        }
        if (IsLog(-132, 73, -19)) {
            count++;
        }

        if (IsLog(-132, 74, -25)) {
            count++;
        }
        if (IsLog(-132, 74, -24)) {
            count++;
        }
        if (IsLog(-132, 74, -23)) {
            count++;
        }
        if (IsLog(-132, 74, -22)) {
            count++;
        }
        if (IsLog(-132, 74, -21)) {
            count++;
        }
        if (IsLog(-132, 74, -20)) {
            count++;
        }

        if (IsLog(-133, 73, -24)) {
            count++;
        }
        if (IsLog(-133, 73, -23)) {
            count++;
        }
        if (IsLog(-133, 73, -22)) {
            count++;
        }
        if (IsLog(-133, 73, -21)) {
            count++;
        }
        if (IsLog(-133, 73, -20)) {
            count++;
        }
        return count;
    }
}
