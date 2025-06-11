package resource.service;

public interface CloudStorageService {
    void uploadFile(String key, byte[] data);

    byte[] downloadFile(String key);

    void deleteFile(String key);
}
