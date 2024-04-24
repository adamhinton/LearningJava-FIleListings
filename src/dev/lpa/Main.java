package dev.lpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        // aka the cwd
        Path path = Path.of("");
        System.out.println("cwd = " + path.toAbsolutePath());

        try (Stream<Path> paths = Files.list(path)) {
            paths
                    .map(Main::listDir)
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    private static String listDir (Path path){
        try{
            boolean isDir = Files.isDirectory(path);
            FileTime dateField = Files.getLastModifiedTime(path);
            return "%s %-15s %s".formatted(dateField, (isDir ? "<DIR>" : ""), path);
        }
        catch(IOException e){
            System.out.println("Whoops! Something went wrong with " + path);
            return path.toString();
        }
    }
}
