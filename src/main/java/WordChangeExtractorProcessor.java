import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class WordChangeExtractorProcessor extends TextProcessor {

    public WordChangeExtractorProcessor(String inputFilePath) throws IOException {
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
        try {
            // Пропускаем пустые строки или строки, не содержащие табуляцию
            if (line == null || line.trim().isEmpty() || !line.contains("\t")) {
                return line;
            }

            String[] parts = line.split("\t");
            // Проверяем, достаточно ли частей для обработки
            if (parts.length < 2 || parts[0].split(" ").length < 2 || parts[1].split(" ").length < 2) {
                return line; // Возвращаем исходную строку, если формат не соответствует ожидаемому
            }

            String singularWord = parts[0].split(" ")[1]; // Удаляем артикль
            String pluralWord = parts[1].split(" ")[1];

            String changes = analyzeChanges(singularWord, pluralWord);

            return parts[0] + "\t" + parts[1] + "\t" + changes;
        } catch (Exception e) {
            // В случае ошибки во время обработки строки, возвращаем исходную строку
            return line;
        }
    }

    private String analyzeChanges(String singular, String plural) {
        String umlaut = "-";
        String ending = "-";

        for (int i = 0; i < Math.min(singular.length(), plural.length()); i++) {
            if (singular.charAt(i) != plural.charAt(i) && "äöüÄÖÜ".indexOf(plural.charAt(i)) != -1) {
                umlaut = String.valueOf(plural.charAt(i));
                break; // Выходим из цикла после первого найденного умлаута
            }
        }

        if (plural.length() > singular.length()) {
            ending = ending + plural.substring(singular.length());
        }

        return "(" + umlaut + ", " + ending + ")";
    }

    public static void main(String[] args) {
        try {
            WordChangeExtractorProcessor processor = new WordChangeExtractorProcessor("workFiles/WordChangeExtractorProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


