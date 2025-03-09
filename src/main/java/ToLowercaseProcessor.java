import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ToLowercaseProcessor extends TextProcessor {

    public ToLowercaseProcessor(String inputFilePath) throws IOException {
        super(inputFilePath);
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines(); // Чтение строк из файла

        List<String> processedLines = lines.stream()
                .map(String::toLowerCase) // Преобразование каждой строки в строчные символы
                .map(String::trim)
                .collect(Collectors.toList());

        // Создание выходного файла в директории, соответствующей имени класса
        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath); // Запись обработанных строк в выходной файл
    }

    public static void main(String[] args) {
        try {
            ToLowercaseProcessor processor = new ToLowercaseProcessor("workFiles/ToLowercaseProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
