package dev.dietermai.projectselect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.dietermai.projectselect.bots.SelectProjectBot;
import dev.dietermai.projectselect.bots.WorkbenchBot;

@RunWith(SWTBotJunit4ClassRunner.class)
public class FilterTest {

	private static WorkbenchBot workbenchBot;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		workbenchBot = WorkbenchBot.workbenchBot;
		workbenchBot.reset();
		workbenchBot.createProject("AlfaBravo1");
		workbenchBot.createProject("alfabravo2");
		workbenchBot.createProject("AlfaBravoCharlieDeltaEchoFoxtrotGolf1");
		workbenchBot.createProject("alfabravocharliedeltaechofoxtrotgolf2");
		workbenchBot.createProject("Alfa.Bravo.Charlie.Delta.Echo.Foxtrot.Golf1");
		workbenchBot.createProject("alfa.bravo.charlie.delta.echo.foxtrot.golf2");
		workbenchBot.createProject("Alfa-Bravo-Charlie-Delta-Echo-Foxtrot-Golf1");
		workbenchBot.createProject("alfa-bravo-charlie-delta-echo-foxtrot-golf2");
		workbenchBot.createProject("Alfa_Bravo_Charlie_Delta_Echo_Foxtrot_Golf1");
		workbenchBot.createProject("alfa_bravo_charlie_delta_echo_foxtrot_golf2");
		workbenchBot.createProject("Alfa Bravo Charlie Delta Echo Foxtrot Golf1");
		workbenchBot.createProject("alfa bravo charlie delta echo foxtrot golf2");
		workbenchBot.createProject("Alfa1Bravo2Charlie3Delta4Echo5Foxtrot6Golf1");
		workbenchBot.createProject("alfa1bravo2charlie3delta4echo5foxtrot6golf2");
		workbenchBot.createProject("ThisShouldNotBeDisplayed");

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		workbenchBot.reset();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialState() {
		workbenchBot.triggerProjectSelectBinding();

		assertTrue("After binding trigger, the dialog should be open.", workbenchBot.isSelectProjectDialogOpen());

		SelectProjectBot selectProjectBot = workbenchBot.getSelectProjectBot();
		assertFalse("No selection so OK should be disabeld.", selectProjectBot.isOkEnabled());
		assertTrue("Cancel should always be possible.", selectProjectBot.isCancelEnabled());
		assertTrue("At the start, the should be no project displayed.", selectProjectBot.listIsEmpty());

		selectProjectBot.cancel();
		assertFalse("After Cancel the dialog should be closed.", workbenchBot.isSelectProjectDialogOpen());
	}

	@Test
	public void testSimpleSearch() {
		workbenchBot.triggerProjectSelectBinding();
		SelectProjectBot selectProjectBot = workbenchBot.getSelectProjectBot();

		// Simple Search works in general
		selectProjectBot.setFilterText("alfa");
		verifyStandardMatch(selectProjectBot);

		// Simple Search even with upper case
		selectProjectBot.setFilterText("ALFA");
		verifyStandardMatch(selectProjectBot);

		// entries are removed if new search string does not match
		selectProjectBot.setFilterText("alfab");
		caseStartWithAlfab(selectProjectBot);

		// entries are added again if new search string does not match again
		selectProjectBot.setFilterText("alfa");
		verifyStandardMatch(selectProjectBot);
		
		// simple search only searches from the start
		selectProjectBot.setFilterText("bravo");
		verifyNoMatch(selectProjectBot);
		
		selectProjectBot.cancel();
	}
	
	@Test
	public void testWildcardSearchWithQustion() {
		workbenchBot.triggerProjectSelectBinding();
		SelectProjectBot selectProjectBot = workbenchBot.getSelectProjectBot();

		// ?-search with one ?
		selectProjectBot.setFilterText("?lfa");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("a?fa");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("al?a");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("alf?");
		verifyStandardMatch(selectProjectBot);

		// ?-search with two ?
		selectProjectBot.setFilterText("??fa");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("a??a");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("al??");
		verifyStandardMatch(selectProjectBot);
		
		// ?-search still needs to match the start
		selectProjectBot.setFilterText("?brvo");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("?rvo");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("b?rvo");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("br?o");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("brv?");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("?brvo?");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.cancel();
	}
	
	@Test
	public void testWildcardSearchWithStar() {
		workbenchBot.triggerProjectSelectBinding();
		SelectProjectBot selectProjectBot = workbenchBot.getSelectProjectBot();

		selectProjectBot.setFilterText("*alfa");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("**alfa");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("*bravo");
		verifyStandardMatch(selectProjectBot);
		
		// still needs to match start
		selectProjectBot.setFilterText("brvo*");
		verifyNoMatch(selectProjectBot);
		
		// also star matches everything
		selectProjectBot.setFilterText("*");
		verifyNoMatch(selectProjectBot);
				
		selectProjectBot.setFilterText("*");
		verifyNoMatch(selectProjectBot);
				
		selectProjectBot.cancel();
	}
	
	@Test
	public void testWildcardSearchWithStarAndQuestion() {
		workbenchBot.triggerProjectSelectBinding();
		SelectProjectBot selectProjectBot = workbenchBot.getSelectProjectBot();

		selectProjectBot.setFilterText("*a?fa");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("**al?a");
		verifyStandardMatch(selectProjectBot);

		selectProjectBot.setFilterText("?*?bravo");
		verifyStandardMatch(selectProjectBot);
		
		// still needs to match start
		selectProjectBot.setFilterText("?brvo*");
		verifyNoMatch(selectProjectBot);
		
		selectProjectBot.cancel();
	}
	
	@Test
	public void testPascalSearch() {
		workbenchBot.triggerProjectSelectBinding();
		SelectProjectBot selectProjectBot = workbenchBot.getSelectProjectBot();

		// just an A matches all a/A-s
		selectProjectBot.setFilterText("A");
		verifyStandardMatch(selectProjectBot);

		// standard Pascal
		selectProjectBot.setFilterText("AB");
		verifyInitialsAB(selectProjectBot);

		selectProjectBot.setFilterText("ABC");
		verifyInitialsABAndCo(selectProjectBot);

		selectProjectBot.setFilterText("ABCD");
		verifyInitialsABAndCo(selectProjectBot);
				
		selectProjectBot.setFilterText("ABCDE");
		verifyInitialsABAndCo(selectProjectBot);
		
		selectProjectBot.setFilterText("ABCDEF");
		verifyInitialsABAndCo(selectProjectBot);

		selectProjectBot.setFilterText("ABCDEFG");
		verifyInitialsABAndCo(selectProjectBot);

		// no combo with wild cards
		selectProjectBot.setFilterText("ABCDEFG?");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("ABCDEFG*");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("?ABCDEFG");
		verifyNoMatch(selectProjectBot);

		selectProjectBot.setFilterText("*ABCDEFG");
		verifyNoMatch(selectProjectBot);

		// letters need to be upper case
		selectProjectBot.setFilterText("abcdefg");
		verifyInitialsABAndCo(selectProjectBot);

		selectProjectBot.cancel();
	}
	
	private void verifyStandardMatch(SelectProjectBot selectProjectBot) {
		List<String> result = selectProjectBot.getFilterResult();
		assertEquals(14, result.size());
		assertTrue(Objects.toString(result), result.contains("AlfaBravo1"));
		assertTrue(Objects.toString(result), result.contains("AlfaBravoCharlieDeltaEchoFoxtrotGolf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa.Bravo.Charlie.Delta.Echo.Foxtrot.Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa-Bravo-Charlie-Delta-Echo-Foxtrot-Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa_Bravo_Charlie_Delta_Echo_Foxtrot_Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa Bravo Charlie Delta Echo Foxtrot Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa1Bravo2Charlie3Delta4Echo5Foxtrot6Golf1"));
		assertTrue(Objects.toString(result), result.contains("alfabravo2"));
		assertTrue(Objects.toString(result), result.contains("alfabravocharliedeltaechofoxtrotgolf2"));
		assertTrue(Objects.toString(result), result.contains("alfa.bravo.charlie.delta.echo.foxtrot.golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa-bravo-charlie-delta-echo-foxtrot-golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa_bravo_charlie_delta_echo_foxtrot_golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa bravo charlie delta echo foxtrot golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa1bravo2charlie3delta4echo5foxtrot6golf2"));
	}
	
	private void verifyInitialsAB(SelectProjectBot selectProjectBot) {
		List<String> result = selectProjectBot.getFilterResult();
		assertEquals(12, result.size());
		assertTrue(Objects.toString(result), result.contains("AlfaBravo1"));
		assertTrue(Objects.toString(result), result.contains("AlfaBravoCharlieDeltaEchoFoxtrotGolf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa.Bravo.Charlie.Delta.Echo.Foxtrot.Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa-Bravo-Charlie-Delta-Echo-Foxtrot-Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa_Bravo_Charlie_Delta_Echo_Foxtrot_Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa Bravo Charlie Delta Echo Foxtrot Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa1Bravo2Charlie3Delta4Echo5Foxtrot6Golf1"));
		assertTrue(Objects.toString(result), result.contains("alfa.bravo.charlie.delta.echo.foxtrot.golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa-bravo-charlie-delta-echo-foxtrot-golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa_bravo_charlie_delta_echo_foxtrot_golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa bravo charlie delta echo foxtrot golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa1bravo2charlie3delta4echo5foxtrot6golf2"));
	}
	
	private void verifyInitialsABAndCo(SelectProjectBot selectProjectBot) {
		List<String> result = selectProjectBot.getFilterResult();
		assertEquals(11, result.size());
		assertTrue(Objects.toString(result), result.contains("AlfaBravoCharlieDeltaEchoFoxtrotGolf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa.Bravo.Charlie.Delta.Echo.Foxtrot.Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa-Bravo-Charlie-Delta-Echo-Foxtrot-Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa_Bravo_Charlie_Delta_Echo_Foxtrot_Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa Bravo Charlie Delta Echo Foxtrot Golf1"));
		assertTrue(Objects.toString(result), result.contains("Alfa1Bravo2Charlie3Delta4Echo5Foxtrot6Golf1"));
		assertTrue(Objects.toString(result), result.contains("alfa.bravo.charlie.delta.echo.foxtrot.golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa-bravo-charlie-delta-echo-foxtrot-golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa_bravo_charlie_delta_echo_foxtrot_golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa bravo charlie delta echo foxtrot golf2"));
		assertTrue(Objects.toString(result), result.contains("alfa1bravo2charlie3delta4echo5foxtrot6golf2"));
	}
	
	private void caseStartWithAlfab(SelectProjectBot selectProjectBot) {
		List<String> result = selectProjectBot.getFilterResult();
		assertEquals(4, result.size());
		assertTrue(Objects.toString(result), result.contains("AlfaBravo1"));
		assertTrue(Objects.toString(result), result.contains("AlfaBravoCharlieDeltaEchoFoxtrotGolf1"));
		assertTrue(Objects.toString(result), result.contains("alfabravo2"));
		assertTrue(Objects.toString(result), result.contains("alfabravocharliedeltaechofoxtrotgolf2"));
	}
	
	private void verifyNoMatch(SelectProjectBot selectProjectBot) {
		List<String> result = selectProjectBot.getFilterResult();
		assertEquals(0, result.size());
	}
}
