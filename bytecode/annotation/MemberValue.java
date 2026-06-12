package org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import org.codemc.worldguardwrapper.shaded.javassist.ClassPool;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.Descriptor;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.MemberValueVisitor;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.NoSuchClassError;

public abstract class MemberValue {
    ConstPool cp;
    char tag;

    MemberValue(char tag, ConstPool cp) {
        this.cp = cp;
        this.tag = tag;
    }

    abstract Object getValue(ClassLoader var1, ClassPool var2, Method var3) throws ClassNotFoundException;

    abstract Class<?> getType(ClassLoader var1) throws ClassNotFoundException;

    static Class<?> loadClass(ClassLoader cl, String classname) throws ClassNotFoundException, NoSuchClassError {
        try {
            return Class.forName(MemberValue.convertFromArray(classname), true, cl);
        }
        catch (LinkageError e) {
            throw new NoSuchClassError(classname, e);
        }
    }

    private static String convertFromArray(String classname) {
        int index = classname.indexOf("[]");
        if (index != -1) {
            String rawType = classname.substring(0, index);
            StringBuilder sb = new StringBuilder(Descriptor.of(rawType));
            while (index != -1) {
                sb.insert(0, '[');
                index = classname.indexOf("[]", index + 1);
            }
            return sb.toString().replace('/', '.');
        }
        return classname;
    }

    public void renameClass(String oldname, String newname) {
    }

    public void renameClass(Map<String, String> classnames) {
    }

    public abstract void accept(MemberValueVisitor var1);

    public abstract void write(AnnotationsWriter var1) throws IOException;
}
