import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HighlightWordsProcessor extends TextProcessor {
    private final Set<String> wordsSet;
    private final String color; // Хранение цвета в формате RGB CSS

    // Конструктор с указанием пути к файлу со словами и цветом в формате java.awt.Color
    public HighlightWordsProcessor(String inputFilePath, String wordsFilePath, Color color) throws IOException {
        super(inputFilePath);
        this.color = String.format("rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
        this.wordsSet = loadWordsSet(wordsFilePath);
    }

    // Конструктор с указанием пути к файлу со словами без указания цвета (используется цвет по умолчанию)
    public HighlightWordsProcessor(String inputFilePath, String wordsFilePath) throws IOException {
        this(inputFilePath, wordsFilePath, Color.BLUE); // Использование синего цвета по умолчанию
    }

    private Set<String> loadWordsSet(String wordsFilePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(wordsFilePath);
        if (inputStream == null) {
            throw new IOException("Файл не найден: " + wordsFilePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toCollection(HashSet::new));
        }
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines(); // Чтение строк из файла

        List<String> processedLines = lines.stream()
                .map(this::highlightLine) // Подсветка слов
                .collect(Collectors.toList());

        // Генерация пути к выходному файлу с учётом имени класса
        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";

        // Вызов метода записи с передачей сгенерированного пути
        writeLines(processedLines, outputFilePath);
    }

    private String highlightLine(String line) {
        String[] words = line.split("\\s+");
        StringBuilder highlightedLine = new StringBuilder();

        for (String word : words) {
            if (wordsSet.contains(word.toLowerCase())) {
                highlightedLine.append(String.format("<span style=\"color: %s;\"><b>%s</b></span> ", this.color, word));
            } else {
                highlightedLine.append(word).append(" ");
            }
        }

        return highlightedLine.toString().trim();
    }
}

