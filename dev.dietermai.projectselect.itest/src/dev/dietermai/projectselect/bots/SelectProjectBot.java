package dev.dietermai.projectselect.bots;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

public class SelectProjectBot {
	private final SWTBotShell shell;
	private final SWTBot bot;
	
	public SelectProjectBot(SWTBotShell shell) {
		this.shell = shell;
		this.bot = shell.bot();
		
	}
	
	public boolean isOkEnabled() {
		return isButtonEnabeld("OK");
	}

	public boolean isCancelEnabled() {
		return isButtonEnabeld("Cancel");
	}
	
	public void ok() {
		clickButton("OK");
	}
	
	public void cancel() {
		clickButton("Cancel");
	}
	
	public SelectProjectBot setFilterText(String newText) {
		getFilterField().setText(newText);
		bot.sleep(200);// changes can take a bit till they are displayed
		return this;
	}
	
	public SelectProjectBot selectEntry(int...indices) {
		getTable().select(indices);
		return this;
	}
	
	public List<String> getSelectedProjectNames(){
		final int tableCount = getTable().selection().rowCount();
		List<String> resultList = new ArrayList<>(tableCount);

		for(int i = 0; i < tableCount; i++) {
			resultList.add(getTable().selection().get(i).get(i));
		}
		return resultList;
	}
	
	public List<String> getFilterResult(){
		final int tableCount = getTable().rowCount();
		List<String> resultList = new ArrayList<>(tableCount);
		for(int i = 0; i < tableCount; i++) {
			resultList.add(getTable().getTableItem(i).getText());
		}
		return resultList;
	}
	
//	public SelectProjectBot help() {
//		
//	}
	
	public boolean listIsEmpty() {
		return getTable().rowCount() == 0;
	}
	
	private SWTBotButton getButton(String text) {
		return bot.button(text);
	}
	
	private boolean isButtonEnabeld(String text) {
		return getButton(text).isEnabled();
	}
	
	private SWTBotTable getTable() {
		return bot.table();
	}
	
	private SWTBotText getFilterField() {
		return bot.text();
	}
	
	private SelectProjectBot clickButton(String text) {
		if(isButtonEnabeld(text)) {
			getButton(text).click();
		}else {
			fail("Button '"+text+"' is not enabled");
		}
		return this;
	}
}
