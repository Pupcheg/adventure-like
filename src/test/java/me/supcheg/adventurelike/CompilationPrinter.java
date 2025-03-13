package me.supcheg.adventurelike;

import com.google.testing.compile.Compilation;
import lombok.RequiredArgsConstructor;

import javax.tools.JavaFileObject;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

@RequiredArgsConstructor
final class CompilationPrinter implements Closeable {
    private final Compilation compilation;
    private final Writer out;

    public static void printGeneratedSourceFiles(Compilation compilation) throws IOException {
        new CompilationPrinter(compilation, new PrintWriter(System.out)).printGeneratedSourceFiles();
    }

    public void printGeneratedSourceFiles() throws IOException {
        for (JavaFileObject sourceFile : compilation.generatedSourceFiles()) {
            try (Reader reader = sourceFile.openReader(true)) {
                reader.transferTo(out);
                out.flush();
            }
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
