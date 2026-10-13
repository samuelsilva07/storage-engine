package map;

import java.util.Map;

public interface IMapManager {
    // Map<Long, Long> map      TODO: definir qual implementação será utilizada

    void putAddress(Long key, Long address);
    Long getAddress(Long key);
    void removeAddress(Long key);
    void setRecordMap(Map<Long, Long> recordMap);
}
