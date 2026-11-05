package br.com.engine.map;

import java.io.IOException;
import java.util.Map;

public interface IMapManager {
    // Map<Long, Long> addressMap  ->  HashMap<chave, endereço do registro no arquivo>

    void putAddress(Long key, Long address);
    Long getAddress(Long key);
    void removeAddress(Long key);
    void setRecordMap(Map<Long, Long> recordMap);
    void loadFromDisk() throws IOException;
}
