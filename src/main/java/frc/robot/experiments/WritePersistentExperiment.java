package frc.robot.experiments;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import edu.wpi.first.wpilibj.Filesystem;

public class WritePersistentExperiment {
    public static final String dir = "persist";

    public static void ensureDirExists() {
        File dir = new File(Filesystem.getOperatingDirectory().toString() + File.separator + "persist");
        if (dir.exists() && !dir.isDirectory())
            dir.delete();
        if (!dir.exists())
            dir.mkdir();
        if (!dir.exists())
            throw new RuntimeException(dir.toString() + " doesn't exist and failed to be created! Dying.");
        if (!dir.isDirectory())
            throw new RuntimeException(dir.toString() + " still isn't a directory after telling it to be! Giving up.");
    }

    public static void ensureFileExists(String filename) {
        ensureDirExists();
        File file = new File(dir + File.separator + filename);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
    }

    public static void overwrite(String filename, String newContent) {
        ensureFileExists(filename);
        File file = new File(dir + File.separator + filename);
        file.setWritable(true);
        try {
            Files.write(file.toPath(), newContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        file.setWritable(false);
    }

    public static void append(String filename, String additionalContent) {
        ensureFileExists(filename);
        File file = new File(dir + File.separator + filename);
        file.setWritable(true);
        try {
            Files.write(file.toPath(), additionalContent.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        file.setWritable(false);
    }

    public static String read(String filename) {
        ensureFileExists(filename);
        File file = new File(dir + File.separator + filename);
        try {
            return Files.readString(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static void boop(String filename) {
        if (read(filename) == "") {
            overwrite(filename, "hello");
            append(filename, " world");
        } else {
            append(filename, "\nyeet!");
        }
    }

    public static void runExperiment() {
        boop("the-file");
        System.out.println(read("the-file"));
    }
}
