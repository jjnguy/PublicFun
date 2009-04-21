package edu.cs319.server;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.cs319.client.IClient;
import edu.cs319.client.WindowClient;

@RunWith(JMock.class)
public class TestMockServer {
	private Mockery context;
	private IClient c;
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();
		c = new WindowClient();
	}

	@Test
	public void testNewClient() {
		final IServer server = context.mock(IServer.class);
		final String username = "jjnguy";
		final String roomanme = "room";
		context.checking(new Expectations() {
			{
				oneOf (server).addNewClient(c, username);
				oneOf (server).addNewCoLabRoom(username, roomanme, null);
				oneOf (server).joinCoLabRoom(username, roomanme, null);
			}
		});
		
		server.addNewClient(c, username);
		server.addNewCoLabRoom(username, roomanme, null);
	}

}
