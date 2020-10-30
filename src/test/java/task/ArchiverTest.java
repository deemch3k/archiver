package task;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArchiverTest {

    @Test
    public void archiveWithFileShouldBeCreated() throws IOException {
        File file = new File("test.txt");
        boolean result = true;
        if (!file.exists()) {
            result = file.createNewFile();
        }

        if (result) {
            Archiver.fileToZip(file, new ZipOutputStream(new FileOutputStream("archiveShouldBeCreated.zip")));
            File archive = new File("archiveShouldBeCreated.zip");
            assertTrue(archive.exists());
            assertTrue(archive.length() > 0);
            archive.delete();
            file.delete();
        }
    }


    @Test
    public void directoryShouldBeArchived() throws IOException {
        File dir = new File("directoryShouldBeArchived");
        dir.mkdirs();

        Archiver.directoryToZip(dir, dir.getName(), new ZipOutputStream(new FileOutputStream("directoryShouldBeArchived.zip")));
        File archive = new File("directoryShouldBeArchived.zip");
        assertTrue(archive.exists());
        dir.delete();
        archive.delete();
    }

    @Test
    public void directoryWithFileShouldBeArchived() throws IOException {
        File dir = new File("directoryWithFileShouldBeArchived");
        dir.mkdirs();

        File file = new File(dir, "directoryWithFilesShouldBeArchived.txt");
        boolean result = true;
        if (!file.exists()) {
            result = file.createNewFile();
        }

        if (result) {
            assertEquals(1, Objects.requireNonNull(dir.listFiles()).length);
            Archiver.directoryToZip(dir, dir.getName(), new ZipOutputStream(new FileOutputStream("directoryWithFileShouldBeArchived.zip")));
            File archive = new File("directoryWithFileShouldBeArchived.zip");
            assertTrue(archive.exists());
            archive.delete();
        }

        file.delete();
        dir.delete();
    }

    @Test
    public void directoryWithEmptyFolderShouldBeArchived() throws IOException {
        File dir = new File("directoryWithEmptyFolderShouldBeArchived");
        dir.mkdirs();

        File dir2 = new File(dir, "directoryWithEmptyFolderShouldBeArchived2");
        dir2.mkdirs();

        assertEquals(1, Objects.requireNonNull(dir.listFiles()).length);

        Archiver.directoryToZip(dir, dir.getName(), new ZipOutputStream(new FileOutputStream("directoryWithEmptyFolderShouldBeArchived.zip")));
        File archive = new File("directoryWithEmptyFolderShouldBeArchived.zip");
        assertTrue(archive.exists());

        archive.delete();
        dir2.delete();
        dir.delete();
    }

    @Test
    public void directoryWithNonEmptyFolderShouldBeArchived() throws IOException {
        File dir = new File("directoryWithNonEmptyFolderShouldBeArchived");
        dir.mkdirs();

        File dir2 = new File(dir,"directoryWithNonEmptyFolderShouldBeArchived2");
        dir2.mkdirs();

        assertEquals(1, Objects.requireNonNull(dir.listFiles()).length);

        File file = new File(dir2,"directoryWithNonEmptyFolderShouldBeArchived.txt");


        boolean result = true;
        if (!file.exists()) {
            result = file.createNewFile();
        }

        assertEquals(1, Objects.requireNonNull(dir2.listFiles()).length);

        if(result){
            Archiver.directoryToZip(dir,dir.getName(),new ZipOutputStream(
                    new FileOutputStream("directoryWithNonEmptyFolderShouldBeArchived.zip")));
            File archive = new File("directoryWithNonEmptyFolderShouldBeArchived.zip");
            assertTrue(archive.exists());
            archive.delete();
        }
        file.delete();
        dir2.delete();
        dir.delete();
    }

    @Test
    public void archiveWithFileShouldBeUnzip() throws IOException {
        File file = new File("test.txt");
        boolean result = true;
        if (!file.exists()) {
            result = file.createNewFile();
        }

        if (result) {
            Archiver.fileToZip(file, new ZipOutputStream(new FileOutputStream("archiveWithFileShouldBeUnzip.zip")));
            File archive = new File("archiveWithFileShouldBeUnzip.zip");
            Archiver.unzip(archive.getName());
            File unzipFile = new File("archiveWithFileShouldBeUnzip/test.txt");

            assertEquals(file.length(), unzipFile.length());

            unzipFile.delete();
            file.delete();
            archive.delete();
            new File("archiveWithFileShouldBeUnzip").delete();
        }
    }

}