package me.supcheg.adventurelike.processor.strategy;

import com.palantir.javapoet.TypeSpec;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;

public interface AdventureLikeGenerationStrategy {
    @NotNull
    TypeSpec generate(@NotNull Element element);
}
