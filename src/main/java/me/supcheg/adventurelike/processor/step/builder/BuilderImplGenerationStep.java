package me.supcheg.adventurelike.processor.step.builder;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.step.GenerationStep;
import me.supcheg.adventurelike.processor.step.ToBuilderImplGenerationStep;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;

@RequiredArgsConstructor
public class BuilderImplGenerationStep implements GenerationStep {
    private final AnnotationHelper annotationHelper;
    private final ClassName buildableImplClassName;
    private final Element element;
    private final List<ParameterSpec> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        ClassName builerImplClassName = buildableImplClassName.nestedClass("BuilderImpl");

        TypeSpec.Builder targetBuilder = TypeSpec.classBuilder(builerImplClassName)
                .addSuperinterface(findBuilderElement(element).asType())
                .addModifiers(Modifier.STATIC);

        new BuilderImplNoArgsConstructorGenerationStep().generate(targetBuilder);
        new BuilderImplTargetConstructorGenerationStep(buildableImplClassName, parameters).generate(targetBuilder);

        for (ParameterSpec parameter : parameters) {
            new BuilderImplParameterGenerationStep(annotationHelper, builerImplClassName, parameter).generate(targetBuilder);
        }

        new BuilderImplBuildMethodGenerationStep(buildableImplClassName, parameters).generate(targetBuilder);
        target.addType(targetBuilder.build());

        new ToBuilderImplGenerationStep(builerImplClassName).generate(target);
    }

    private TypeElement findBuilderElement(@NotNull Element element) {
        for (TypeElement candidate : ElementFilter.typesIn(element.getEnclosedElements())) {
            if (candidate.getKind() == ElementKind.INTERFACE && candidate.getSimpleName().toString().equals("Builder")) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("No builder found for element " + element);
    }
}
