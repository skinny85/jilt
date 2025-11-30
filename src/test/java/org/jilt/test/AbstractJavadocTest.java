package org.jilt.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.javadoc.Javadoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractJavadocTest {
    protected Optional<String> getSetterJavadoc(String javaFilePath, String setterName) throws FileNotFoundException {
        return this.getSetterJavadoc(javaFilePath, null,  setterName);
    }

    protected Optional<String> getSetterJavadoc(String javaFilePath, String innerTypeName, String setterName)
            throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(new File(
                "build/generated/sources/annotationProcessor/java/test/" + javaFilePath));
        TypeDeclaration<?> typeDeclaration = compilationUnit.getPrimaryType().get();

        List<MethodDeclaration> setters = typeDeclaration.getMethodsByName(setterName);
        if (innerTypeName != null) {
            for (BodyDeclaration<?> member : typeDeclaration.getMembers()) {
                if (member.isTypeDeclaration()) {
                    TypeDeclaration<?> nestedType = member.toTypeDeclaration().get();
                    if (innerTypeName.equals(nestedType.getName().asString())) {
                        setters = nestedType.getMethodsByName(setterName);
                    }
                }
            }
        }

        assertThat(setters).hasSize(1);
        MethodDeclaration setter = setters.get(0);
        return setter.getJavadoc().map(Javadoc::toText);
    }
}
