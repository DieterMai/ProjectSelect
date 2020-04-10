package dev.dietermai.projectselect.pgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ProjectGenerator {
	public static final String fileSeparator = System.getProperty("file.separator");
	public static final String workspaceRoot = ".." + fileSeparator + ".." + fileSeparator + "runtime-SimpleSelect";

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
	public static final String[] randomWords = new String[] { "defeated", "pencil", "hungry", "true", "class", "rhyme",
			"bang", "loud", "wild", "vegetable", "future", "educate", "slimy", "hospital", "hateful", "yummy", "liquid",
			"well-to-do", "attack", "serve", "messy", "loose", "reply", "stage", "snatch", "exclusive", "warlike",
			"overjoyed", "ignore", "repair", "camp", "transport", "dogs", "fact", "pray", "hate", "grade", "copy",
			"able", "wonderful", "loaf", "alluring", "place", "quaint", "chickens", "guiltless", "surprise", "mint",
			"way", "paint" };

	public static final String[] DELIMITER = { ".", "-", "_", " " };

	public static final Set<String> alreadyCreated = new HashSet<>();

	/** Save guard to prevent generating projects inside a workspace by accident. */
	private static final boolean I_REALLY_WANT_TO_DO_THIS = false;

	public static void main(String[] args) {
		System.out.println(new Object().toString());
		if (I_REALLY_WANT_TO_DO_THIS) {
			for (int i = 0; i < 10000; i++) {
				createProject(createRandomProjectName(3, 7));
			}
		}
	}

	public static void createProject(String name) {
		System.out.println("create project with name '" + name + "'");
		createProject(name, workspaceRoot);
	}
	
	public static void createProject(String name, String location) {
		System.out.println("create project with name '" + name + "'");
		try {
			File projectDir = new File(location + fileSeparator + name);
			projectDir.mkdir();

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

	public static String createRandomProjectName(int min, int max) {
		Random random = new Random(System.nanoTime());

		String randomProjectName;
		do {
			int wordCount = random.nextInt(max - min) + min;
			String[] projectWords = new String[wordCount];
			for (int i = 0; i < wordCount; i++) {
				projectWords[i] = randomWords[random.nextInt(randomWords.length)];
			}
			if (random.nextBoolean()) { // Each word capital
				for (int i = 0; i < projectWords.length; i++) {
					projectWords[i] = projectWords[i].substring(0, 1).toUpperCase() + projectWords[i].substring(1);
				}
			} else if (random.nextBoolean()) { // Each but the first word capital
				for (int i = 1; i < projectWords.length; i++) {
					projectWords[i] = projectWords[i].substring(0, 1).toUpperCase() + projectWords[i].substring(1);
				}
			} else if (random.nextBoolean()) { // Each char
				for (int i = 1; i < projectWords.length; i++) {
					projectWords[i] = projectWords[i].toUpperCase();
				}
			}

			String joint = String.join(DELIMITER[random.nextInt(DELIMITER.length)], projectWords);

			if (random.nextBoolean()) { // random upper case
				int index = random.nextInt(joint.length() - 1) + 1;
				joint = joint.substring(0, index - 1) + joint.substring(index, index + 1) + joint.substring(index);
			}

			randomProjectName = joint;
		} while (alreadyCreated.contains(randomProjectName.toLowerCase()));
		alreadyCreated.add(randomProjectName.toLowerCase());

		return randomProjectName;
	}

}
