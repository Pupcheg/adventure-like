package me.supcheg.adventurelike.processor.impl.strategy;

import com.palantir.javapoet.TypeSpec;
import me.supcheg.adventurelike.processor.ElementProcessContext;
import org.jetbrains.annotations.NotNull;

public interface AdventureLikeGenerationStrategy {
    @NotNull
    TypeSpec generate(@NotNull ElementProcessContext ctx);
}
