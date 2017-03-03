package project;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class CSVParser {

    private static final String CSV_FILENAME = "/file.txt";
    static Map<String, String> row;
    static Map<String, Set<String>> formulas = new HashMap<>();
    static Map<String, Set<String>> result = new LinkedHashMap<>();
    static String[] headers = null;

    private static Map<String, Set<String>> readWithCsvMapReader() throws IOException {
        ICsvMapReader mapReader = null;
        InputStream io = CSVParser.class.getResourceAsStream(CSV_FILENAME);
        Reader featIO = new InputStreamReader(io);
        try {
            mapReader = new CsvMapReader(featIO, CsvPreference.TAB_PREFERENCE);

            // the header columns are used as the keys to the Map

            headers = mapReader.getHeader(true);
//            final CellProcessor[] processors = getProcessors();

            while ((row = mapReader.read(headers)) != null) {
//                System.out.println(String.format("lineNo=%s, rowNo=%s", mapReader.getLineNumber(), mapReader.getRowNumber()));
                for (String header : headers) {
//                    System.out.println(header + " is " + row.get(header));
                    if (formulas.get(header) != null) {
                        Set<String> oldKeys = formulas.get(header);
                        if (row.get(header).equalsIgnoreCase("x")) {
//                          TODO Actually add the correct value
                            oldKeys.add(headers[(mapReader.getRowNumber() - 2)]);
                        }
                        formulas.put(header, oldKeys);
                    } else {
                        Set<String> oldKeys = new HashSet<>();
                        if (row.get(header).equalsIgnoreCase("x")) {
//                          TODO Actually add the correct value
                            oldKeys.add(headers[(mapReader.getRowNumber() - 2)]);
                        }
                        formulas.put(header, oldKeys);
                    }
                }
            }

        } finally {
            if (mapReader != null) {
                mapReader.close();
            }
            for (String header : headers) {
                Set<String> orderSet = new LinkedHashSet<>();
                Set<String> foundKeys = formulas.get(header);
                for (String key : headers) {
                    if (foundKeys.contains(key)) {
                        orderSet.add(key);
                    }
                }
                result.put(header, orderSet);
            }

        }
        return result;
    }

    /*private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull(), // each column must be unique
                new NotNull() // each column must be unique
        };

        return processors;
    }
*/
    public static void main(String[] args) {
        try {
            System.out.println(readWithCsvMapReader());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
