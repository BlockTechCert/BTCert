package org.bham.btcert.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.bham.btcert.utils.merkle.BytesFormatter;

/**
 * 
 * @Title: CryptoUtil.java
 * @Package org.bham.btcert.utils
 * @Description: TODO
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
public class CryptoUtil {

	private static final String SHA_256 = "SHA-256";

	public static BytesFormatter hash(byte[] bytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(SHA_256);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		md.update(bytes);
		return new BytesFormatter(md.digest());
	}

	public static BytesFormatter hash(String text) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(SHA_256);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] hash = md.digest(text.getBytes(StandardCharsets.UTF_8));
		BytesFormatter bit256 = new BytesFormatter(hash);
		return bit256;
	}


	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	@SuppressWarnings("restriction")
	public static String base64Encode(byte[] bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	@SuppressWarnings("restriction")
	public static String base64Decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
			return new String(bt, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
