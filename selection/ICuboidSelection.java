package org.codemc.worldguardwrapper.selection;

import org.bukkit.Location;
import org.codemc.worldguardwrapper.selection.ISelection;

public interface ICuboidSelection
extends ISelection {
    public Location getMinimumPoint();

    public Location getMaximumPoint();
}
