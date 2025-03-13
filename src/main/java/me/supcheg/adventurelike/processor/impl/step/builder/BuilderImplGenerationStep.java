package me.supcheg.adventurelike.processor.impl.step.builder;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.impl.step.GenerationStep;
import me.supcheg.adventurelike.processor.impl.step.ToBuilderImplGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.builder.constructor.BuilderImplCollectionParameterInitializer;
import me.supcheg.adventurelike.processor.impl.step.builder.constructor.BuilderImplNoArgsConstructorGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.builder.constructor.BuilderImplParameterInitializer;
import me.supcheg.adventurelike.processor.impl.step.builder.constructor.BuilderImplTargetConstructorGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.builder.param.BuilderImplCollectionParameterGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.builder.param.BuilderImplParameterGenerationStep;
import me.supcheg.adventurelike.processor.impl.step.builder.param.BuilderImplPrimitiveParameterGenerationStep;
import me.supcheg.adventurelike.processor.impl.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.impl.util.MoreTypes;
import me.supcheg.adventurelike.processor.value.ValueParameter;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BuilderImplGenerationStep implements GenerationStep {
    private final AnnotationHelper annotationHelper;
    private final MoreTypes moreTypes;
    private final ClassName buildableImplClassName;
    private final ClassName builderImplClassName;
    private final Element element;
    private final List<ValueParameter> parameters;

    @Override
    public void generate(TypeSpec.@NotNull Builder target) {
        TypeSpec.Builder targetBuilder = TypeSpec.classBuilder(builderImplClassName)
                .addSuperinterface(findBuilderElement(element).asType())
                .addModifiers(Modifier.STATIC, Modifier.FINAL);

        List<BuilderImplParameterInitializer> parameterInitializers = parameters.stream()
                .map(this::parameterInitializer)
                .toList();

        new BuilderImplNoArgsConstructorGenerationStep(parameterInitializers).generate(targetBuilder);
        new BuilderImplTargetConstructorGenerationStep(buildableImplClassName, parameterInitializers).generate(targetBuilder);

        for (ValueParameter parameter : parameters) {
            parameterGenerationStep(parameter).generate(targetBuilder);
        }

        new BuilderImplBuildMethodGenerationStep(buildableImplClassName, parameterInitializers).generate(targetBuilder);
        target.addType(targetBuilder.build());

        new ToBuilderImplGenerationStep(builderImplClassName).generate(target);
    }

    private TypeElement findBuilderElement(@NotNull Element element) {
        for (TypeElement candidate : ElementFilter.typesIn(element.getEnclosedElements())) {
            if (candidate.getKind() == ElementKind.INTERFACE && candidate.getSimpleName().toString().equals("Builder")) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("No builder found for element " + element);
    }

    private BuilderImplParameterInitializer parameterInitializer(@NotNull ValueParameter parameter) {
        if (moreTypes.isAccessible(Collection.class, parameter.type())) {
            return new BuilderImplCollectionParameterInitializer(moreTypes, parameter);
        }

        return new BuilderImplParameterInitializer(parameter);
    }

    private BuilderImplParameterGenerationStep parameterGenerationStep(@NotNull ValueParameter parameter) {
        if (parameter.type().isPrimitive()) {
            return new BuilderImplPrimitiveParameterGenerationStep(parameter, annotationHelper, builderImplClassName);
        }

        if (moreTypes.isAccessible(Collection.class, parameter.type())) {
            return new BuilderImplCollectionParameterGenerationStep(parameter, annotationHelper, builderImplClassName);
        }

        return new BuilderImplParameterGenerationStep(parameter, annotationHelper, builderImplClassName);
    }
}
