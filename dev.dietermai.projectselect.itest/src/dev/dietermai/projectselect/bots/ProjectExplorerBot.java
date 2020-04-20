package dev.dietermai.projectselect.bots;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

public class ProjectExplorerBot {
	private final SWTBotView viewBot;
	
	public ProjectExplorerBot(SWTBotView viewBot) {
		this.viewBot = viewBot;
	}
	
	public void close() {
		viewBot.close();
	}
	
	public ProjectExplorerBot focus() {
		viewBot.setFocus();
		return this;
	}
	
	public ProjectExplorerBot show() {
		viewBot.show();
		return this;
	}
	
	public ProjectExplorerBot selectProjects(String...items) {
		viewBot.bot().tree().select(items);
		return this;
	}
	
	public void deleteAllProjects() {
		if(containsProjects()) {
			viewBot.bot().tree().selectAll();
//			viewBot.bot().activeShell().pressShortcut(SWT.NONE, SWT.DEL);
			viewBot.bot().activeShell().pressShortcut(SWT.NONE, SWT.DEL, (char)0);
			SWTBotShell deleteResourcesDialog = viewBot.bot().shell("Delete Resources");
			deleteResourcesDialog.bot().checkBox().click();
			deleteResourcesDialog.bot().button("OK").click();
			viewBot.bot().sleep(1000);
		}
	}
		
	private boolean containsProjects() {
		try {
			return viewBot.bot().tree().getAllItems().length >0;
		}catch( WidgetNotFoundException wnfe) {
			return false;
		}
	}
	
	public List<String> getSelectedProjectNames(){
		List<String> selectedProjectNames = new ArrayList<>();
		for(SWTBotTreeItem projectItem : viewBot.bot().tree().getAllItems()) {
			if(projectItem.isSelected()) {
				selectedProjectNames.add(projectItem.getText());
			}
		}
		return selectedProjectNames;
	}
}
