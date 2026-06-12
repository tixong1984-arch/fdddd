package org.codemc.worldguardwrapper.shaded.javassist.compiler;

import org.codemc.worldguardwrapper.shaded.javassist.compiler.CompileError;
import org.codemc.worldguardwrapper.shaded.javassist.compiler.Lex;

public class SyntaxError
extends CompileError {
    private static final long serialVersionUID = 1L;

    public SyntaxError(Lex lexer) {
        super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
    }
}
