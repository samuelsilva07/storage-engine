package br.com.engine.disk;
import br.com.engine.record.Record;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import br.com.engine.record.RecordStatus;

import java.util.HashMap;

public class DiskManager implements IDiskManager {
	
    static final int HEADER_SIZE = 16;
    static final int VALUE_SIZE_OFFSET = 12;
	
	final File file;
	RandomAccessFile raf;
	boolean mapUpToDate = false;
	long activeBytes=0, inactiveBytes=0;
	
	public DiskManager(String folderPath) throws IOException {
		file = new File(folderPath, "data.dat");
		raf = new RandomAccessFile(file, "rw");
		if (raf.length()>0) {
			compactData();
		}
	}

	@Override
	public Long put(Record record, Long address) throws IOException {
		delete(address);
		raf.seek(raf.length());
	    Long newAddress= raf.getFilePointer();
		
		raf.writeInt(RecordStatus.ACTIVE.getStatus());
		raf.writeLong(record.getKey());
		raf.writeInt(record.getValueSize());
		raf.write((record.getValue()).getBytes(StandardCharsets.UTF_8));
		activeBytes += HEADER_SIZE+record.getValueSize();
		mapUpToDate = false;
		return newAddress;
	}

	@Override
	public String get(Long address) throws IOException {
		raf.seek(address+VALUE_SIZE_OFFSET);
		int size = raf.readInt();
		byte[] bytes = new byte[size];
		raf.readFully(bytes);
		String value = new String(bytes, StandardCharsets.UTF_8);
		return value;
	}

	@Override
	public void delete(Long address) throws IOException {
		if (address == null) {
			return;
		}
		raf.seek(address);
		if(raf.readInt() != RecordStatus.NOACTIVE.getStatus()) {
			raf.seek(address);
			raf.writeInt(RecordStatus.NOACTIVE.getStatus());
			mapUpToDate = false;
			raf.seek(address+VALUE_SIZE_OFFSET);
			int size = raf.readInt();
			inactiveBytes += HEADER_SIZE+size;
			activeBytes -= HEADER_SIZE+size;
			if (inactiveBytes * 4 >= activeBytes + inactiveBytes) { //quando 25% ou mais do arquivo for composto de registros marcados para remoção
				calculateActiveRecordsSize();
				compactData();
			}
		}
	}

	@Override
	public Map<Long, Long> getActiveRecords() throws IOException, CorruptedDataException  {
		Map<Long, Long> map = new HashMap<>();
		int status, valueSize;
		Long address=(long) 0, key;
		raf.seek(0);
		Long fileSize=raf.length();
		while(address<fileSize) {
			try {
				status=raf.readInt();
				key=raf.readLong();
				valueSize=raf.readInt();
				raf.seek(address+HEADER_SIZE+valueSize);
				if(raf.getFilePointer()>raf.length()) {
					throw new EOFException();
				}
				if(status == RecordStatus.ACTIVE.getStatus()) {
					map.put(key, address);
				}
				address=raf.getFilePointer();
			}
			catch(EOFException e) {
				throw new CorruptedDataException("Registro truncado: o tamanho declarado do conteúdo excede os bytes disponíveis no arquivo.");
			}
		}
		mapUpToDate = true;
		return map;
	}
	
	public boolean mapUpdateStatus() {
		return mapUpToDate;
	}
	
	private void compactData() throws IOException, CorruptedDataException {
		File tempFile = new File(file.getParent(), "dataTemp.dat");
		try (RandomAccessFile tempRAF = new RandomAccessFile(tempFile, "rw")) {
			tempRAF.setLength(0);
			int status, valueSize;
			Long address=(long) 0, key;
			raf.seek(0);
			Long fileSize=raf.length();
			while(address<fileSize) {
				try {
					status=raf.readInt();
					key=raf.readLong();
					valueSize=raf.readInt();
					byte[] recordData = new byte[valueSize];
					raf.readFully(recordData);
					if(status == 1) {
						tempRAF.writeInt(status);
						tempRAF.writeLong(key);
						tempRAF.writeInt(valueSize);
						tempRAF.write(recordData);
					}
					address=raf.getFilePointer();
				}
				catch(EOFException e) {
					throw new CorruptedDataException("Registro truncado: o tamanho declarado do conteúdo excede os bytes disponíveis no arquivo.");
				}
			}
		}
		raf.close();
		Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
		raf = new RandomAccessFile(file, "rw");
		mapUpToDate = false;
		calculateActiveRecordsSize();
		}
	
	private void calculateActiveRecordsSize() throws IOException {
		int status, valueSize;
		Long address=(long) 0;
		activeBytes = 0;
		inactiveBytes = 0;
		raf.seek(0);
		while(address<raf.length()) {
			try {
				status=raf.readInt();
				raf.seek(address+VALUE_SIZE_OFFSET);
				valueSize=raf.readInt();
				raf.seek(address+HEADER_SIZE+valueSize);
				if(status == 1) {
					activeBytes+=HEADER_SIZE+valueSize;
				}
				else{
					inactiveBytes+=HEADER_SIZE+valueSize;
				}
				address=raf.getFilePointer();
			}
			catch(EOFException e) {
				throw new CorruptedDataException("Registro truncado: o tamanho declarado do conteúdo excede os bytes disponíveis no arquivo.");
			}
		}
	}

}
