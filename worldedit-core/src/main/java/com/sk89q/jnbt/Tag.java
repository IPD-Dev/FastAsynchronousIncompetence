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

package com.sk89q.jnbt;

import com.sk89q.worldedit.util.nbt.BinaryTagLike;

/**
 * Represents a NBT tag.
 *
 * @deprecated JNBT is being removed for adventure-nbt in WorldEdit 8.
 */
@Deprecated(forRemoval = true)
public abstract class Tag implements BinaryTagLike {

    /**
     * Gets the value of this tag.
     *
     * @return the value
     */
    public abstract Object getValue();

    @Override
    public String toString() {
        return asBinaryTag().toString();
    }

    //FAWE start
    public Object toRaw() {
        return getValue();
    }

    public abstract int getTypeCode();
    //FAWE end

}
