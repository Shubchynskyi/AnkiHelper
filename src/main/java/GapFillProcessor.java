import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GapFillProcessor extends TextProcessor {
    private final Set<String> wordsSet;

    public GapFillProcessor(String inputFilePath, String wordsFilePath) throws IOException {
        super(inputFilePath);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(wordsFilePath);
        if (inputStream == null) {
            // Если файл с словами находится в файловой системе
            Path wordsPath = Paths.get(wordsFilePath);
            if (Files.exists(wordsPath)) {
                inputStream = new FileInputStream(wordsPath.toFile());
            } else {
                throw new IOException("Файл не найден: " + wordsFilePath);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            wordsSet = reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toCollection(HashSet::new));
        }
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines(); // Чтение строк из файла

        List<String> processedLines = lines.stream()
                .filter(line -> !line.trim().isEmpty())
                .map(this::processLine)
                .collect(Collectors.toList());

        // Создание выходного файла в директории, соответствующей имени класса
        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath);
    }

//    private String processLine(String line) {
//        int firstParenthesisIndex = line.indexOf('(');
//        int lastParenthesisIndex = line.indexOf(')');
//
//        String partBeforeParentheses = line.substring(0, firstParenthesisIndex).trim();
//        String partBetweenParentheses = line.substring(firstParenthesisIndex + 1, lastParenthesisIndex).trim();
//
//        // Обрабатываем строку до скобки
//        String processedPartBeforeParentheses = Arrays.stream(partBeforeParentheses.split("\\s+"))
//                .map(word -> wordsSet.contains(word.toLowerCase()) ? "{{c1::" + word + "}}" : word)
//                .collect(Collectors.joining(" "));
//
//        // Собираем итоговую строку
//        return processedPartBeforeParentheses + "\t" + partBetweenParentheses + "\t" + partBeforeParentheses;
//    }



    private String processLine(String line) {
        // Разбиваем строку на слова, сохраняя знаки пунктуации
        return Arrays.stream(line.split("\\s+"))
                .map(word -> {
                    // Определяем, есть ли знак пунктуации в конце слова
                    String punctuation = word.replaceAll("[\\wа-яА-Я]+", ""); // Удаляем все, кроме знаков пунктуации
                    String cleanWord = word.replaceAll("\\p{Punct}", ""); // Удаляем знаки пунктуации из слова

                    // Проверяем, находится ли "очищенное" слово в списке
                    if (wordsSet.contains(cleanWord.toLowerCase())) {
                        // Если да, оборачиваем слово и возвращаем с пунктуацией
                        return "{{c1::" + cleanWord + "}}" + punctuation;
                    } else {
                        // Если слово не в списке, возвращаем как есть
                        return word;
                    }
                })
                .collect(Collectors.joining(" "));
    }



    public static void main(String[] args) {
        try {
            GapFillProcessor processor = new GapFillProcessor("workFiles/GapFillProcessor/inputFile.txt", "workFiles/GapFillProcessor/words.txt");
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}