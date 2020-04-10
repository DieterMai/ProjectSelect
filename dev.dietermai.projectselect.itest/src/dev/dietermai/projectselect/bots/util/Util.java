package dev.dietermai.projectselect.bots.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
	public static final String simpleDotProject = """
			<?xml version="1.0" encoding="UTF-8"?>
			<projectDescription>
				 <name>${project.name}</name>
				 <comment></comment>
				 <projects>
				 </projects>
				 <buildSpec>
				 </buildSpec>
				 <natures>
				 </natures>
			</projectDescription>
			
			""";
	
	
	public static void createProject(String name, String location) {
		System.out.println("create project with name '" + name + "'");
		try {
			File projectDir = new File(join(location, name));
			projectDir.mkdir();
			System.out.println("Project path: "+projectDir.getCanonicalPath());
			System.out.println("Project exist: "+projectDir.exists());
			

			File dotProjectFile = new File(projectDir, ".project");
			dotProjectFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(dotProjectFile);
			fos.write(simpleDotProject.replace("${project.name}", name).getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String join(String... parts) {
		return String.join(File.separator, parts);
	}
}
