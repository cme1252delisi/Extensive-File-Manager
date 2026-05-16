package tr.edu.deu.efm.core.impl.cryptographer;

import tr.edu.deu.efm.core.api.EntityEncryptor;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;

public class AESEncryptor extends BaseAESCryptographer implements EntityEncryptor {

	@Override
	public OperationResult encrypt(String currentDir, String targetPath, char[] password,
			ConfirmationStrategy strategy) {
		Path target = Paths.get(currentDir).resolve(targetPath).normalize();

		if (!Files.exists(target)) {
			return new OperationResult(false, "source does not exist: " + targetPath, Collections.emptyList());
		}

		if (Files.isDirectory(target)) {
			return new OperationResult(false, "target cannot be a directory. compress it first.",
					Collections.emptyList());
		}

		Path encryptedTarget = Paths.get(target.toString() + ".enc");

		if (Files.exists(encryptedTarget)) {
			boolean overwriteAllowed = strategy.askPermission(encryptedTarget.getFileName().toString());
			if (!overwriteAllowed) {
				return new OperationResult(false,
						"aborted. target file already exists: '" + encryptedTarget.getFileName() + "'",
						Collections.emptyList());
			}
		}

		try {
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[SALT_LENGTH];
			byte[] iv = new byte[IV_LENGTH];
			random.nextBytes(salt);
			random.nextBytes(iv);

			SecretKey secretKey = deriveKey(password, salt);
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

			try (FileOutputStream fos = new FileOutputStream(encryptedTarget.toFile());
					CipherOutputStream cos = new CipherOutputStream(fos, cipher);
					FileInputStream fis = new FileInputStream(target.toFile())) {

				fos.write(salt);
				fos.write(iv);

				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					cos.write(buffer, 0, bytesRead);
				}
			}

			return new OperationResult(true, "successfully encrypted to '" + encryptedTarget.getFileName() + "'",
					Collections.singletonList(encryptedTarget.toString()));

		} catch (Exception e) {
			try {
				Files.deleteIfExists(encryptedTarget);
			} catch (IOException ignored) {
			}
			return new OperationResult(false, "encryption failed -> " + e.getMessage(), Collections.emptyList());
		} finally {
			if (password != null) {
				Arrays.fill(password, '0');
			}
		}
	}
}