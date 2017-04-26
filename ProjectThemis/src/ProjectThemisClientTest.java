
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ProjectThemisClientTest {
	@Test
	public void testClient() throws IOException{
			
		String[] inputs0 = new String[0];
		String[] inputs1 = new String[1];
		String[] inputs2 = new String[2];
		String[] inputs3 = new String[3];
		inputs1[0] = "themis.servegame.com";
		inputs2[0] = "";
		inputs2[1] = "";
		inputs3[0] = "themis.servegame.com";
		inputs3[1] = "";
		inputs3[2] = "";
		ProjectThemisClient.main(inputs1);
		assertEquals(ProjectThemisClient.user,"user");
		assertEquals(ProjectThemisClient.pass,"pass");
		assertEquals(ProjectThemisClient.hostName,"themis.servegame.com");
		
		ProjectThemisClient.main(inputs2);
		assertEquals(ProjectThemisClient.user,"");
		assertEquals(ProjectThemisClient.pass,"");
		assertEquals(ProjectThemisClient.hostName,"themis.servegame.com");
		
		ProjectThemisClient.main(inputs3);
		assertEquals(ProjectThemisClient.user,"");
		assertEquals(ProjectThemisClient.pass,"");
		assertEquals(ProjectThemisClient.hostName,"themis.servegame.com");
		
		ProjectThemisClient.main(inputs0);
		assertEquals(ProjectThemisClient.user,"user");
		assertEquals(ProjectThemisClient.pass,"pass");
		assertEquals(ProjectThemisClient.hostName,"themis.servegame.com");
		
		//to use the above tests, place a /* at line 54  and
		// a */ at line 162 in ProjectThemisClient
	}
}
