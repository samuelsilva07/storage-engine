package br.com.engine.map;

import br.com.engine.disk.IDiskManager;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class MapManager implements IMapManager {
    private Map<Long, Long> addressMap; // chave -> endereço do registro no arquivo
    private final IDiskManager diskManager;

    public MapManager(IDiskManager diskManager) throws IOException {
        this.diskManager = diskManager;
        this.addressMap = new HashMap<>();
        loadFromDisk();
    }

    // monta a tabela com os registros ativos do disco (usado na inicialização)
    @Override
    public void loadFromDisk() throws IOException {
        setRecordMap(diskManager.getActiveRecords());
    }

    @Override
    public void putAddress(Long key, Long address) {
        syncIfNeeded();
        addressMap.put(key, address);
    }

    // retorna null se a chave não existir (not_found)
    @Override
    public Long getAddress(Long key) {
        syncIfNeeded();
        return addressMap.get(key);
    }

    @Override
    public void removeAddress(Long key) {
        syncIfNeeded();
        addressMap.remove(key);
    }

    @Override
    public void setRecordMap(Map<Long, Long> recordMap) {
        this.addressMap = (recordMap != null) ? recordMap : new HashMap<>();
    }

    public int size() {
        return addressMap.size();
    }

    // recarrega a tabela quando o DiskManager compacta o arquivo (endereços mudam)
    private void syncIfNeeded() {
        if (!diskManager.getMapUpdateStatus()) {
            try {
                loadFromDisk();
            } catch (IOException e) {
                throw new UncheckedIOException("Erro ao recarregar a tabela de endereços do disco", e);
            }
        }
    }
}
