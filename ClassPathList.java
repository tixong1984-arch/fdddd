package org.codemc.worldguardwrapper.shaded.javassist;

import org.codemc.worldguardwrapper.shaded.javassist.ClassPath;

final class ClassPathList {
    ClassPathList next;
    ClassPath path;

    ClassPathList(ClassPath p, ClassPathList n) {
        this.next = n;
        this.path = p;
    }
}
