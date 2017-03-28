package robots.teacher_pack.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class MapUtils
{
    private static class EntriesComparer implements Comparator<Map.Entry<String, String>>
    {
        public final static EntriesComparer INSTANCE = new EntriesComparer();

        @Override
		public int compare(Map.Entry<String, String> e1, Map.Entry<String, String> e2)
        {
            return e1.getKey().compareTo(e2.getKey());
        }
    }

    private static <T> Iterable<T> asIterable(Stream<T> stream)
    {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator()
            {
                return stream.iterator();
            }
        };
    }

    public static void saveMap(String fileName, Map<String, String> map)
    {
        saveMap(new File(fileName), map);
    }

    public static void saveMap(File file, Map<String, String> map)
    {
        try (FileOutputStream fos = new FileOutputStream(file))
        {
            try (Writer w = new OutputStreamWriter(fos, "UTF-8"))
            {
                try (PrintWriter pw = new PrintWriter(w))
                {
                    for (Map.Entry<String, String> entry : asIterable(map.entrySet().stream().sorted(EntriesComparer.INSTANCE)))
                    {
                        pw.println(MessageFormat.format("{0}:{1}", entry.getKey(), entry.getValue()));
                    }
                }
            }
        }
        catch (IOException ex)
        {
            System.out.println(MessageFormat.format("Ошибка при записи словаря: {0}", ex.getMessage()));
        }
    }


    public static Map<String, String> loadMap(String fileName)
    {
        return loadMap(new File(fileName));
    }

    public static Map<String, String> loadMap(File file)
    {
        HashMap<String, String> map = new HashMap<>();
        loadMap(file, map);
        return map;
    }

    public static void loadMap(String fileName, Map<String, String> map)
    {
        loadMap(new File(fileName), map);
    }

    public static void loadMap(File file, Map<String, String> map)
    {
        try (FileInputStream fis = new FileInputStream(file))
        {
            try (Reader r = new InputStreamReader(fis, "UTF-8"))
            {
                try (BufferedReader br = new BufferedReader(r))
                {
                    String line;
                    while ((line = br.readLine()) != null)
                    {
                        int colonPos = line.indexOf(':');
                        if (colonPos > 0)
                        {
                            String key = line.substring(0, colonPos);
                            String value = line.substring(colonPos+1);
                            map.put(key, value);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            System.out.println(MessageFormat.format("Ошибка при чтении словаря: {0}", ex.getMessage()));
        }
    }

    public static String[] getPrefixes(Map<String, String> map)
    {
    	return map.keySet().stream().map((String key) -> key.split("\\.")[0]).distinct().toArray(String[]::new);
    }
}
