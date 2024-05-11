import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DeutschPluralCheckerProcessor extends TextProcessor {

    public DeutschPluralCheckerProcessor(String inputFilePath) throws IOException {
        super(inputFilePath);
    }

    @Override
    public void process() throws IOException {
        // Чтение строк из файла
        List<String> lines = readLines();

        // Обработка каждой строки
        List<String> processedLines = lines.stream()
                .map(this::processLine)
                .collect(Collectors.toList());

        // Запись обработанных строк в выходной файл (пример пути для выходного файла, можно изменить)
        // Создание выходного файла в директории, соответствующей имени класса
        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath);
    }



    private String processLine(String line) {
        // Разбиваем строку на части по пробелу
        String[] parts = line.split(" ", 2);

        // Проверяем, есть ли в строке больше одного слова
        if (parts.length == 2) {
            // Проверяем, является ли первое слово "die"
            if (!parts[0].equals("die")) {
                // Если нет, заменяем первое слово на "die" и возвращаем измененную строку
                return "die " + parts[1];
            } else {
                // Если первое слово уже "die", возвращаем строку как есть
                return line;
            }
        }

        // Возвращаем исходную строку как есть, если в ней только одно слово или она пустая
        return line;
    }

    public static void main(String[] args) {
        try {
            DeutschPluralCheckerProcessor processor = new DeutschPluralCheckerProcessor("workFiles/DeutschPluralCheckerProcessor/inputFile.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

