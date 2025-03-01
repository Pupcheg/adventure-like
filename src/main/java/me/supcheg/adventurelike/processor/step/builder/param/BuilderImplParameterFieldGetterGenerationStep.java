package me.supcheg.adventurelike.processor.step.builder.param;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;

@RequiredArgsConstructor
public class BuilderImplParameterFieldGetterGenerationStep implements GenerationStep {
    private final ParameterSpec parameter;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.methodBuilder(parameter.name())
                        .addAnnotation(Nullable.class)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(parameter.type().box())
                        .addCode("return this.$L;\n", parameter.name())
                        .build()
        );
    }
}
