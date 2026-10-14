package map;

import java.util.HashMap;
import java.util.Map;

public class MapManager implements IMapManager {
    private Map<Long, Long> addressMap;

    public MapManager() {
        this.addressMap = new HashMap<>();
    }

    public void putAddress(Long key, Long address) {
        addressMap.put(key, address);
    }

    @Override
    public Long getAddress(Long key) {
        return addressMap.get(key);
    }

    @Override
    public void removeAddress(Long key) {
        addressMap.remove(key);
    }

    public void setRecordMap(Map<Long, Long> addressMap) {
        this.addressMap = addressMap;
    }

}
