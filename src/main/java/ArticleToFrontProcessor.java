import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ArticleToFrontProcessor extends TextProcessor {
    private final boolean colorize;
    // Обновленное регулярное выражение
    private static final Pattern ARTICLE_PATTERN = Pattern.compile("(.+?)\\b(die/der|der/die|der|die|das)\\b\\s*(.*)");
    private static final Pattern ARTICLE_AT_START_PATTERN = Pattern.compile("^(die|der|das)\\b\\s*(.*)");

    public ArticleToFrontProcessor(String inputFilePath, boolean colorize) throws IOException {
        super(inputFilePath);
        this.colorize = colorize;
    }

    @Override
    public void process() throws IOException {
        List<String> lines = readLines();
        List<String> processedLines = lines.stream()
                .map(this::processLine)
                .collect(Collectors.toList());

        String className = this.getClass().getSimpleName();
        String outputFilePath = "workFiles/" + className + "/outputFile_" + System.currentTimeMillis() + ".txt";
        writeLines(processedLines, outputFilePath);
    }

    private String processLine(String line) {
        // Сначала пытаемся найти артикли, стоящие в начале строки
        Matcher startMatcher = ARTICLE_AT_START_PATTERN.matcher(line);
        if (startMatcher.find()) {
            String article = startMatcher.group(1);
            String afterArticle = startMatcher.group(2);

            // Колоризируем артикль если нужно
            article = getArticle(article, afterArticle, colorize);

            // Возвращаем измененную строку
            return article + " " + afterArticle;
        }

        // Если в начале строки артикль не найден, ищем по старому паттерну
        Matcher matcher = ARTICLE_PATTERN.matcher(line);
        if (matcher.find()) {
            String beforeArticle = matcher.group(1).trim();
            String article = matcher.group(2);
            String afterArticle = matcher.group(3);

            System.out.println("Before Article: '" + beforeArticle + "'");
            System.out.println("Article: '" + article + "'");
            System.out.println("After Article: '" + afterArticle + "'");

            String result = formatResult(beforeArticle, article, afterArticle);
            System.err.println("result: " + result);
            return result;
        }
        System.out.println("No match found for line: '" + line + "'");
        return line; // Return the original line if no match is found
    }


    private String formatResult(String beforeArticle, String article, String afterArticle) {
        // Ищем индекс последней заглавной буквы с конца
        int lastCapitalIndex = -1;
        for (int i = beforeArticle.length() - 1; i >= 0; i--) {
            if (Character.isUpperCase(beforeArticle.charAt(i))) {
                lastCapitalIndex = i;
                break;
            }
        }

        if (lastCapitalIndex == -1) {
            // Если заглавная буква не найдена, возвращаем исходную строку
            return beforeArticle + " " + article + " " + afterArticle;
        }

        // Ищем пробел перед найденной заглавной буквой, чтобы определить начало существительного
        int spaceBeforeCapitalIndex = beforeArticle.lastIndexOf(' ', lastCapitalIndex - 1);

        // Если пробел не найден, используем весь `beforeArticle` как существительное
        if (spaceBeforeCapitalIndex == -1) spaceBeforeCapitalIndex = 0;
        else spaceBeforeCapitalIndex += 1; // Смещаемся за пробел, чтобы начать с заглавной буквы

        // Извлекаем последнее слово (существительное), начиная с найденной заглавной буквы
        String lastWord = beforeArticle.substring(spaceBeforeCapitalIndex);
        String textBeforeNoun = beforeArticle.substring(0, spaceBeforeCapitalIndex).trim();

        // Получаем артикль с возможной стилизацией
        article = getArticle(article, afterArticle, colorize);

        // Проверяем, начинается ли `afterArticle` с запятой
        String spaceOrNot = (afterArticle.startsWith(",") ? "" : " ");

        // Формируем строку с артиклем, правильно размещаем артикль перед существительным
        String formattedArticle = article + " " + lastWord + spaceOrNot;

        // Собираем итоговую строку: текст до существительного, артикль с существительным, и все что после
        String result = textBeforeNoun + " " + formattedArticle + afterArticle;

        // Возвращаем итоговую строку, обрезая лишние пробелы на концах если они есть
        return result.trim();
    }



//    private String formatResult(String beforeArticle, String article, String afterArticle) {
//        // Ищем индекс первой заглавной буквы с конца
//        int lastCapitalIndex = -1;
//        for (int i = beforeArticle.length() - 1; i >= 0; i--) {
//            if (Character.isUpperCase(beforeArticle.charAt(i))) {
//                lastCapitalIndex = i;
//                break;
//            }
//        }
//
//        if (lastCapitalIndex == -1) {
//            // Если заглавная буква не найдена, возвращаем исходную строку
//            return beforeArticle + " " + article + " " + afterArticle;
//        }
//
//        // Извлекаем последнее слово (существительное), начиная с найденной заглавной буквы
//        String lastWord = beforeArticle.substring(lastCapitalIndex);
//        String textBeforeNoun = beforeArticle.substring(0, lastCapitalIndex).trim();
//
//        // Получаем артикль с возможной стилизацией
//        article = getArticle(article, afterArticle, colorize);
//
//        // Формируем строку с артиклем, правильно размещаем артикль перед существительным
//        String formattedArticle = article + " " + lastWord;
//
//        // Собираем итоговую строку: текст до существительного, артикль с существительным, и все что после
//        String result = textBeforeNoun + " " + formattedArticle + " " + afterArticle;
//
//        // Возвращаем итоговую строку, обрезая лишние пробелы на концах если они есть
//        return result.trim();
//    }


    private String getArticle(String article, String afterArticle, boolean colorize) {
        if (colorize) {
//             Проверяем, содержит ли строка "Plural"
            boolean isPlural = afterArticle.contains("(Plural)");

            // Определяем цвет в зависимости от артикля и наличия "Plural"
            String color;
            if (isPlural) {
                color = "rgb(255, 165, 0)"; // Оранжевый
            } else {
                color = switch (article) {
                    case "der" -> "rgb(42, 170, 225)"; // Синий
                    case "das" -> "rgb(42, 170, 3)"; // Зеленый
                    case "die" -> "rgb(225, 42, 42)"; // Красный
                    default -> "black"; // По умолчанию черный
                };
            }

            // Формируем стиль для артикля с соответствующим цветом
            article = "<span style=\"color: " + color + "; font-weight: bold;\">" + article + "</span>";
        }
        return article;
    }


    private String determineColor(String article, String qualifier) {
        String baseStyle = "font-weight: bold;";
        switch (article) {
            case "der":
                return "color: rgb(42, 170, 225);" + baseStyle; // Синий + жирный
            case "das":
                return "color: rgb(42, 170, 3);" + baseStyle; // Зеленый + жирный
            case "die":
                if (qualifier.equals("(Plural)")) {
                    return "color: rgb(255, 165, 0);" + baseStyle; // Оранжевый + жирный
                } else {
                    return "color: rgb(225, 42, 42);" + baseStyle; // Красный + жирный
                }
            default:
                return baseStyle; // Просто жирный текст без цвета
        }
    }


    public static void main(String[] args) {
        try {
            ArticleToFrontProcessor processor = new ArticleToFrontProcessor(
                            "workFiles/ArticleToFrontProcessor/inputFile.txt",
                            false);
            processor.process();
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
