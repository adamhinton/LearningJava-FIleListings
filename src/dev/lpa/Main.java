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

        System.out.println("-----------");
        // Walk is recursive if you specify a depth >1
        // Files.walk and Files.list return a stream
        try (Stream<Path> paths = Files.walk(path, 2)) {
            paths
                    .filter(Files::isRegularFile)
                    // dist bw dir and file
                    .map(Main::listDir)
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("-----------");
        // Walk is recursive if you specify a depth >1
        // Files.walk and Files.list return a stream
        try (Stream<Path> paths = Files.find(path, Integer.MAX_VALUE,
                (p, attr) -> attr.isRegularFile() && attr.size() > 300
                )) {
            paths
                    // dist bw dir and file
                    .map(Main::listDir)
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("========Directory Stream=======");

        try(var dirs = Files.newDirectoryStream(path)){
            dirs.forEach(d -> System.out.println(Main.listDir(d)));
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }

    }

    // dist bw dir and file
    private static String listDir(Path path) {
        try {
            boolean isDir = Files.isDirectory(path);
            FileTime dateField = Files.getLastModifiedTime(path);
            LocalDateTime modDT = LocalDateTime.ofInstant(
                    dateField.toInstant(),
                    ZoneId.systemDefault()
            );
            return "%tD %tT %-5s %12s %s"
                    .formatted(modDT, modDT, (isDir ? "<DIR>" : ""), (isDir ? "" : Files.size(path)), path);
        } catch (IOException e) {
            System.out.println("Whoops! Something went wrong with " + path);
            return path.toString();
        }
    }
}
