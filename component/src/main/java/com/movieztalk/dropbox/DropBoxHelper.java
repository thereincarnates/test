package com.movieztalk.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.movieztalk.configuration.ConfigurationHelper;

public class DropBoxHelper {

	public String uploadFile(String sourcePath, String destinationPath)
			throws DbxException, IOException, ClassNotFoundException, SQLException {
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());

		// Get this access token from https://www.dropbox.com/developers/apps/
		String accessToken = ConfigurationHelper.getInstance().getParamterToValueMap()
				.get(ConfigurationHelper.ConfigurationKey.DROPBOXAUTHORIZATION.name().toLowerCase());

		DbxClient client = new DbxClient(config, accessToken);

		System.out.println("Linked account: " + client.getAccountInfo().displayName);

		File inputFile = new File(sourcePath);
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
			DbxEntry.File uploadedFile = client.uploadFile(destinationPath, DbxWriteMode.add(), inputFile.length(),
					inputStream);
			System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
			inputStream.close();
		}
		return client.createShareableUrl(destinationPath);
	}
}
