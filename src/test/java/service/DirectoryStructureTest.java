package service;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectoryStructureTest {

    @Test
    public void emptyDirTest() throws IOException {
        SortedMap<String, Integer> expected = new TreeMap<>();
        assertEquals(expected, new DirectoryStructure("src/test/resources/empty-dir").getDirectoryStructure());
    }

    @Test
    public void dirWithDirTest() throws IOException {
        String fileName = "src/test/resources/dir-dir";
        Map<String, Integer> expected = Stream.of(new String[][] {
                {fileName, "0" }
        }).collect(Collectors.collectingAndThen(
                Collectors.toMap(data -> data[0], data -> Integer.parseInt(data[1])),
                Collections::<String, Integer> unmodifiableMap));
        assertEquals(expected, new DirectoryStructure(fileName).getDirectoryStructure());
    }

    @Test
    public void dirWithTwoFilesTest() throws IOException {
        String fileName = "src/test/resources/dir1";
        String file1 = "file1.txt";
        String file2 = "file2.red";
        LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();
        expected.put(fileName, 4);
        expected.put(fileName+"/"+file1, 3);
        expected.put(fileName+"/"+file2, 1);
        assertEquals(expected, new DirectoryStructure(fileName).getDirectoryStructure());
    }

    @Test
    public void complexTest() throws IOException {
        String fileName = "src/test/resources/dir2";
        String inner1 = "dir1";
        // both in dir2 and dir1
        String file1 = "file1.txt";
        String file2 = "file2.red";

        String inner2 = "dir3";
        String file3 = "file3_1.red";
        String file4 = "file3_2.txt";

        LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();
        expected.put(fileName, 4);

        expected.put(fileName+"/"+inner1, 4);
        expected.put(fileName+"/"+inner1+"/"+file1, 3);
        expected.put(fileName+"/"+inner1+"/"+file2, 1);

        expected.put(fileName+"/"+inner2, 8);
        expected.put(fileName+"/"+inner2+"/"+file3, 5);
        expected.put(fileName+"/"+inner2+"/"+file4, 3);

        expected.put(fileName+"/"+file1, 3);
        expected.put(fileName+"/"+file2, 1);

        expected.put(fileName+"/"+file1, 3);
        expected.put(fileName+"/"+file2, 1);
        assertEquals(expected, new DirectoryStructure(fileName).getDirectoryStructure());
    }


    @Test
    public void m1(){
        assertTrue(true, "Just test for test :)");
    }
}
