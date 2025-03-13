package me.supcheg.adventurelike.processor.impl.strategy;

import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.ElementProcessContext;
import me.supcheg.adventurelike.processor.impl.step.ConstructorGenerationStep;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class SimpleAdventureLikeGenerationStrategy implements AdventureLikeGenerationStrategy {

    @NotNull
    @Override
    public TypeSpec generate(@NotNull ElementProcessContext ctx) {
        TypeSpec.Builder builder = TypeSpec.recordBuilder(ctx.element().getSimpleName() + "Impl")
                .addSuperinterface(ctx.element().asType());

        new ConstructorGenerationStep(ctx.valueParameters()).generate(builder);

        return builder.build();
    }
}
