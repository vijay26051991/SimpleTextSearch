package com.text.search.model;

/**
 * Metadata about file.
 *
 * @author vijaykumar.s 08/07/2019.
 */
public abstract class FileMetadata {
    private String fileName;
    private String directory;
    private Long size;
    private boolean fileExists;

    public FileMetadata(String fileName, String directory, Long size, boolean fileExists) {
        this.fileName = fileName;
        this.directory = directory;
        this.size = size;
        this.fileExists = fileExists;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectory() {
        return directory;
    }

    public Long getSize() {
        return size;
    }

    public boolean isFileExists() {
        return fileExists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileMetadata that = (FileMetadata) o;

        if (fileExists != that.fileExists) return false;
        if (!fileName.equals(that.fileName)) return false;
        if (!directory.equals(that.directory)) return false;
        return size.equals(that.size);
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + directory.hashCode();
        result = 31 * result + size.hashCode();
        result = 31 * result + (fileExists ? 1 : 0);
        return result;
    }
}
