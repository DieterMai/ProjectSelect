package dev.dietermai.projectselect.test;

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.dietermai.projectselect.bots.WorkbenchBot;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EmptyWorkspaceTest {

	private static WorkbenchBot workbenchBot;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		workbenchBot = WorkbenchBot.workbenchBot;
		workbenchBot.reset();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDefaultKeyBindingOpensDialog() {
		// TODO
		
		
	}
}
