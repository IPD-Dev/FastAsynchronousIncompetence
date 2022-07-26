package com.fastasyncworldedit.core.function.mask;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;

public class SurfaceMask extends AdjacentAnyMask {

    public SurfaceMask(Extent extent) {
        super(getMask(extent), extent.getMinY(), extent.getMaxY());
    }

    private SurfaceMask(CachedMask mask, int minY, int maxY) {
        super(mask, minY, maxY);
    }

    public static AbstractExtentMask getMask(Extent extent) {
        return new BlockMaskBuilder()
                .addTypes(BlockTypes.AIR, BlockTypes.CAVE_AIR, BlockTypes.VOID_AIR)
                .addAll(b -> !b.getMaterial().isMovementBlocker())
                .build(extent);
    }

    @Override
    public boolean test(BlockVector3 v) {
        return !getParentMask().test(v) && super.test(v);
    }

    @Override
    public Mask copy() {
        // CachedMask (super getParentMask) should not be used from multiple threads.
        return new SurfaceMask(getParentMask(), minY, maxY);
    }

}
