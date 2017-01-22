package com.movieztalk.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

public class GoogleCloudStorage {

	public static final String BUCKETNAME = "purchhaseme";
	public static final String FILENAME = "ExampleFileName";

	public static void main(String args[]) throws IOException {
		GcsService gcsService = GcsServiceFactory.createGcsService();

		GcsFilename filename = new GcsFilename(BUCKETNAME, FILENAME);
		GcsFileOptions options = new GcsFileOptions.Builder().mimeType("text/html").acl("public-read")
				.addUserMetadata("myfield1", "my field value").build();

		GcsOutputChannel writeChannel = gcsService.createOrReplace(filename, options);
		PrintWriter writer = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		writer.println("The woods are lovely dark and deep.");
		writer.println("But I have promises to keep.");
		writer.flush();

		writeChannel.waitForOutstandingWrites();

		writeChannel.write(ByteBuffer.wrap("And miles to go before I sleep.".getBytes("UTF8")));

		writeChannel.close();
		System.out.println("Done writing");
	}
}
