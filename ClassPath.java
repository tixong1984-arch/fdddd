package org.codemc.worldguardwrapper.shaded.javassist;

import java.io.InputStream;
import java.net.URL;
import org.codemc.worldguardwrapper.shaded.javassist.NotFoundException;

public interface ClassPath {
    public InputStream openClassfile(String var1) throws NotFoundException;

    public URL find(String var1);
}
