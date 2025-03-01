package me.supcheg.adventurelike.processor.step;

import com.palantir.javapoet.TypeSpec;
import org.jetbrains.annotations.NotNull;

public interface GenerationStep {
    void generate(@NotNull TypeSpec.Builder target);
}
