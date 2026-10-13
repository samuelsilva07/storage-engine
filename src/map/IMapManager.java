package map;

import java.util.Map;

public interface IMapManager {
    // Map<Long, Long> map      TODO: definir qual implementação será utilizada

    public void putAddress(Long key, Long address);
    public Long getAddress(Long key);
    public void setRecordMap(Map<Long, Long> recordMap);
}
