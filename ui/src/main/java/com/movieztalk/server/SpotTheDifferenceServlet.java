package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;

@WebServlet("/spotdifference")
public class SpotTheDifferenceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		SpotDifferenceImage spotImage = new SpotDifferenceImage();
		Connection connect = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from spotthedifference where is_used='false' limit 1 ");
			while (resultSet.next()) {
				spotImage.setActualImageUrl(resultSet
						.getString("actual_img_url"));
				spotImage.setModifiedImageUrl(resultSet
						.getString("modifies_img_url"));
				spotImage.setImageWidth(resultSet.getInt("img_width"));
				spotImage.setImageHeight(resultSet.getInt("img_height"));

				String defectCoords = resultSet.getString("diff_coordinates");
				String[] defectCoordinates = defectCoords.split("###");
				List<Coords> diffCoord = new ArrayList<>();

				for (String coord : defectCoordinates) {
					String[] coordinates = coord.split(",");
					Coords defect = new Coords();
					defect.setRadius(Integer.parseInt(coordinates[2]));
					defect.setyCoord(Integer.parseInt(coordinates[1]));
					defect.setxCoord(Integer.parseInt(coordinates[0]));
					diffCoord.add(defect);
				}

				spotImage.setDiffCoordinates(diffCoord);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}

		Gson gson = new Gson();
		String json = gson.toJson(spotImage);
		PrintWriter out = response.getWriter();
		System.out.println("Start" + new Date());
		out.write(json);
		out.flush();
		out.close();

	}

	class SpotDifferenceImage {
		String actualImageUrl;
		String modifiedImageUrl;
		List<Coords> diffCoordinates;
		int imageWidth;
		int imageHeight;

		public SpotDifferenceImage() {
			actualImageUrl = "";
			modifiedImageUrl = "";
			diffCoordinates = new ArrayList<>();
			imageWidth = 0;
			imageHeight = 0;
		}

		public SpotDifferenceImage(String actualImageUrl,
				String modifiedImageUrl, List<Coords> diffCoords,
				int imageWidth, int imageHeight) {
			this.actualImageUrl = actualImageUrl;
			this.modifiedImageUrl = modifiedImageUrl;
			this.diffCoordinates = diffCoords;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
		}

		public String getActualImageUrl() {
			return actualImageUrl;
		}

		public void setActualImageUrl(String actualImageUrl) {
			this.actualImageUrl = actualImageUrl;
		}

		public String getModifiedImageUrl() {
			return modifiedImageUrl;
		}

		public void setModifiedImageUrl(String modifiedImageUrl) {
			this.modifiedImageUrl = modifiedImageUrl;
		}

		public List<Coords> getDiffCoordinates() {
			return diffCoordinates;
		}

		public void setDiffCoordinates(List<Coords> diffCoordinates) {
			this.diffCoordinates = diffCoordinates;
		}

		public int getImageWidth() {
			return imageWidth;
		}

		public void setImageWidth(int imageWidth) {
			this.imageWidth = imageWidth;
		}

		public int getImageHeight() {
			return imageHeight;
		}

		public void setImageHeight(int imageHeight) {
			this.imageHeight = imageHeight;
		}

	}

	class Coords {
		int xCoord;
		int yCoord;
		int radius;

		public Coords() {
			xCoord = 0;
			yCoord = 0;
			radius = 0;

		}

		public Coords(int x, int y, int r) {
			this.xCoord = x;
			this.yCoord = y;
			this.radius = r;

		}

		public int getxCoord() {
			return xCoord;
		}

		public void setxCoord(int xCoord) {
			this.xCoord = xCoord;
		}

		public int getyCoord() {
			return yCoord;
		}

		public void setyCoord(int yCoord) {
			this.yCoord = yCoord;
		}

		public int getRadius() {
			return radius;
		}

		public void setRadius(int radius) {
			this.radius = radius;
		}

	}

}
