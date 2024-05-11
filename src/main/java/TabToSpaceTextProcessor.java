import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TabToSpaceTextProcessor extends TextProcessor {

    public TabToSpaceTextProcessor(String inputFilePath) throws IOException {
        super(inputFilePath);
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines();

        List<String> processedLines = lines.stream()
                .map(line -> line.trim().replace("\t", "      ")) // Выполняем trim и заменяем табуляцию на 4 пробела
                .collect(Collectors.toList());

        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath);
    }

    public static void main(String[] args) {
        try {
            TabToSpaceTextProcessor processor = new TabToSpaceTextProcessor("workFiles/TabToSpaceTextProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

