package org.codemc.worldguardwrapper.shaded.javassist.util.proxy;

import org.codemc.worldguardwrapper.shaded.javassist.util.proxy.MethodHandler;
import org.codemc.worldguardwrapper.shaded.javassist.util.proxy.Proxy;

public interface ProxyObject
extends Proxy {
    @Override
    public void setHandler(MethodHandler var1);

    public MethodHandler getHandler();
}
