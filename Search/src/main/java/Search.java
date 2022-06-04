import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Search {

    private String directory;
    private List<File> listFiles = new ArrayList<>();
    private String resultPath;

    public Search(String directory) {
        this.directory = directory;
        this.resultPath = directory + "\\result.txt";
    }

    public static void main(String[] args) {
        String directory = consoleInput();
        Search search = new Search(directory);
        File file = new File(search.directory);
        search.search(file);
        search.sort();
        search.read_write();
        System.out.println(String.format("Всего найдено текстовых файлов : %d, данные из которых были записаны " +
                "в результирующий файл: %s", search.listFiles.size(), search.resultPath));
    }

    // TODO: 04.06.2022 ввод директории с консоли
    public static String consoleInput() {
        String directory = null;
        try {
            BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Пожалуйста введите директорию для поиска!");
            directory = bufferedInputStream.readLine().trim();
            if (directory.equals("exit")) System.exit(0);
            File file = new File(directory);
            if (!file.exists()) throw new FileNotFoundException();
        } catch (Exception e) {
            if (e.getClass().getSimpleName().equals("FileNotFoundException")) {
                System.out.println("Вы ввели не верную директорию, " +
                        "проверьте данные или завершите работу программы");
                return consoleInput();
            } else e.printStackTrace();
        }
        return directory;
    }

    // TODO: 04.06.2022 метод поиска файлов в категории, заполняет listFiles
    public void search(File directory) {
        if (directory.isDirectory()) {
            for(File file : directory.listFiles()){
                if(file.isFile() && (file.getName().endsWith(".txt") ||  file.getName().endsWith(".TXT"))){
                    listFiles.add(file);
                } else {
                    search(file);
                }
            }
        }
        if (listFiles.size() == 0) {
            System.out.println("В указанной директории не найдено файлов формата \".txt\"");
            System.exit(0);
        }
    }

    // TODO: 04.06.2022 сортировка файлов по названию
    public void sort() {
        Comparator<File> comparator = new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(this.listFiles, comparator);
    }

    // TODO: 04.06.2022 метод склейки в один файл
    public void read_write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.resultPath));
            for (File file : this.listFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toString()))) {
                    while (reader.ready()) {
                        writer.write(reader.readLine());
                        writer.newLine();
                    }
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

