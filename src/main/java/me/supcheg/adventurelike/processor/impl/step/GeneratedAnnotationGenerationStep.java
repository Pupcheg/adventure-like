package me.supcheg.adventurelike.processor.impl.step;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.AdventureLikeProcessor;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.time.OffsetDateTime;
import java.util.List;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class GeneratedAnnotationGenerationStep implements GenerationStep {
    private final List<ValueParameter> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addAnnotation(
                AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", AdventureLikeProcessor.class.getName())
                        .addMember("date", "$S", OffsetDateTime.now().toString())
                        .addMember("comments", "$S", buildParametersComment())
                        .build()
        );
    }

    private String buildParametersComment() {
        return parameters.isEmpty() ?
                "No parameters found" :
                parameters.stream()
                        .map(param -> param.type() + " " + param.name())
                        .collect(joining(", ", "Parameters: ", ""));
    }
}
