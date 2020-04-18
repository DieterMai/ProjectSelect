package dev.dietermai.projectselect.bots;

import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.SWTBotInfo;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public enum WorkbenchBot {
	workbenchBot;
	
	private static final String ID_VIEW_INTRO = "org.eclipse.ui.internal.introview";
	private static final String ID_VIEW_PROJECT_EXPLORER = "org.eclipse.ui.navigator.ProjectExplorer";
	private static final String ID_VIEW_PACKEGE_EXPLORER = "org.eclipse.jdt.ui.PackageExplorer";
	
	private final SWTWorkbenchBot bot;
	
	private WorkbenchBot() {
		SWTBotInfo.printInfo();
		bot = new SWTWorkbenchBot();
	}
	
	public WorkbenchBot close() {
		bot.closeAllShells();
		return this;
	}
	
	public WorkbenchBot sleep(long ms) {
		bot.sleep(ms);
		return this;
	}
	
	public WorkbenchBot reset() {
		SWTBotView intorView = getViewById(ID_VIEW_INTRO);
		if(intorView != null) {
			intorView.close();
		}
		bot.resetWorkbench();
		
		openProjectExplorerView().deleteAllProjects();

		return this;
	}
	
	public SelectProjectBot triggerProjectSelectBinding() {
		bot.activeView().setFocus();  
		bot.activeShell().pressShortcut(SWT.CTRL, '6');
		bot.sleep(200);
		return getSelectProjectBot();
	}
	
	public void printAllViewIds() {
		List<SWTBotView> views = bot.views();
		for(SWTBotView view : views) {
			System.out.println(view.getViewReference().getId());
		}
	}
	
	public WorkbenchBot createProject(String name) {
		bot.shell().setFocus();
		bot.menu().menu("File").menu("New").menu("Project...").click();
		SWTBotShell newProjectWizzard = bot.shell("New Project");
		newProjectWizzard.bot().tree().expandNode("General", "Project").select();
		newProjectWizzard.bot().button("Next >").click();
		newProjectWizzard.bot().text().setText(name);
		newProjectWizzard.bot().button("Finish").click();
		return this;
	}
	
	public boolean isSelectProjectDialogOpen() {
		return getShellWithText("Select Project") != null;
	}
	
	public boolean isPackageExplorerViewOpen() {
		return getViewById(ID_VIEW_PACKEGE_EXPLORER) != null;
	}
	
	public boolean isProjectExplorerViewOpen() {
		return getViewById(ID_VIEW_PROJECT_EXPLORER) != null;
	}
	
	public SelectProjectBot getSelectProjectBot() {
		return new SelectProjectBot(getShellWithText("Select Project"));
	}
	
	public PackageExplorerBot openPackageExplorerView() {
		if(!isPackageExplorerViewOpen()) {
			bot.shell().setFocus();
			bot.menu().menu("Window").menu("Show View").menu("Other...").click();
			SWTBotShell newProjectWizzard = bot.shell("Show View");
			newProjectWizzard.bot().tree().expandNode("Java", "Package Explorer").select();
			newProjectWizzard.bot().button("Open").click();
			return new PackageExplorerBot(getViewById(ID_VIEW_PACKEGE_EXPLORER));
		}
		return new PackageExplorerBot(getViewById(ID_VIEW_PACKEGE_EXPLORER));
	}
	
	public ProjectExplorerBot openProjectExplorerView() {
		if(!isProjectExplorerViewOpen()) {
			bot.shell().setFocus();
			bot.menu().menu("Window").menu("Show View").menu("Other...").click();
			SWTBotShell newProjectWizzard = bot.shell("Show View");
			newProjectWizzard.bot().tree().expandNode("General", "Project Explorer").select();
			newProjectWizzard.bot().button("Open").click();
			return new ProjectExplorerBot(getViewById(ID_VIEW_PROJECT_EXPLORER));
		}
		return new ProjectExplorerBot(getViewById(ID_VIEW_PROJECT_EXPLORER));
	}
	
	private SWTBotView getViewById(String ID){
		List<SWTBotView> views = bot.views();
		for(SWTBotView view : views) {
			if(Objects.equals(ID, view.getViewReference().getId())){
				return view;
			}
		}
		return null;
	}
	
	private SWTBotShell getShellWithText(String text) {
		for(SWTBotShell shell : bot.shells()) {
			if(Objects.equals(shell.getText(), text)) {
				return shell;
			}
		}
		return null;
	}


}
