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

    // todo количество строк вынести в переменную для работы с разными
    private String processLine(String line, int stringCount) {
        stringCount = 3;
        String[] parts = line.split("\t");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }
        // Извлекаем перевод
        String translation = parts[2].substring(parts[2].indexOf('(') + 1, parts[2].lastIndexOf(')'));
        // Удаляем перевод из исходной части
        String partWithoutTranslation = parts[2].substring(0, parts[2].indexOf('(')).trim();
        // Возвращаем строку в новом формате, заменив исходную часть без перевода
        return String.format("%s\t%s\t%s\t%s", parts[0], parts[1], partWithoutTranslation, translation);
    }

    private String processLine(String line) {


//        String translation = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
        try {
            // Извлекаем перевод
            String translation = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
            // Удаляем перевод из исходной части
            String partWithoutTranslation = line.substring(0, line.indexOf('(')).trim();
            // Возвращаем строку в новом формате, заменив исходную часть без перевода
            return String.format("%s\t%s", partWithoutTranslation, translation);
        } catch (Exception e){
            System.err.println("break: " + line);
        }
        return line;
    }
}


