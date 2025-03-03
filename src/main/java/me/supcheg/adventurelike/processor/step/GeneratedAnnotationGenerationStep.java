package me.supcheg.adventurelike.processor.step;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.TypeSpec;
import me.supcheg.adventurelike.processor.AdventureLikeProcessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.time.OffsetDateTime;

public class GeneratedAnnotationGenerationStep implements GenerationStep {
    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addAnnotation(
                AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", AdventureLikeProcessor.class.getName())
                        .addMember("date", "$S", OffsetDateTime.now().toString())
                        .build()
        );
    }
}
