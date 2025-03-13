package me.supcheg.adventurelike.processor;

import me.supcheg.adventurelike.processor.impl.ImplementationGenerator;
import me.supcheg.adventurelike.processor.impl.strategy.BuildableAdventureLikeGenerationStrategy;
import me.supcheg.adventurelike.processor.impl.strategy.SimpleAdventureLikeGenerationStrategy;
import me.supcheg.adventurelike.processor.impl.util.AnnotationHelper;
import me.supcheg.adventurelike.processor.impl.util.MoreTypes;
import me.supcheg.adventurelike.processor.value.ValueParameterLookup;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("me.supcheg.adventurelike.AdventureLike")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class AdventureLikeProcessor extends AbstractProcessor {
    private final ValueParameterLookup valueParameterLookup = new ValueParameterLookup();
    private ImplementationGenerator implementationGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        MoreTypes moreTypes = new MoreTypes(processingEnv);

        this.implementationGenerator = new ImplementationGenerator(
                new SimpleAdventureLikeGenerationStrategy(),
                new BuildableAdventureLikeGenerationStrategy(
                        new AnnotationHelper(),
                        moreTypes
                ),
                moreTypes
        );
    }

    @Override
    public boolean process(@NotNull Set<? extends TypeElement> annotations, @NotNull RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element.getKind() != ElementKind.INTERFACE) {
                    processingEnv.getMessager().printError("Element is not an interface", element);
                    continue;
                }

                ElementProcessContext ctx = new ElementProcessContext(
                        ((TypeElement) element),
                        ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString(),
                        valueParameterLookup.listValueParameters(element)
                );

                try {
                    generateImplementations(ctx);
                } catch (Exception e) {
                    processingEnv.getMessager().printError(e.toString(), element);
                }
            }
        }
        return true;
    }

    private void generateImplementations(ElementProcessContext ctx) throws IOException {
        implementationGenerator.processElement(ctx, processingEnv.getFiler());
    }
}
