package com.movieztalk.demo;

import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;

public class DropBoxAPI {
	
	public static void main(String args[]) throws IOException, DbxException{
        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
            Locale.getDefault().toString());

        // Get this access token from https://www.dropbox.com/developers/apps/
        String accessToken = "";

        DbxClient client = new DbxClient(config, accessToken);

        System.out.println("Linked account: " + client.getAccountInfo().displayName);


        File inputFile = new File("/home/tarun/upload.txt");
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile("/upload.txt",
                DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } finally {
            inputStream.close();
        }
        
        System.out.println(client.createShareableUrl("/upload.txt"));
	}

}
