package me.supcheg.adventurelike.processor.strategy;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import me.supcheg.adventurelike.processor.parameter.ParameterSpecLookup;
import me.supcheg.adventurelike.processor.step.ConstructorGenerationStep;
import me.supcheg.adventurelike.processor.step.builder.BuilderImplGenerationStep;
import me.supcheg.adventurelike.processor.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.util.MoreTypes;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.List;

@RequiredArgsConstructor
public class BuildableAdventureLikeGenerationStrategy implements AdventureLikeGenerationStrategy {
    public static final String BUILDER_NAME = "Builder";
    public static final String IMPL_SUFFIX = "Impl";

    private final AnnotationHelper annotationHelper;
    private final MoreTypes moreTypes;
    private final ParameterSpecLookup parameterSpecLookup;

    @NotNull
    @Override
    public TypeSpec generate(@NotNull Element element) {
        ClassName targetName = ClassName.get(
                getPackageElement(element).getQualifiedName().toString(),
                element.getSimpleName() + IMPL_SUFFIX
        );

        TypeSpec.Builder target = TypeSpec.recordBuilder(targetName)
                .addSuperinterface(element.asType());

        List<ParameterSpec> parameters = parameterSpecLookup.listRecordParametersForInterface(element);

        new ConstructorGenerationStep(parameters).generate(target);
        new BuilderImplGenerationStep(
                annotationHelper,
                moreTypes,
                targetName,
                targetName.nestedClass(BUILDER_NAME + IMPL_SUFFIX),
                element,
                parameters
        ).generate(target);

        return target.build();
    }

    private PackageElement getPackageElement(Element element) {
        while (element != null && element.getKind() != ElementKind.PACKAGE) {
            element = element.getEnclosingElement();
        }
        return (PackageElement) element;
    }
}
