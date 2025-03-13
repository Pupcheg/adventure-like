package me.supcheg.adventurelike.processor.impl.strategy;

import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.ElementProcessContext;
import me.supcheg.adventurelike.processor.impl.step.ConstructorGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.GeneratedAnnotationGenerationStep;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SimpleAdventureLikeGenerationStrategy implements AdventureLikeGenerationStrategy {

    @NotNull
    @Override
    public TypeSpec generate(@NotNull ElementProcessContext ctx) {
        TypeSpec.Builder target = TypeSpec.recordBuilder(ctx.element().getSimpleName() + "Impl")
                .addSuperinterface(ctx.element().asType());

        List<ValueParameter> valueParameters = ctx.valueParameters();
        new ConstructorGenerationStep(valueParameters).generate(target);
        new GeneratedAnnotationGenerationStep(valueParameters).generate(target);

        return target.build();
    }
}
