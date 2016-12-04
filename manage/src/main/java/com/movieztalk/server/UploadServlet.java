package com.movieztalk.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dropbox.core.DbxException;
import com.movieztalk.dropbox.DropBoxHelper;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			String movieId = request.getParameter("movieid");
			String extension = request.getParameter("extension");
			String fileName = movieId + "." + extension;
			System.err.println("SOMETHING IS WRONG");
			Part filePart = request.getPart("file");
			System.err.println(filePart.getName());
			inputStream = filePart.getInputStream();
			outputStream = new FileOutputStream(new File(fileName));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			new DropBoxHelper().uploadFile(fileName, "/images/" + movieId + "/" + fileName);
			File file = new File(fileName);
			file.delete();
			response.sendRedirect("/manage/html/Manage.html#/newMovie");
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} finally {

			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} finally {

			}
		}
	}
}
