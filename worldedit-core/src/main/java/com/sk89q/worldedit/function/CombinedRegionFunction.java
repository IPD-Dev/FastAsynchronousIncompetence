/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.function;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.math.BlockVector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Executes several region functions in order.
 */
public class CombinedRegionFunction implements RegionFunction {

    //FAWE start - don't use a List<RF> here
    private RegionFunction[] functions;
    //FAWE end

    /**
     * Create a combined region function.
     */
    public CombinedRegionFunction() {
    }

    /**
     * Create a combined region function.
     *
     * @param functions a list of functions to match
     */
    public CombinedRegionFunction(Collection<RegionFunction> functions) {
        checkNotNull(functions);
        this.functions = functions.toArray(new RegionFunction[0]);
    }

    /**
     * Create a combined region function.
     *
     * @param function an array of functions to match
     */
    public CombinedRegionFunction(RegionFunction... function) {
        this.functions = function;
    }

    //FAWE start
    public static CombinedRegionFunction combine(RegionFunction function, RegionFunction add) {
        CombinedRegionFunction combined;
        if (function instanceof CombinedRegionFunction) {
            combined = ((CombinedRegionFunction) function);
            combined.add(add);
        } else if (add instanceof CombinedRegionFunction) {
            combined = new CombinedRegionFunction(function);
            combined.add(((CombinedRegionFunction) add).functions);
        } else {
            combined = new CombinedRegionFunction(function, add);
        }
        return combined;
    }
    //FAWE end

    /**
     * Add the given functions to the list of functions to call.
     *
     * @param functions a list of functions
     */
    public void add(Collection<RegionFunction> functions) {
        checkNotNull(functions);
        //FAWE start - use our logic
        ArrayList<RegionFunction> functionsList = new ArrayList<>(Arrays.asList(this.functions));
        functionsList.addAll(functions);
        this.functions = functionsList.toArray(new RegionFunction[0]);
        //FAWE end
    }

    /**
     * Add the given functions to the list of functions to call.
     *
     * @param function an array of functions
     */
    public void add(RegionFunction... function) {
        add(Arrays.asList(checkNotNull(function)));
    }

    @Override
    public boolean apply(BlockVector3 position) throws WorldEditException {
        boolean ret = false;
        for (RegionFunction function : functions) {
            ret |= (function.apply(position));
        }
        return ret;
    }

}
