package com.codeshaper.ms3.debug;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.codeshaper.ms3.Ms3;
import com.codeshaper.ms3.apiBuilder.ApiBuilder;
import com.codeshaper.ms3.util.Logger;

import scala.util.control.Exception;

/**
 * Builds the API files. Used for debugging the build process.
 * 
 * @author CodeShaper
 */
public class DebugRun {

	public static void main(String[] args) throws FileNotFoundException {
		Logger.msg("Starting debug build...");
		
		if(args.length < 1) {
			throw new IllegalArgumentException("The argument with the path to the soruce folder missing.");
		}

		//System.getProperty("user.home") + args[0];
			
		build("C:\\Users\\parke\\OneDrive\\Desktop\\Ms3\\run\\ms3");

		Logger.msg("Debug build finished!");
	}

	public static void build(String path) throws FileNotFoundException {
		path = StringUtils.removeEnd(StringUtils.removeEnd(path, "\\"), "/");
		File f = new File(path + "\\api-" + Ms3.API_VERSION);
		if (!new File(path).exists()) {
			throw new FileNotFoundException(path);
		}
		ApiBuilder builder = new ApiBuilder(f);

		builder.buildApi();
	}
}
