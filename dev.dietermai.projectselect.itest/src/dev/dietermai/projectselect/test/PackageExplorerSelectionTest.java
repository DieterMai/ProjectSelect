package dev.dietermai.projectselect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dev.dietermai.projectselect.bots.PackageExplorerBot;
import dev.dietermai.projectselect.bots.SelectProjectBot;
import dev.dietermai.projectselect.bots.WorkbenchBot;

public class PackageExplorerSelectionTest {

	private static WorkbenchBot workbenchBot;
	private PackageExplorerBot packageExplorer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		workbenchBot = WorkbenchBot.workbenchBot;
		workbenchBot.reset();
		workbenchBot.createProject("Project01");
		workbenchBot.createProject("Project02");
		workbenchBot.createProject("Project03");
		workbenchBot.createProject("Project04");
		workbenchBot.createProject("Project05");
		workbenchBot.createProject("Project06");
		workbenchBot.createProject("Project07");
		workbenchBot.createProject("Project08");
		workbenchBot.createProject("Project09");
		workbenchBot.createProject("Project10");
		workbenchBot.createProject("Project11");
		workbenchBot.createProject("Project12");
		workbenchBot.createProject("Project13");
		workbenchBot.createProject("Project14");
		workbenchBot.createProject("Project15");
		workbenchBot.createProject("Project16");
		workbenchBot.createProject("Project17");
		workbenchBot.createProject("Project18");
		workbenchBot.createProject("Project19");
		workbenchBot.createProject("Project20");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		workbenchBot.reset();
		packageExplorer = workbenchBot.openPackageExplorerView();
		packageExplorer.show();
	}

	@After
	public void tearDown() throws Exception {
		packageExplorer.close();
	}

	@Test
	public void testCancelDoesNotSelectProject() {
		workbenchBot.triggerProjectSelectBinding().cancel();
		List<String> selectedProjectNames = packageExplorer.getSelectedProjectNames();
		assertTrue("Selected Prjects: " + Objects.toString(selectedProjectNames), selectedProjectNames.size() == 0);
	}

	@Test
	public void testCancelDoesNotChangePrevSelection() {
		packageExplorer.selectProjects("Project02");

		workbenchBot.triggerProjectSelectBinding().cancel();
		List<String> selectedProjectNames = packageExplorer.getSelectedProjectNames();
		assertTrue("Selected Prjects: " + Objects.toString(selectedProjectNames), selectedProjectNames.size() == 1);
		assertEquals("Project02", selectedProjectNames.get(0));
	}

	@Test
	public void testOneSelection() {
		packageExplorer.selectProjects("Project02");
		workbenchBot.triggerProjectSelectBinding().setFilterText("P").selectEntry(5).ok();
		List<String> selectedProjectNames = packageExplorer.getSelectedProjectNames();
		assertTrue("Selected Prjects: " + Objects.toString(selectedProjectNames), selectedProjectNames.size() == 1);
		assertEquals("Project06", selectedProjectNames.get(0));
	}

	@Test
	public void testMultiSelection() {
		packageExplorer.selectProjects("Project02");
		workbenchBot.triggerProjectSelectBinding().setFilterText("P").selectEntry(5, 6, 7, 10, 15)
		.ok();
		List<String> selectedProjectNames = packageExplorer.getSelectedProjectNames();
		assertTrue("Selected Prjects: " + Objects.toString(selectedProjectNames), selectedProjectNames.size() == 5);
		assertTrue(Objects.toString(selectedProjectNames), selectedProjectNames.contains("Project06"));
		assertTrue(Objects.toString(selectedProjectNames), selectedProjectNames.contains("Project07"));
		assertTrue(Objects.toString(selectedProjectNames), selectedProjectNames.contains("Project08"));
		assertTrue(Objects.toString(selectedProjectNames), selectedProjectNames.contains("Project11"));
		assertTrue(Objects.toString(selectedProjectNames), selectedProjectNames.contains("Project16"));
	}
}
