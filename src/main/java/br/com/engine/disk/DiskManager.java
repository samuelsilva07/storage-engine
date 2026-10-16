package br.com.engine.disk;
import br.com.engine.record.Record;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import br.com.engine.record.RecordStatus;

import java.util.HashMap;

import br.com.engine.utils.ByteArrayCodec;
public class DiskManager implements IDiskManager {

	RandomAccessFile file;
	
	public DiskManager(String filePath) throws IOException {
		this.file = new RandomAccessFile(filePath, "rw");
	}

	@Override
	public Long put(Record record, Long address) throws IOException {
		delete(address);
		file.seek(file.length());
	    Long newAddress= file.getFilePointer();
		
		file.writeInt(RecordStatus.ACTIVE.getStatus());
		file.write(ByteArrayCodec.serializeLong(record.getKey()));
		file.write(ByteArrayCodec.serializeInt(record.getValueSize()));
		file.write(ByteArrayCodec.serializeString(record.getValue()));
		return newAddress;
	}

	@Override
	public String get(Long address) throws IOException {
		file.seek(address);
		file.skipBytes(12);
		int size = file.readInt();
		byte[] bytes = new byte[size];
		file.readFully(bytes);
		String value = ByteArrayCodec.deserializeString(bytes);
		return value;
	}

	@Override
	public void delete(Long address) throws IOException {
		if (address == null) {
			return;
		}
		file.seek(address);
		file.writeInt(RecordStatus.NOACTIVE.getStatus());
	}

	@Override
	public Map<Long, Long> getActiveRecords() throws IOException, InvalidFileAddressException  {
		Map<Long, Long> map = new HashMap<>();
		int status, valueSize;
		Long address=(long) 0, key;
		file.seek(0);
		Long fileSize=file.length();
		while(address<fileSize) {
				status=file.readInt();
				key=file.readLong();
				valueSize=file.readInt();
				if(status == 1) {
					map.put(key, address);
				}
				if(address+16+valueSize > fileSize) {
					throw new InvalidFileAddressException("Registro truncado: o tamanho declarado do conteúdo excede os bytes disponíveis no arquivo.");
				}
				file.skipBytes(valueSize);
				address=file.getFilePointer();
		}
		return map;
	}

}
