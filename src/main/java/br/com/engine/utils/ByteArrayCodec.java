package br.com.engine.utils;

import java.nio.charset.StandardCharsets;

public final class ByteArrayCodec {
	
	private ByteArrayCodec() {
		//Classe não deve ser instanciada
	}
	
	public static byte[] serializeLong(long value) {
		byte[] bytes = new byte[8]; 	//8 bytes = 1 long
	    for (int i = 7; i >= 0; i--) { 	//preenche da direita à esquerda (mais significante primeiro)
	        bytes[i] = (byte) value; 	//extrai o byte menos significante de value
	        value >>= 8; 				//bit-shift 8 bits à direita
	    }
	    return bytes;
	}
	public static long deserializeLong(byte[] bytes) {
		if (bytes == null || bytes.length != 8) {
		    throw new IllegalArgumentException("Esperado 8 bytes, recebeu " + (bytes == null ? "null" : bytes.length));
		}
		long value = 0; 					//long vazio
	    for (int i = 0; i <= 7; i++) { 		//preenche da esquerda à direita (mais significante primeiro)
	        value <<= 8; 					//bit-shift 8 bits à esquerda
	        value |= (bytes[i] & 0xFFL); 	//traduz byte i de bytes. Mascara 0xFFL previne que extensão de sinal corrompa o resultado.
	    }
	    return value;
	}
	
	public static byte[] serializeString(String value) {
		return value.getBytes(StandardCharsets.UTF_8);
	}
	
	public static String deserializeString(byte[] bytes) {
	    return new String(bytes, StandardCharsets.UTF_8);
	}

	public static byte[] serializeInt(int value) {
		
		byte[] bytes = new byte[4]; 	//4 bytes = 1 int
	    for (int i = 3; i >= 0; i--) { 	//preenche da direita à esquerda (mais significante primeiro)
	        bytes[i] = (byte) value; 	//extrai o byte menos significante de value
	        value >>= 8; 				//bit-shift 8 bits à direita
	    }
	    return bytes;
	}
	public static int deserializeInt(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
		    throw new IllegalArgumentException("Esperado 4 bytes, recebeu " + (bytes == null ? "null" : bytes.length));
		}
		int value = 0; 						
	    for (int i = 0; i <=3; i++) { 		//preenche da esquerda à direita (mais significante primeiro)
	        value <<= 8; 					//bit-shift 8 bits à esquerda
	        value |= (bytes[i] & 0xFF); 	//traduz byte i de bytes. Mascara 0xFF previne que extensão de sinal corrompa o resultado.
	    }
	    return value;
	}
}
