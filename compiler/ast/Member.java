package org.codemc.worldguardwrapper.shaded.javassist.compiler.ast;

import org.codemc.worldguardwrapper.shaded.javassist.CtField;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.CompileError;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.Symbol;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.Visitor;

public class Member
extends Symbol {
    private static final long serialVersionUID = 1L;
    private CtField field = null;

    public Member(String name) {
        super(name);
    }

    public void setField(CtField f) {
        this.field = f;
    }

    public CtField getField() {
        return this.field;
    }

    @Override
    public void accept(Visitor v) throws CompileError {
        v.atMember(this);
    }
}
