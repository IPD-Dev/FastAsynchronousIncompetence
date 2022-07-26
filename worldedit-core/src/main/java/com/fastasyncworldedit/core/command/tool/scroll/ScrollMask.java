package com.fastasyncworldedit.core.command.tool.scroll;

import com.fastasyncworldedit.core.util.MathMan;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.mask.Mask;

public class ScrollMask extends Scroll {

    private final Mask[] masks;
    private int index;

    public ScrollMask(BrushTool tool, Mask... masks) {
        super(tool);
        this.masks = masks;
    }


    @Override
    public boolean increment(Player player, int amount) {
        if (masks.length > 1) {
            getTool().setMask(masks[MathMan.wrap(index += amount, 0, masks.length - 1)]);
            return true;
        }
        return false;
    }

}
