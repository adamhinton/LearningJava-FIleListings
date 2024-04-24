package dev.lpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        // aka the cwd
        Path path = Path.of("");
        System.out.println("cwd = " + path.toAbsolutePath());

        try (Stream<Path> paths = Files.list(path)) {
            paths
                    // dist bw dir and file
                    .map(Main::listDir)
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    // dist bw dir and file
    private static String listDir (Path path){
        try{
            boolean isDir = Files.isDirectory(path);
            FileTime dateField = Files.getLastModifiedTime(path);
            LocalDateTime modDT = LocalDateTime.ofInstant(
                    dateField.toInstant(),
                    ZoneId.systemDefault()
            );
            return "%tD %tT %-5s %12s %s"
                    .formatted(modDT, modDT, (isDir ? "<DIR>" : ""), (isDir ? "" : Files.size(path)), path);
        }
        catch(IOException e){
            System.out.println("Whoops! Something went wrong with " + path);
            return path.toString();
        }
    }
}
