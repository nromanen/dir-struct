package service;

import java.util.*;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;


public class DirectoryStructure {

    private static String separatorPattern = Pattern.quote(System.getProperty("file.separator"));
    private String fileName;

    public DirectoryStructure(String fileName) {
        this.fileName = fileName;
    }

    private  SortedMap<String, Integer> fileMaps = new TreeMap<>((o1, o2)->{
        return o1.compareTo(o2.trim());});


    private  int lineNumber(File file){
        int count = 1;
        try(FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)){
            ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
            while(buffer.hasRemaining()){
                byte currentByte = buffer.get();
                if(currentByte == '\n'){
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with " + file + "\n "+ Arrays.toString(e.getStackTrace()));
        }
        return count;
    }

    private static String lastAfterSlash(String s){
        return  s.substring(0,s.indexOf(s.trim()))+s.split(separatorPattern)[s.split(separatorPattern).length-1];
    }


    public int walking(File file) {
        if (file.isFile()){
            int count = lineNumber(file);
            fileMaps.compute(file.toString(),
                    (k,v) -> (v==null ? 0 : v) + count);
            return count;
        }
        for (File f : file.listFiles()) {
            fileMaps.compute(file.toString(),
                    (k, v) -> (v == null ? 0 : v) + walking(f));
        }
        return 0;
    }
    private String spaceSymbolsBeforeName(String fileName){
        int spaceCount = new File(fileName).getAbsolutePath().split(Pattern.quote(System.getProperty("file.separator"))).length;
        return " ".repeat(spaceCount*2);
    }

    public  void printDirectoryStructure(){
        walking(new File(fileName));
        fileMaps.forEach((k, v) -> System.out.println(spaceSymbolsBeforeName(k)+DirectoryStructure.lastAfterSlash(k) + " : " + v));
    }

    public  Map<String, Integer> getDirectoryStructure(){
        walking(new File(fileName));
        return fileMaps;
    }

    public static void main(String ...strings ) {
        try {
            new DirectoryStructure(strings[0]).printDirectoryStructure();
        }
        catch(Exception e){
            System.out.println("Something went wrong with file " + strings[0]);
        }

    }
}