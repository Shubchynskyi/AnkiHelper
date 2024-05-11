import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShuffleTextProcessor extends TextProcessor {

    public ShuffleTextProcessor(String inputFilePath) throws IOException {
        super(inputFilePath);
    }

    @Override
    public void process() throws IOException {
        // Чтение всех строк из файла и фильтрация пустых строк
        List<String> lines = readLines().stream()
                .filter(line -> !line.trim().isEmpty()) // Игнорируем пустые строки
                .collect(Collectors.toList());

        // Перемешивание строк
//        Collections.shuffle(lines);

        // Можно вызвать Collections.shuffle(lines) несколько раз, если нужно увеличить "случайность"
        for (int i = 0; i < 5; i++) Collections.shuffle(lines);

        // Формирование пути к выходному файлу с уникальным именем
        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";

        // Запись перемешанных строк в выходной файл
        writeLines(lines, outputFilePath);
    }

    public static void main(String[] args) {
        try {
            ShuffleTextProcessor processor = new ShuffleTextProcessor("workFiles/ShuffleTextProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
