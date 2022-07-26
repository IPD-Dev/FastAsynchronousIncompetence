package com.fastasyncworldedit.core.regions;

import com.fastasyncworldedit.core.extent.processor.ProcessorScope;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;

public class FaweMask implements IDelegateRegion {

    private final Region region;

    public FaweMask(Region region) {
        this.region = region;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    public boolean isValid(Player player, FaweMaskManager.MaskType type) {
        return false;
    }

    @Override
    public Region clone() {
        throw new UnsupportedOperationException("Clone not supported");
    }

    @Override
    public ProcessorScope getScope() {
        return ProcessorScope.REMOVING_BLOCKS;
    }

}
