package br.com.bthirtyeight.data.dto;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownlaodUri;
    private String fileType;
    private long size;

    public UploadFileResponseDTO() {
    }

    public UploadFileResponseDTO(String fileName, String fileDownlaodUri,
                                 String fileType, long size) {
        this.fileName = fileName;
        this.fileDownlaodUri = fileDownlaodUri;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownlaodUri() {
        return fileDownlaodUri;
    }

    public void setFileDownlaodUri(String fileDownlaodUri) {
        this.fileDownlaodUri = fileDownlaodUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UploadFileResponseDTO that = (UploadFileResponseDTO) o;
        return size == that.size && Objects.equals(fileName, that.fileName) && Objects.equals(fileDownlaodUri, that.fileDownlaodUri) && Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileDownlaodUri, fileType, size);
    }
}
