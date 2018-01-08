package org.bham.btcert.utils.merkle;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 
 * @Title: BytesFormatter.java
 * @Package org.bham.btcert.utils.merkle
 * @Description: BytesFormatter
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
public class BytesFormatter {

	private byte[] _bytes;

	public BytesFormatter(byte[] bytes) {
		// Preconditions.checkArgument(bytes.length == 32);
		this._bytes = bytes;
	}

	public byte[] getBytes() {
		return this._bytes;
	}

	public String toHex() {
		return Hex.encodeHexString(this._bytes);
	}

	public String toBinary() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this._bytes.length; i++) {
			Byte b = this._bytes[i];
			sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return Base64.encodeBase64URLSafeString(this._bytes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(_bytes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BytesFormatter other = (BytesFormatter) obj;
		if (!Arrays.equals(_bytes, other._bytes))
			return false;
		return true;
	}

}
