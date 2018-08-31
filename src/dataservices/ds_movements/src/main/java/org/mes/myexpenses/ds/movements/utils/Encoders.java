package org.mes.myexpenses.ds.movements.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

@Slf4j
@Component
public class Encoders {

	private final ThreadLocal<Random> random = new ThreadLocal<Random>();

	public Boolean validatePwd(String passwordEncoded, String password) {
		String hashedAndSalted = passwordEncoded;
		String salt = hashedAndSalted.split(",")[1];
		if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
			log.error("Submitted password is not a match");
			return false;
		}

		return true;
	}

	public String makePasswordHash(String password) {
		return makePasswordHash(password, Integer.toString(getRandom().nextInt()));
	}

	public String makePasswordHash(String password, String salt) {
		try {
			String saltedAndHashed = password + "," + salt;
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(saltedAndHashed.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
			return encoder.encode(hashedBytes) + "," + salt;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 is not available", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
		}
	}

	private Random getRandom() {
		Random result = random.get();
		if (result == null) {
			result = new Random();
			random.set(result);
		}
		return result;
	}

}