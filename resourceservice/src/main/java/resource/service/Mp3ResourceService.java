package resource.service;

import resource.dto.Mp3ResourceDto;

import java.util.List;

public interface Mp3ResourceService {
    Mp3ResourceDto save(byte[] file);

    byte[] getFileDataById(Long id);

    public List<Long> deleteByIds(List<Long> ids);
}
