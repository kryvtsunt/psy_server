package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Constant;
import common.ImageInfo;

public class ImageDAO {
	
	private static Logger slf4jLogger = LoggerFactory.getLogger(ImageDAO.class);
	
	public static boolean createImage(ImageInfo imageInfo){
		
		slf4jLogger.info("Entered into createImage");
		
		String insertQuery = "INSERT INTO IMAGE (NAME, DESCRIPTION, CATEGORYID, INTENSITY, IMAGETYPE, IMAGELOC) "
				+ "values (?, ?, ?, ?, ?, ?)";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, imageInfo.getImageName());
			preparedStatement.setString(2, imageInfo.getImageDesc());
			preparedStatement.setLong(3, imageInfo.getImageCategoryId());
			preparedStatement.setLong(4, imageInfo.getImageIntensity());
			preparedStatement.setLong(5, imageInfo.getImageTypeId());
			preparedStatement.setString(6, imageInfo.getImageShortPath());
			
			slf4jLogger.info(preparedStatement.toString());
			int created = preparedStatement.executeUpdate();
			connection.close();
			if(created == 1) {
				return true;
			}
			return false;
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while extracting field information");
			slf4jLogger.info(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateImage(ImageInfo imageInfo){
		
		slf4jLogger.info("Entered into updateImage");
		
		String insertQuery = "UPDATE IMAGE SET NAME = ?, DESCRIPTION = ? , CATEGORYID = ?, INTENSITY = ? , "
				+ "IMAGETYPE = ?, IMAGELOC = ? where id = ? ";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, imageInfo.getImageName());
			preparedStatement.setString(2, imageInfo.getImageDesc());
			preparedStatement.setLong(3, imageInfo.getImageCategoryId());
			preparedStatement.setLong(4, imageInfo.getImageIntensity());
			preparedStatement.setLong(5, imageInfo.getImageTypeId());
			preparedStatement.setString(6, imageInfo.getImageShortPath());
			
			preparedStatement.setLong(7, imageInfo.getId());
			
			slf4jLogger.info(preparedStatement.toString());
			int created = preparedStatement.executeUpdate();
			connection.close();
			if(created == 1) {
				slf4jLogger.info("Image details updated successfully");
				return true;
			}
			return false;
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while extracting field information");
			slf4jLogger.info(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	public static Long getImageIdByImageName(String name){
		
		slf4jLogger.info("Entered into getImageIdByImageName");
		

		//String selectQuery = "SELECT ID FROM IMAGE WHERE NAME = ?";

		String selectQuery = "SELECT id FROM IMAGE WHERE name = ?";

		Connection connection = null;
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			Long id = null;
			if(rs.first()) {

				//id = rs.getLong("ID");

				id = rs.getLong("id");

			}
			connection.close();
			return id;
		}catch(SQLException e){
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	public static void deleteImageById(Long id){
		
		slf4jLogger.info("Entered into deleteImageById");

		//String selectQuery = "DELETE FROM IMAGE WHERE ID = ?";

		String selectQuery = "DELETE FROM IMAGE WHERE id = ?";

		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public static boolean isDuplicateImage(String name){
		
		slf4jLogger.info("Entered into isDuplicateImage");
		
		String selectQuery;
		selectQuery = "SELECT * FROM IMAGE WHERE NAME = ?";
		Connection connection = null;
		
		boolean isDuplicate = false;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.first()) {
				isDuplicate = true;
			}else{
				isDuplicate = false;
			}
			connection.close();
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			isDuplicate = false;
		}
		
		return isDuplicate;
	}
	
	public static JSONArray fetchAllImage(){
		
		slf4jLogger.info("Entered into fetchAllImage");

		String selectQuery = "SELECT IM.ID, IM.NAME, IM.DESCRIPTION, IM.CATEGORYID, IM.INTENSITY, "
				+ "IM.IMAGETYPE, IM.IMAGELOC, FL.FIELDNAME, IC.NAME FROM IMAGE AS IM JOIN FIELDLOOKUP "
				+ "AS FL ON IM.IMAGETYPE=FL.ID JOIN IMAGECATEGORY AS IC ON IC.ID =  IM.CATEGORYID;";

		Connection connection = null;
		JSONArray jsonArray = new JSONArray();
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				JSONObject jsonObject = new JSONObject();

				jsonObject.put(Constant.IMAGE_ID, rs.getLong("IM.ID"));
				jsonObject.put(Constant.IMAGE_NAME, rs.getString("IM.NAME"));
				jsonObject.put(Constant.IMAGE_DESCRIPTION, rs.getString("IM.DESCRIPTION"));
				jsonObject.put(Constant.IMAGE_CATEGORY_ID,rs.getLong("IM.CATEGORYID"));
				jsonObject.put(Constant.IMAGE_INTENSITY,rs.getLong("IM.INTENSITY"));
				jsonObject.put(Constant.IMAGE_TYPE_ID,rs.getLong("IM.IMAGETYPE"));
				jsonObject.put(Constant.IMAGE_PATH,rs.getString("IM.IMAGELOC"));
				jsonObject.put(Constant.IMAGE_CATEGORY, rs.getString("IC.NAME"));
				jsonObject.put(Constant.IMAGE_TYPE, rs.getString("FL.FIELDNAME"));

				jsonArray.add(jsonObject);
			}
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return jsonArray;
	}
	
	public static ArrayList<ImageInfo> fetchTrainingImageCategoryInfoByTargetGroupId(long tgId){
		
		slf4jLogger.info("Entered into fetchAllImageForTargetGroup");

		String selectQuery = "SELECT TIM.IMAGECATEGORYID, TIM.DURATION, TIM.IMAGETYPE, FL.FIELDNAME, TIM.NOOFIMAGES "
				+ "FROM TRAINING AS TR JOIN TARGETGROUP AS TG ON TR.ID=TG.TRAININGID JOIN TRAININGIMAGEMAP "
				+ "AS TIM ON TR.ID=TIM.TRAININGID JOIN FIELDLOOKUP AS "
				+ "FL ON FL.ID = TIM.IMAGETYPE WHERE TG.ID= ? ORDER BY RAND()";

		
		ArrayList<ImageInfo> results = new ArrayList<ImageInfo>();
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setLong(1, tgId);
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				ImageInfo imageInfo = new ImageInfo();

				imageInfo.setImageCategoryId(rs.getLong("TIM.IMAGECATEGORYID"));
				imageInfo.setDuration(rs.getLong("TIM.DURATION"));
				imageInfo.setImageTypeId(rs.getLong("TIM.IMAGETYPE"));
				imageInfo.setImageType(rs.getString("FL.FIELDNAME"));
				imageInfo.setNoOfImages(rs.getLong("TIM.NOOFIMAGES"));

				results.add(imageInfo);
			}
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return results;
	}
	
	public static JSONArray fetchTrainingImageInfoByTargetGroupId(long tgId){
		
		slf4jLogger.info("Entered into fetchAllImageForTargetGroup");

		String selectQuery = "SELECT ID, IMAGELOC FROM IMAGE WHERE CATEGORYID = ? AND IMAGETYPE = ? ORDER BY RAND()";

		//String selectQuery = "select id, imageLoc from IMAGE where categoryid = ? and imageType = ? ORDER BY RAND()";

		
		ArrayList<ImageInfo> results = ImageDAO.fetchTrainingImageCategoryInfoByTargetGroupId(tgId);
		Connection connection = null;
		JSONArray jsonArray = new JSONArray();
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement  preparedStatement = connection.prepareStatement(selectQuery);
			int length = results.size();
			Random rnd = new Random();
	        rnd.setSeed(System.currentTimeMillis());
			for(int i=0; i< length; i++){
				
				
				preparedStatement.setLong(1, results.get(i).getImageCategoryId());
				preparedStatement.setLong(2, results.get(i).getImageTypeId());
				ResultSet rs = preparedStatement.executeQuery();
				
				ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();
				while(rs.next()) {
					ImageInfo imageInfo = new ImageInfo();

					imageInfo.setId(rs.getLong("ID"));
					imageInfo.setImageShortPath(rs.getString("IMAGELOC"));

					images.add(imageInfo);
				}
				
				long allowedImages = results.get(i).getNoOfImages();
				ArrayList<Integer> imageNumbers = new ArrayList<Integer>();
				int totalImages = images.size();
			    for (int j = 0; j < totalImages; j++) {
			        imageNumbers.add(j);
			    }
				
				Collections.shuffle(imageNumbers);
				if(totalImages < allowedImages)
					allowedImages = totalImages;
				
				for(int k =0; k<allowedImages; k++){
					int randomImageIndex = imageNumbers.get(k);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(Constant.IMAGE_CATEGORY_ID,  results.get(i).getImageCategoryId());
					jsonObject.put(Constant.IMAGE_TYPE, results.get(i).getImageType());
					jsonObject.put(Constant.IMAGE_TYPE_ID, results.get(i).getImageTypeId());
					jsonObject.put(Constant.IMAGE_DISPLAY_DURATION, results.get(i).getDuration());
					jsonObject.put(Constant.IMAGE_ID, images.get(randomImageIndex).getId());
					jsonObject.put(Constant.IMAGE_PATH, images.get(randomImageIndex).getImageShortPath());
					jsonArray.add(jsonObject);
				}
			}
			Collections.shuffle(jsonArray);
			// execute select SQL statement
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return jsonArray;
	}
	
	public static boolean saveImageResponse(ArrayList<HashMap<String, String>> responseList, 
			Long sessionId, String participantId){
		
		/*
		 * String insertQuery =
		 * "INSERT INTO IMAGERESPONSE (SESSIONID, PARTICIPANTID, CORRECTNESS, TIMETAKEN, BGCOLOR,"
		 * + "ISATTEMPTED, IMAGEID) "
		 */

		String insertQuery = "INSERT INTO IMAGERESPONSE (sessionId, participantId, correctness, timeTaken, bgColor,"
				+ "isAttempted, imageId) "

				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			int length = responseList.size();
			for(int i=0; i< length; i++){
				HashMap<String, String> response = responseList.get(i);
				System.out.println("Check this : "+response.toString());
				preparedStatement.setLong(1, sessionId);
				preparedStatement.setLong(2, Long.parseLong(participantId));
				if(response.get(Constant.CORRECTNESS).equals(Constant.TRUE))
					preparedStatement.setShort(3, (short) 1);
				else
					preparedStatement.setShort(3, (short) 0);
				preparedStatement.setLong(4, Long.parseLong(response.get(Constant.RESPONSE_TIME)));
				preparedStatement.setString(5, response.get(Constant.BACKGROUND_COLOR));
				if(response.get(Constant.IS_ATTEMPTED).equals(Constant.TRUE))
					preparedStatement.setShort(6, (short) 1);
				else
					preparedStatement.setShort(6, (short) 0);
				preparedStatement.setLong(7, Long.parseLong(response.get(Constant.IMAGE_ID)));
				// execute select SQL statement
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected != 1){
					System.out.println("Issue while insert");
					System.out.println(preparedStatement.toString());
					return false;
				}
			}
			connection.close();
			return true;
		}catch(SQLException e){
			System.out.println(e.getMessage());
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean deleteImageResponses(Long sessionId){
		

		//String deleteQuery = "DELETE FROM IMAGERESPONSE WHERE SESSIONID = ?";
		String deleteQuery = "DELETE FROM IMAGERESPONSE WHERE sessionId = ?";

		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setLong(1, sessionId);
			
			// execute select SQL statement
			int rowsAffected = preparedStatement.executeUpdate();
			
			connection.close();
			if (rowsAffected >= 1){
				return true;
			}
			else{
				return false;
			}
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	
}
