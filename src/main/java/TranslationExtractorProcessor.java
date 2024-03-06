import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TranslationExtractorProcessor extends TextProcessor {

    public TranslationExtractorProcessor(String inputFilePath) throws IOException {
        super(inputFilePath);
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines(); // Чтение строк из файла

        List<String> processedLines = lines.stream()
                .map(this::processLine)
                .collect(Collectors.toList());

        // Создание выходного файла в директории, соответствующей имени класса
        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath);
    }

    private String processLine(String line) {
        String[] parts = line.split("\t");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }
        String translation = parts[2].substring(parts[2].indexOf('(') + 1, parts[2].lastIndexOf(')')); // Извлекаем перевод
        String partWithoutTranslation = parts[2].substring(0, parts[2].indexOf('(')).trim(); // Удаляем перевод из исходной части
        // Возвращаем строку в новом формате, заменив исходную часть без перевода
        return String.format("%s\t%s\t%s\t%s", parts[0], parts[1], partWithoutTranslation, translation);
    }
}


