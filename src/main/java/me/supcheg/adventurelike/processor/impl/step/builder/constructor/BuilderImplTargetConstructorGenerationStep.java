package me.supcheg.adventurelike.processor.impl.step.builder.constructor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.impl.step.GenerationStep;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class BuilderImplTargetConstructorGenerationStep implements GenerationStep {
    public static final String PARAMETER_NAME = "impl";

    private final ClassName implClassName;
    private final List<BuilderImplParameterInitializer> initializers;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.constructorBuilder()
                        .addParameter(implClassName, PARAMETER_NAME)
                        .addCode(code())
                        .build()
        );
    }

    private CodeBlock code() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (BuilderImplParameterInitializer initializer : initializers) {
            builder.add(initializer.copyingInitializer(PARAMETER_NAME));
        }
        return builder.build();
    }
}
