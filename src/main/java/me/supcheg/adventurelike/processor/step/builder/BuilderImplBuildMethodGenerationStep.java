package me.supcheg.adventurelike.processor.step.builder;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import me.supcheg.adventurelike.processor.step.builder.constructor.BuilderImplParameterInitializer;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.List;

@RequiredArgsConstructor
public class BuilderImplBuildMethodGenerationStep implements GenerationStep {
    private final ClassName buildableImplClassName;
    private final List<BuilderImplParameterInitializer> parameters;

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
            BuilderImplParameterInitializer param = it.next();
            builder.add(param.finalizer());
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
