package org.codemc.worldguardwrapper.shaded.javassist.compiler.ast;

import org.codemc.worldguardwrapper.shaded.javassist.compiler.CompileError;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.ASTList;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.ASTree;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.CastExpr;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.Visitor;

public class InstanceOfExpr
extends CastExpr {
    private static final long serialVersionUID = 1L;

    public InstanceOfExpr(ASTList className, int dim, ASTree expr) {
        super(className, dim, expr);
    }

    public InstanceOfExpr(int type, int dim, ASTree expr) {
        super(type, dim, expr);
    }

    @Override
    public String getTag() {
        return "instanceof:" + this.castType + ":" + this.arrayDim;
    }

    @Override
    public void accept(Visitor v) throws CompileError {
        v.atInstanceOfExpr(this);
    }
}
