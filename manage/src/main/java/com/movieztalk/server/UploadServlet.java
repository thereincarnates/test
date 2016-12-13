package com.movieztalk.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dropbox.core.DbxException;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.dropbox.DropBoxHelper;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			int movieId = findNextMovieId();
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
			String shareableUrl = new DropBoxHelper().uploadFile(fileName, "/images/" + movieId + "/" + fileName);
			shareableUrl = shareableUrl.replaceAll("0$", "1");
			insertMovieIntoDb(movieId, shareableUrl);
			System.out.println(shareableUrl);
			File file = new File(fileName);
			file.delete();
			response.sendRedirect("/manage/html/Manage.html#/newMovie/" + movieId);
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

	public int findNextMovieId() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int result = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select max(movieid) from movie");
			resultSet.next();
			result = resultSet.getInt(1) + 1;
		} finally {
			DatabaseHelper.getInstance().closeResources(connection, statement, resultSet);
		}
		return result;
	}

	public void insertMovieIntoDb(int movieid, String url) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int result = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connection.prepareStatement("insert into movie(movieid, imageurl) values(?, ?)");
			statement.setString(1, String.valueOf(movieid));
			statement.setString(2, url);
			statement.execute();
		} finally {
			DatabaseHelper.getInstance().closeResources(connection, statement, resultSet);
		}
	}
}
