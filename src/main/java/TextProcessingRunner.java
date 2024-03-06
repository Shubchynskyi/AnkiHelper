public class TextProcessingRunner {

    public static void main(String[] args) {

        try {
//            TextProcessor processor = new GapFillProcessor(
//                    "workFiles/GapFillProcessor/inputFile.txt",
//                    "workFiles/GapFillProcessor/words.txt"
//            );
            TextProcessor processor = new TranslationExtractorProcessor(
                    "workFiles/TranslationExtractorProcessor/inputFile.txt"
            );
            processor.process();
        } catch (Exception e) {
            System.err.println("!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
        }
    }

}
