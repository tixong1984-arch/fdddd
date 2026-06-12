package org.codemc.worldguardwrapper.selection;

import java.util.Set;
import org.bukkit.Location;
import org.codemc.worldguardwrapper.selection.ISelection;

public interface IPolygonalSelection
extends ISelection {
    public Set<Location> getPoints();

    public int getMinimumY();

    public int getMaximumY();
}
