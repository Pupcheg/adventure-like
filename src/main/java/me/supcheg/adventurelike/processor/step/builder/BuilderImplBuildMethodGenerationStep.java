package me.supcheg.adventurelike.processor.step.builder;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BuilderImplBuildMethodGenerationStep implements GenerationStep {
    private final ClassName buildableImplClassName;
    private final List<ParameterSpec> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        target.addMethod(
                MethodSpec.methodBuilder("build")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(NotNull.class)
                        .addAnnotation(Override.class)
                        .returns(buildableImplClassName)
                        .addCode(code())
                        .build()
        );
    }

    private CodeBlock code() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("return new $T(\n", buildableImplClassName);

        builder.indent();
        for (var it = parameters.iterator(); it.hasNext(); ) {
            ParameterSpec param = it.next();
            builder.add("$T.requireNonNull($L, $S)", Objects.class, param.name(), param.name());
            if (it.hasNext()) {
                builder.add(",");
            }
            builder.add("\n");
        }
        builder.unindent();

        builder.add(");\n");
        return builder.build();
    }
}
