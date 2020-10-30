package task;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.nio.file.FileSystems;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Archiver {

    public static void main(String[] args) throws IOException {

        if (args.length == 1 && ".zip".equals(args[0].substring(args[0].length() - 4))) {
            unzip(args[0]);
        } else {
            List<File> files = new ArrayList<>();

            for (String fileName : args) {
                files.add(new File(fileName));
            }

            FileOutputStream fos = new FileOutputStream("archived.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (File f : files) {
                if (f.isDirectory()) {
                    directoryToZip(f, f.getName(), zos);
                } else if (f.isFile()) {
                    fileToZip(f, zos);
                }
            }

            fos.close();
        }
    }

    public static void directoryToZip(File directory, String fileName, ZipOutputStream zos) throws IOException {
        if (directory.isDirectory()) {
            if (fileName.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(fileName));
            } else {
                zos.putNextEntry(new ZipEntry(fileName + "/"));
            }
            zos.closeEntry();
            File[] children = directory.listFiles();
            for (File childFile : Objects.requireNonNull(children)) {
                directoryToZip(childFile, fileName + "/" + childFile.getName(), zos);
            }
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(directory);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fileInputStream.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        fileInputStream.close();
        zos.close();
    }

    public static void fileToZip(File file, ZipOutputStream zos) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fileInputStream.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        fileInputStream.close();
        zos.close();
    }

    public static void unzip(String archiveName) throws IOException {
        ZipFile file = new ZipFile(archiveName);
        FileSystem fileSystem = FileSystems.getDefault();
        Enumeration<? extends ZipEntry> entries = file.entries();

        String targetDir = archiveName.substring(0, archiveName.length() - 4) + File.separator;
        Files.createDirectory(fileSystem.getPath(targetDir));

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                Files.createDirectories(fileSystem.getPath(targetDir + entry.getName()));
            } else {
                InputStream is = file.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(is);
                String fileName = targetDir + entry.getName();
                Path filePath = fileSystem.getPath(fileName);
                Files.createFile(filePath);
                FileOutputStream fileOutput = new FileOutputStream(fileName);
                while (bis.available() > 0) {
                    fileOutput.write(bis.read());
                }
                fileOutput.close();
            }
        }
    }
}



