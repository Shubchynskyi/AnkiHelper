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

        String coloredWord = colorWord(firstWord, restOfLine); // pass restOfLine as second argument
        return coloredWord + restOfLine;
    }

    private String colorWord(String word, String restOfLine) {
        String color;
        switch (word) {
            case "der":
                color = "rgb(42, 170, 225)"; // Синий
                break;
            case "das":
                color = "rgb(42, 170, 3)"; // Зеленый
                break;
            case "die":
                if (restOfLine.contains("(Pl.)")) {
                    color = "rgb(255, 165, 0)"; // Оранжевый
                } else {
                    color = "rgb(225, 42, 42)"; // Красный
                }
                break;
            default:
                return word; // Если слово не является артиклем, возвращаем его без изменений
        }
        return "<span style=\"color: " + color + "; font-weight: bold;\">" + word + "</span>";
    }

    public static void main(String[] args) {
        try {
            ColorArticleTextProcessor processor = new ColorArticleTextProcessor(
                    "workFiles/ColorArticleTextProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
