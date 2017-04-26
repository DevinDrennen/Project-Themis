import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordAuthenticationTest {

	/**
	 * Both the methods for PasswordAuthentication, authentication and hash,
	 * require the other to be used for it to be useful.
	 */
	@Test
	public void testAuthenticate() {
		PasswordAuthentication pw = new PasswordAuthentication();
		char[] password1 = "password".toCharArray();
		char[] password2 = " password".toCharArray();
		String hash1 = pw.hash(password1);
		String hash2 = pw.hash(password2);
		
		assertTrue(pw.authenticate(password1, hash1));
		assertTrue(pw.authenticate(password2, hash2));
		assertFalse(pw.authenticate(password1, hash2));
		assertFalse(pw.authenticate(password2, hash1));
	}

}
