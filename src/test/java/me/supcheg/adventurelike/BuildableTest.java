package me.supcheg.adventurelike;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import me.supcheg.adventurelike.processor.AdventureLikeProcessor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.google.testing.compile.CompilationSubject.assertThat;

class BuildableTest {
    @Test
    void myCoolInterface() throws IOException {
        Compilation compilation = Compiler.javac()
                .withProcessors(new AdventureLikeProcessor())
                .compile(JavaFileObjects.forResource("buildable/MyCoolInterface.java"));

        assertThat(compilation).generatedSourceFile("me.supcheg.adventurelike.test.MyCoolInterfaceImpl");
        assertThat(compilation).succeeded();

        CompilationPrinter.printGeneratedSourceFiles(compilation);
    }
}
