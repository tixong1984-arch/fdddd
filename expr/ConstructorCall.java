package org.codemc.worldguardwrapper.shaded.javassist.expr;

import org.codemc.worldguardwrapper.shaded.javassist.CtClass;
import org.codemc.worldguardwrapper.shaded.javassist.CtConstructor;
import org.codemc.worldguardwrapper.shaded.javassist.CtMethod;
import org.codemc.worldguardwrapper.shaded.javassist.NotFoundException;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.CodeIterator;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.MethodInfo;
import org.codemc.worldguardwrapper.shaded.javassist.expr.MethodCall;

public class ConstructorCall
extends MethodCall {
    protected ConstructorCall(int pos, CodeIterator i, CtClass decl, MethodInfo m) {
        super(pos, i, decl, m);
    }

    @Override
    public String getMethodName() {
        return this.isSuper() ? "super" : "this";
    }

    @Override
    public CtMethod getMethod() throws NotFoundException {
        throw new NotFoundException("this is a constructor call.  Call getConstructor().");
    }

    public CtConstructor getConstructor() throws NotFoundException {
        return this.getCtClass().getConstructor(this.getSignature());
    }

    @Override
    public boolean isSuper() {
        return super.isSuper();
    }
}
