package tr.edu.deu.efm.core.impl.cryptographer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityDecryptor;
import tr.edu.deu.efm.core.api.OperationResult;

public class AESDecryptor extends BaseAESCryptographer implements EntityDecryptor {

	@Override
	public OperationResult decrypt(String currentDir, String targetPath, char[] password,
			ConfirmationStrategy strategy) {
		Path target = Paths.get(currentDir).resolve(targetPath).normalize();

		if (!Files.exists(target) || !target.toString().endsWith(".enc")) {
			return new OperationResult(false, "valid encrypted file not found: " + targetPath,
					Collections.emptyList());
		}

		String originalName = target.getFileName().toString().replaceFirst("\\.enc$", "");
		Path decryptedTarget = target.getParent().resolve(originalName);

		if (Files.exists(decryptedTarget)) {
			boolean overwriteAllowed = strategy.askPermission(decryptedTarget.getFileName().toString());
			if (!overwriteAllowed) {
				return new OperationResult(false,
						"aborted. target file already exists: '" + decryptedTarget.getFileName() + "'",
						Collections.emptyList());
			}
		}

		try (FileInputStream fis = new FileInputStream(target.toFile())) {

			byte[] salt = new byte[SALT_LENGTH];
			byte[] iv = new byte[IV_LENGTH];

			if (fis.read(salt) != SALT_LENGTH || fis.read(iv) != IV_LENGTH) {
				return new OperationResult(false, "file is corrupted or not a valid encrypted file.",
						Collections.emptyList());
			}

			SecretKey secretKey = deriveKey(password, salt);
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

			try (CipherInputStream cis = new CipherInputStream(fis, cipher);
					FileOutputStream fos = new FileOutputStream(decryptedTarget.toFile())) {

				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = cis.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			}

			return new OperationResult(true, "successfully decrypted to '" + decryptedTarget.getFileName() + "'",
					Collections.singletonList(decryptedTarget.toString()));

		} catch (Exception e) {
			try {
				Files.deleteIfExists(decryptedTarget);
			} catch (IOException ignored) {
			}
			return new OperationResult(false,
					"decryption failed (wrong password or corrupted file) -> " + e.getMessage(),
					Collections.emptyList());
		} finally {
			if (password != null) {
				Arrays.fill(password, '0');
			}
		}
	}
}