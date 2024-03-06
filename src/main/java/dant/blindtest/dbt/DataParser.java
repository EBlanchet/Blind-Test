package dant.blindtest.dbt;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DataParser {
    private static List<String> keys;
    private static HashMap<String, String> data;

    DataParser(GameTypes type) {
        DataParser.data = new HashMap<>();
        DataParser.keys = new ArrayList<>();

        String dataSource = switch (type) {
            case PEOPLE -> "data/person_list.txt";
            case MUSIC -> "data/music_list.txt";
        };

        this.init(dataSource);
    }

    private void init(String sourceName) {
        if (sourceName.equals("none")) throw new IllegalArgumentException("Couldn't link to a data source file.");

        Path path;
        try {
            path = Path.of(Objects.requireNonNull(DBTApplication.class.getResource(sourceName)).toURI());
            List<String> lines = Files.readAllLines(path);
            lines.forEach((String line) -> {
                String[] fields = line.split(":");
                if (fields.length == 2) {
                    DataParser.keys.add(fields[0]);
                    DataParser.data.put(fields[0], fields[1]);
                } else {
                    throw new RuntimeException("More fields that there should be!");
                }
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return DataParser.data.get(key);
    }

    public static List<String> keys() {
        return new ArrayList<>(keys);
    }

    public static Integer getSize() {
        return DataParser.data.size();
    }
}
