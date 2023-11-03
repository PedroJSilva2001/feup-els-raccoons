package pt.up.fe.els2023.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GlobFinder {

    public static List<Path> getFilesGlob(String glob) throws IOException {
        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher("glob:" + glob);
        List<Path> files = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
            paths
                    .filter(matcher::matches)
                    .forEach(files::add);
        }

        return files;
    }
}
