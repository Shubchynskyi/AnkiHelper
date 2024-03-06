import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public abstract class TextProcessor {
    protected InputStream inputFileStream;

    public TextProcessor(String inputFilePath) throws IOException {
        this.inputFileStream = getClass().getClassLoader().getResourceAsStream(inputFilePath);
        if (this.inputFileStream == null) {
            // Если ресурс не найден, пытаемся открыть как файл
            Path inputPath = Paths.get(inputFilePath);
            if (Files.exists(inputPath)) {
                this.inputFileStream = new FileInputStream(inputPath.toFile());
            } else {
                throw new IOException("Файл входных данных не найден: " + inputFilePath);
            }
        }
    }

    public abstract void process() throws IOException;

    protected List<String> readLines() throws IOException {
        if (this.inputFileStream == null) {
            throw new IOException("InputStream не был инициализирован.");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputFileStream))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    public void writeLines(List<String> lines, String outputFilePath) throws IOException {
        // Создаём путь к родительской директории файла (если он включает имя директории)
        Path directoryPath = Paths.get(outputFilePath).getParent();
        // Проверяем, существует ли директория
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath); // Создаём директорию, если она не существует
        }

        // Затем, как и ранее, записываем строки в файл
        Path outputPath = Paths.get(outputFilePath); // Путь к выходному файлу
        Files.write(outputPath, lines); // Записываем строки в файл
    }
}



