package dev.dietermai.projectselect.bots;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
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
