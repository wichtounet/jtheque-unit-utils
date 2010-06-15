package org.jtheque.utils.unit.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

/*
 * This file is part of JTheque.
 * 	   
 * JTheque is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License. 
 *
 * JTheque is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JTheque.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * A Unit utility class to test file operations. This class is made to facilitate the tests of file operations on JUnit
 * tests. This class emulate a simple file system in a temporary folder.
 *
 * @author Baptiste Wicht
 */
public final class FileUnit {
    private static final File ROOT_FOLDER = new File(System.getProperty("java.io.tmpdir"), "unit");

    /**
     * Utility class, not instanciable.
     */
    private FileUnit() {
        super();
    }

    //Utility methods

    /**
     * Init the test file system.
     */
    public static void initTestFileSystem() {
        createFolderIfNecessary(ROOT_FOLDER);
    }

    /**
     * Clear the test file system. All the created files will be deleted.
     */
    public static void clearTestFileSystem() {
        deleteIfNecessary(ROOT_FOLDER);
    }

    /**
     * Return the path to the root folder of the test file system.
     *
     * @return The path to the root folder of the test file system.
     */
    public static String getRootFolder() {
        return ROOT_FOLDER.getAbsolutePath();
    }

    /**
     * Return the <code>File</code> object of a path on the test file system.
     *
     * @param path The path on the test file system.
     *
     * @return Return the corresponding <code>File</code> object.
     */
    public static File getFile(String path) {
        return new File(ROOT_FOLDER, path);
    }

    /**
     * Return the <code>File</code> object of the path in the specified folder on the test file system.
     *
     * @param folder The path to the parent folder.
     * @param path   The path of the file.
     *
     * @return The corresponding <code>File</code> object,
     */
    public static File getFile(String folder, String path) {
        return new File(getFile(folder), path);
    }

    /**
     * Return the real path of the path on the test file system.
     *
     * @param path The path on the test file system.
     *
     * @return The corresponding path on the real file system.
     */
    public static String getPath(String path) {
        return getFile(path).getAbsolutePath();
    }

    /**
     * Return the real path of the path on the specified folder on the test file system.
     *
     * @param folder The path to the parent folder.
     * @param path   The path of the file.
     *
     * @return The corresponding path on the real file system.
     */
    public static String getPath(String folder, String path) {
        return getFile(folder, path).getAbsolutePath();
    }

    /**
     * Return an <code>InputStream</code> to the path.
     *
     * @param path The path on the test file system.
     *
     * @return The <code>InputStream</code> to the path. This stream is still buffered.
     *
     * @see InputStream
     */
    public static InputStream getInputStream(String path) {
        try {
            return new BufferedInputStream(new FileInputStream(getPath(path)));
        } catch (FileNotFoundException e) {
            fail("Unable to open stream to {" + path + " } due to " + e.getMessage());
        }

        return null;
    }

    /**
     * Add a file to the test file system.
     *
     * @param path The path to the file on the test file system.
     */
    public static void addFile(String path) {
        createFileIfNecessary(getFile(path));
    }

    /**
     * Add a file to the test file system in the specified folder.
     *
     * @param folder The path to the folder on the test file system.
     * @param path   The path to the file on the test file system.
     */
    public static void addFile(String folder, String path) {
        createFileIfNecessary(getFile(folder, path));
    }

    /**
     * Add a folder to the test file system.
     *
     * @param path The path to the folder on the test file system.
     */
    public static void addFolder(String path) {
        createFolderIfNecessary(getFile(path));
    }

    /**
     * Add a file to the test file system in the specified folder.
     *
     * @param folder The path to the folder on the test file system.
     * @param path   The path to the file on the test file system.
     */
    public static void addFolder(String folder, String path) {
        createFolderIfNecessary(getFile(folder, path));
    }

    /**
     * Set the content of the file specified by the path.
     *
     * @param path    The path to the file on the test file system.
     * @param content The content of the file.
     */
    public static void setContent(String path, String content) {
        assertIsFile(path);

        write(content, getFile(path));
    }

    /**
     * Set the content of the file specified by the path.
     *
     * @param folder  The path to the folder on the test file system.
     * @param path    The path to the file on the test file system.
     * @param content The content of the file.
     */
    public static void setContent(String folder, String path, String content) {
        assertIsFile(folder, path);

        write(content, getFile(folder, path));
    }

    //Assert methods

    /**
     * Assert that the test file system has been correctly created.
     */
    public static void assertInitOK() {
        assertTrue(ROOT_FOLDER.exists());
    }

    /**
     * Assert that the file referenced by the specified path exists.
     *
     * @param path The path to the file to test.
     */
    public static void assertFileExists(String path) {
        assertTrue("The file {" + path + "} must exists", getFile(path).exists());
    }

    /**
     * Assert that the file referenced by the specified path in the specified folder exists.
     *
     * @param folder The path to the folder on the test file system.
     * @param path   The path to the file to test.
     */
    public static void assertFileExists(String folder, String path) {
        assertTrue("The file {" + folder + '/' + path + "} must not exists", getFile(folder, path).exists());
    }

    /**
     * Assert that the file referenced by the specified path doesn't exists.
     *
     * @param path The path to the file to test.
     */
    public static void assertFileNotExists(String path) {
        assertFalse("The file {" + path + "} must not exists", getFile(path).exists());
    }

    /**
     * Assert that the folder contains the specified file.
     *
     * @param folder The path the folder in the test file system to test.
     * @param file   The file that we must found in the specified folder.
     */
    public static void assertDirectoryContains(String folder, String file) {
        assertIsDirectory(folder);

        boolean found = false;

        for (File f : getFile(folder).listFiles()) {
            if (f.getName().equals(file)) {
                found = true;
                break;
            }
        }

        assertTrue("The directory must contains the file " + file, found);
    }

    /**
     * Assert that the folder contains the specified number of files.
     *
     * @param folder The path to the folder on the test file system.
     * @param files  The expected number of files.
     */
    public static void assertDirectorySize(String folder, int files) {
        assertIsDirectory(folder);

        int size = getFile(folder).listFiles().length;

        assertEquals("The directory must contains " + files + " files but contains " + size + " files", files, size);
    }

    /**
     * Assert that the file referenced by the specified path is a directory.
     *
     * @param file The path to the file on the test file system.
     */
    public static void assertIsDirectory(String file) {
        assertTrue(file + " must be a directory", getFile(file).isDirectory());
    }

    /**
     * Assert that the file referenced by the specified path is a file.
     *
     * @param file The path to the file on the test file system.
     */
    public static void assertIsFile(String file) {
        assertTrue(file + " must be a file", getFile(file).isFile());
    }

    /**
     * Assert that the file referenced by the specified path is a file.
     *
     * @param folder The path to the parent folder on the test file system.
     * @param file   The path to the file on the test file system.
     */
    public static void assertIsFile(String folder, String file) {
        assertTrue(folder + '/' + file + " must be a file", getFile(folder, file).isFile());
    }

    /**
     * Assert that the content of the file referenced by the specified path equals the specified content.
     *
     * @param path            The path to the file in test file system.
     * @param expectedContent The expected content.
     */
    public static void assertFileContentEquals(String path, String expectedContent) {
        assertIsFile(path);

        String content = getContent(getFile(path));

        assertEquals("The content of the must be " + expectedContent + " but it's content is " + content, expectedContent, content);
    }

    /**
     * Assert that the size of the file is the expected size.
     *
     * @param path The path to the file on the test file system.
     * @param size The expected size.
     */
    public static void assertFileSizeEquals(String path, long size) {
        assertIsFile(path);

        long actualSize = getFile(path).length();

        assertEquals("The size of the file must be " + size + " but is actually " + actualSize, size, actualSize);
    }

    /**
     * Assert that the size of the file referenced by the specified path is lower than the specified size.
     *
     * @param path The path to the file on the test file system.
     * @param size The size.
     */
    public static void assertFileSizeLowerThan(String path, long size) {
        assertIsFile(path);

        long actualSize = getFile(path).length();

        if (actualSize >= size) {
            fail(path + " must be lower than " + size + " but was actually " + actualSize);
        }
    }

    /**
     * Assert that the size of the file referenced by the specified path is greater than the specified size.
     *
     * @param path The path to the file on the test file system.
     * @param size The size.
     */
    public static void assertFileSizeGreaterThan(String path, long size) {
        assertIsFile(path);

        long actualSize = getFile(path).length();

        if (actualSize <= size) {
            fail(path + " must be greater than " + size + " but was actually " + actualSize);
        }
    }

    /**
     * Assert that the zip file referenced by the specified path contains all the specified files.
     *
     * @param path  The path to the zip file in the test file system.
     * @param files All the files that the zip must contains.
     */
    public static void assertZipContains(String path, String... files) {
        if (files.length == 0) {
            return;
        }

        Collection<String> entries = new ArrayList<String>(10);

        ZipInputStream zis = new ZipInputStream(getInputStream(path));

        try {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                entries.add(entry.getName());
            }
        } catch (FileNotFoundException e) {
            fail("Unable to read zip due to {" + e.getMessage() + '}');
        } catch (IOException e) {
            fail("Unable to read zip due to {" + e.getMessage() + '}');
        } finally {
            try {
                zis.close();
            } catch (IOException e) {
                fail("Unable to read zip due to {" + e.getMessage() + '}');
            }
        }

        for (String file : files) {
            assertTrue("The zip must contains " + file, entries.contains(file));
        }
    }

    //Private methods

    /**
     * Create the specified file if it not exists.
     *
     * @param file The file to create.
     */
    private static void createFileIfNecessary(File file) {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    fail("Unable to create the file {" + file.getAbsolutePath() + " }");
                }
            } catch (IOException e) {
                fail("Unable to create the file {" + file.getAbsolutePath() + " } due to " + e.getMessage());
            }
        }
    }

    /**
     * Create the specified folder if it not exists.
     *
     * @param folder The folder to create.
     */
    private static void createFolderIfNecessary(File folder) {
        if (!folder.exists() && !folder.mkdirs()) {
            fail("Unable to create the folder {" + folder.getAbsolutePath() + " }");
        }
    }

    /**
     * Delete the file if it exists.
     *
     * @param file The file of folder to delete.
     */
    private static void deleteIfNecessary(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteIfNecessary(f);
                }
            }

            if (!file.delete()) {
                fail("Unable to delete the file {" + file.getAbsolutePath() + " }");
            }
        }
    }

    /**
     * Return the content of the file in a String.
     *
     * @param file The file to get the content from.
     *
     * @return The content of the file.
     */
    private static String getContent(File file) {
        StringBuilder builder = new StringBuilder(100);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line;

            boolean first = true;

            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append('\n');
                }

                builder.append(line);
            }
        } catch (FileNotFoundException e) {
            fail("Unable to read the file {" + file.getAbsolutePath() + " } due to " + e.getMessage());
        } catch (IOException e) {
            fail("Unable to read the file {" + file.getAbsolutePath() + " } due to " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    fail("Unable to read the file {" + file.getAbsolutePath() + " } due to " + e.getMessage());
                }
            }
        }

        return builder.toString();
    }

    /**
     * Write the specified content to the specified file.
     *
     * @param content The content to write.
     * @param file    The file to write the content in.
     */
    private static void write(String content, File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));

            writer.write(content);
        } catch (IOException e) {
            fail("Unable to write the file {" + file.getAbsolutePath() + " } due to " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("Unable to write the file {" + file.getAbsolutePath() + " } due to " + e.getMessage());
                }
            }
        }
    }
}