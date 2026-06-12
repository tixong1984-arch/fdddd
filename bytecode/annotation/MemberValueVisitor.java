package org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation;

import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.AnnotationMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.ArrayMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.BooleanMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.ByteMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.CharMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.ClassMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.DoubleMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.EnumMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.FloatMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.IntegerMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.LongMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.ShortMemberValue;
import org.codemc.worldguardwrapper.shaded.javassist.bytecode.annotation.StringMemberValue;

public interface MemberValueVisitor {
    public void visitAnnotationMemberValue(AnnotationMemberValue var1);

    public void visitArrayMemberValue(ArrayMemberValue var1);

    public void visitBooleanMemberValue(BooleanMemberValue var1);

    public void visitByteMemberValue(ByteMemberValue var1);

    public void visitCharMemberValue(CharMemberValue var1);

    public void visitDoubleMemberValue(DoubleMemberValue var1);

    public void visitEnumMemberValue(EnumMemberValue var1);

    public void visitFloatMemberValue(FloatMemberValue var1);

    public void visitIntegerMemberValue(IntegerMemberValue var1);

    public void visitLongMemberValue(LongMemberValue var1);

    public void visitShortMemberValue(ShortMemberValue var1);

    public void visitStringMemberValue(StringMemberValue var1);

    public void visitClassMemberValue(ClassMemberValue var1);
}
