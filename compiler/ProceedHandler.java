package org.codemc.worldguardwrapper.shaded.javassist.compiler;

import org.codemc.worldguardwrapper.shaded.javassist.bytecode.Bytecode;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.CompileError;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.JvstCodeGen;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.JvstTypeChecker;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.ast.ASTList;

public interface ProceedHandler {
    public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError;

    public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError;
}
