import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ColorArticleTextProcessor extends TextProcessor {

    public ColorArticleTextProcessor(String inputFilePath) throws IOException {
        super(inputFilePath);
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines();

        List<String> processedLines = lines.stream()
                .map(this::colorFirstWord)
                .collect(Collectors.toList());

        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath);
    }

    private String colorFirstWord(String line) {
        String[] parts = line.split(" ", 2);
        String firstWord = parts[0];
        String restOfLine = parts.length > 1 ? " " + parts[1] : "";

        String coloredWord = colorWord(firstWord);
        return coloredWord + restOfLine;
    }

    private String colorWord(String word) {
        return switch (word) {
            case "der" -> "<span style=\"color: rgb(42, 170, 225); font-weight: bold;\">" + word + "</span>"; // Синий + жирный
            case "das" -> "<span style=\"color: rgb(42, 170, 3); font-weight: bold;\">" + word + "</span>"; // Зеленый + жирный
            case "die" -> "<span style=\"color: rgb(225, 42, 42); font-weight: bold;\">" + word + "</span>"; // Красный + жирный
            default -> word; // Возвращает слово без изменений, если оно не соответствует ни одному из условий
        };
    }

    public static void main(String[] args) {
        try {
            ColorArticleTextProcessor processor = new ColorArticleTextProcessor("workFiles/ColorArticleTextProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
