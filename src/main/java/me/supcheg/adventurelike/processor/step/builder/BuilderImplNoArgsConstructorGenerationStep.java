package me.supcheg.adventurelike.processor.step.builder;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import org.jetbrains.annotations.NotNull;

public class BuilderImplNoArgsConstructorGenerationStep implements GenerationStep {
    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.constructorBuilder()
                        .build()
        );
    }
}
