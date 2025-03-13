package me.supcheg.adventurelike.processor.impl.step;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;

@RequiredArgsConstructor
public class ToBuilderImplGenerationStep implements GenerationStep {
    private final ClassName builderImplClassName;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.methodBuilder("toBuilder")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(NotNull.class)
                        .addAnnotation(Override.class)
                        .returns(builderImplClassName)
                        .addCode(code())
                        .build()
        );
    }

    private CodeBlock code() {
        return CodeBlock.of("return new $T(this);\n", builderImplClassName);
    }
}
