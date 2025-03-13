package me.supcheg.adventurelike.processor.impl.step.builder.constructor;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.impl.step.GenerationStep;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class BuilderImplNoArgsConstructorGenerationStep implements GenerationStep {
    private final List<BuilderImplParameterInitializer> initializers;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.constructorBuilder()
                        .addCode(codeBlock())
                        .build()
        );
    }

    @NotNull
    private CodeBlock codeBlock() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (BuilderImplParameterInitializer initializer : initializers) {
            builder.add(initializer.newValueInitializer());
        }
        return builder.build();
    }
}
