package me.supcheg.adventurelike.processor.step.builder;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class BuilderImplTargetConstructorGenerationStep implements GenerationStep {
    private final ClassName implClassName;
    private final List<ParameterSpec> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.constructorBuilder()
                        .addParameter(implClassName, "impl")
                        .addCode(code())
                        .build()
        );
    }

    private CodeBlock code() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (ParameterSpec parameter : parameters) {
            builder.add("this.$L = $L;\n", parameter.name(), parameter.name());
        }
        return builder.build();
    }
}
