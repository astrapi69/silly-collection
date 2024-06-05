package io.github.astrapi69.collection.migrate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for migrate to toml versions
 */
public class MigrateToTomlVersions
{

	public static File getGradleDirectory()
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		return gradleDirectory;
	}

	@Test
	public void testWriteToTomlFileWithJackson() throws IOException
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		System.out.println(projectDirectory.getAbsolutePath());
		System.out.println(gradleDirectory.getAbsolutePath());
	}
}
