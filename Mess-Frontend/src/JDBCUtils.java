import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JDBCUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/mms_dbs";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Scientist#99";
    private static Connection connection;
    
    private static int mess1capacity = JDBCUtils.getMessInfo(1).capacity;
    private static int mess2capacity = JDBCUtils.getMessInfo(2).capacity;
    
    private static Object[][] currentMess1Capacity = {{LocalDate.now(), mess1capacity}, 
    		{LocalDate.now(), mess1capacity}, {LocalDate.now(), mess1capacity}};
    private static Object[][] currentMess2Capacity = {{LocalDate.now(), mess2capacity}, 
    		{LocalDate.now(), mess2capacity}, {LocalDate.now(), mess2capacity}};
    
    @SuppressWarnings("serial")
	public static final HashMap<String, Integer> messID = new HashMap<String, Integer>(){{
    	put("Mess 1", 1);
    	put("Mess 2", 2);
    }};
    @SuppressWarnings("serial")
	public static final HashMap<Integer, String> messName = new HashMap<Integer, String>(){{
    	put(1, "Mess 1");
    	put(2, "Mess 2");
    }};

    public static void setConnection() throws SQLException {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
    	connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return;
    }

    public static Object[] authenticateLogin(String email, String password) {
    	try {
        	setConnection();
            // Query to check if email and password match in the student table
            PreparedStatement studentQuery = connection.prepareStatement("SELECT * FROM student WHERE email = ? AND pwd = ?");
            studentQuery.setString(1, email);
            studentQuery.setString(2, password);
            ResultSet studentResult = studentQuery.executeQuery();

            if (studentResult.next()) {
                User studentUser = JDBCUtils.getUserInfo((int)studentResult.getInt("student_id"), 1);
            	return new Object[]{1, studentUser}; // Login successful for student
            }

            // Query to check if email and password match in the mess_admin table
            PreparedStatement messAdminQuery = connection.prepareStatement("SELECT * FROM mess_admin WHERE email = ? AND pwd = ?");
            messAdminQuery.setString(1, email);
            messAdminQuery.setString(2, password);
            ResultSet messAdminResult = messAdminQuery.executeQuery();

            if (messAdminResult.next()) {
            	User adminUser = JDBCUtils.getUserInfo((int)messAdminResult.getInt("admin_id"), 2);
            	return new Object[]{2, adminUser}; // Login successful for mess admin
            }
            
         // Query to check if email and password match in the manager table
            PreparedStatement managerQuery = connection.prepareStatement("SELECT * FROM manager WHERE email = ? AND pwd = ?");
            managerQuery.setString(1, email);
            managerQuery.setString(2, password);
            ResultSet managerResult = managerQuery.executeQuery();

            if (managerResult.next()) {
            	User managerUser = JDBCUtils.getUserInfo((int)managerResult.getInt("manager_id"), 3);
            	return new Object[]{3, managerUser}; // Login successful for manager
            }

            // Close database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Object[] {0, null}; // Login failed
    }
    
    public static boolean createUser(String name, String email, String password, String mess, 
    		String hostel, String role) {
    	int rowsAffected = 0;
    	try {
    		setConnection();
    		if(role=="Student") {
        		String sql = "INSERT INTO student (name, email, hostel, pwd, mess_id) VALUES (?, ?, ?, ?, ?)";

                // Create PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int messid = messID.get(mess);

                // Set parameter values for PreparedStatement
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, hostel);
                preparedStatement.setString(4, password);
                preparedStatement.setInt(5, messid);
                
                rowsAffected = preparedStatement.executeUpdate();
        	}
        	else if (role=="Admin"){
        		String sql = "INSERT INTO mess_admin (email, pwd, name) VALUES (?, ?, ?)";

                // Create PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                // Set parameter values for PreparedStatement
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, name);

                // Execute the PreparedStatement to insert data into the table
                rowsAffected = preparedStatement.executeUpdate();
        	}
        	else {
        		String sql = "INSERT INTO manager (email, pwd, name, mess_id) VALUES (?, ?, ?, ?)";

                // Create PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int messid = messID.get(mess);

                // Set parameter values for PreparedStatement
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, name);
                preparedStatement.setInt(4, messid);

                // Execute the PreparedStatement to insert data into the table
                rowsAffected = preparedStatement.executeUpdate();
        	}

    	} catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	if(rowsAffected==0) {
    		return false;
    	}
    	return true;
    }
    
    public static User getUserInfo(int id, int role) {
    	try {
    		setConnection();
    		User user = new User();
    		
    		switch(role) {
    		case 1:
    			String query = "SELECT * FROM student WHERE student_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);

                // Execute query
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                	user.userId = id;
                	user.name = resultSet.getString("name");
                	user.email = resultSet.getString("email");
                	user.hostel = resultSet.getString("hostel");
                	user.messId = resultSet.getInt("mess_id");
                	user.role = User.Role.Student;
                	
                	return user;
                }
                break;
    		case 2:
    			query = "SELECT * FROM mess_admin WHERE admin_id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);

                // Execute query
                resultSet = statement.executeQuery();
                if(resultSet.next()) {
                	user.userId = id;
                	user.name = resultSet.getString("name");
                	user.email = resultSet.getString("email");
                	user.role = User.Role.Admin;
                	
                	return user;
                }
                break;
    		case 3:
    			query = "SELECT * FROM manager WHERE manager_id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);

                // Execute query
                resultSet = statement.executeQuery();
                if(resultSet.next()) {
                	user.userId = id;
                	user.name = resultSet.getString("name");
                	user.email = resultSet.getString("email");
                	user.messId = resultSet.getInt("mess_id");
                	user.role = User.Role.Manager;
                	
                	return user;
                }
                break;
             default:
            	 break;
    		}
           
            return null;
    	} catch (SQLException e) {
            return null;
        }
    }
    
    public static Mess getMessInfo(int id) {
    	try {
    		setConnection();
    		Mess mess = new Mess();
    		
    		String query = "SELECT * FROM mess WHERE mess_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
            	mess.messId = id;
            	mess.capacity = resultSet.getInt("capacity");
            	return mess;
            }
            return null;
            
    	} catch (SQLException e) {
            return null;
        }
    }
    
    public static boolean updateMeal(Meal meal) {
    	try {
    		setConnection();
    		if(meal.student.messId==1) {
				if(meal.date==currentMess1Capacity[0][0]) {
					switch(meal.meal) {
					case BREAKFAST:
						if((Integer)currentMess1Capacity[0][1]==0) {
							return false;
						}
						currentMess1Capacity[0][1] = (Integer)currentMess1Capacity[0][1]-1;
						break;
					case LUNCH:
						if((Integer)currentMess1Capacity[1][1]==0) {
							return false;
						}
						currentMess1Capacity[1][1] = (Integer)currentMess1Capacity[1][1]-1;
						break;
					case DINNER:
						if((Integer)currentMess1Capacity[2][1]==0) {
							return false;
						}
						currentMess1Capacity[2][1] = (Integer)currentMess1Capacity[2][1]-1;
						break;
					default:
						break;
					}
				}
				else {
					currentMess1Capacity[0][0] = currentMess1Capacity[1][0] = currentMess1Capacity[2][0] = meal.date;
					currentMess1Capacity[0][1] = currentMess1Capacity[1][1] = currentMess1Capacity[2][1] = mess1capacity-1;
				}
			}
    		else if(meal.student.messId==2) {
    			if(meal.date==currentMess2Capacity[0][0]) {
					switch(meal.meal) {
					case BREAKFAST:
						if((Integer)currentMess2Capacity[0][1]==0) {
							return false;
						}
						currentMess2Capacity[0][1] = (Integer)currentMess2Capacity[0][1]-1;
						break;
					case LUNCH:
						if((Integer)currentMess2Capacity[1][1]==0) {
							return false;
						}
						currentMess2Capacity[1][1] = (Integer)currentMess2Capacity[1][1]-1;
						break;
					case DINNER:
						if((Integer)currentMess2Capacity[2][1]==0) {
							return false;
						}
						currentMess2Capacity[2][1] = (Integer)currentMess2Capacity[2][1]-1;
						break;
					default:
						break;
					}
				}
				else {
					currentMess2Capacity[0][0] = currentMess2Capacity[1][0] = currentMess2Capacity[2][0] = meal.date;
					currentMess2Capacity[0][1] = currentMess2Capacity[1][1] = currentMess2Capacity[2][1] = mess2capacity-1;
				}
    		}
    		
    		String query = "INSERT INTO meal (date, time, student_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(meal.date));
            statement.setString(2, meal.meal.toString());
            statement.setInt(3, meal.student.userId);
            
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected>0) {
            	return true;
            }
            return false;
            
    	} catch (SQLException e) {
            return false;
        }
    }
    
    public static String[] loadMeals(int studentId) {
    	try {
    		setConnection();
    		String[] meals = {"", "", ""};
    		
    		String query = "SELECT * FROM meal WHERE date = ? AND student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setInt(2, studentId);
            
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
            	if(resultSet.getString("time").equals("BREAKFAST") && meals[0].isEmpty()) {
            		if((Integer)currentMess1Capacity[0][1]==0) {
            			meals[0] = "FULL";
            		}
            		else meals[0] = "OPTED";
            	}
            	else if(resultSet.getString("time").equals("LUNCH") && meals[1].isEmpty()){
            		if((Integer)currentMess1Capacity[1][1]==0) {
            			meals[1] = "FULL";
            		}
            		else meals[1] = "OPTED";
            	}
            	else if(meals[2].isEmpty()){
            		if((Integer)currentMess1Capacity[2][1]==0) {
            			meals[2] = "FULL";
            		}
            		else meals[2] = "OPTED";
            	}
            }
            
            for(int i=0; i<3; i++) {
            	if(meals[i].isEmpty()) {
            		meals[i]="OPT";
            	}
            }
            
            return meals;
            
    	} catch (SQLException e) {
            return null;
        }
    }
    
    public static List<LeaveApplication> getStudentLeaves(int id){
    	try {
    		setConnection();
    		List<LeaveApplication> leaves = new ArrayList<>();
        	
    		String query = "SELECT * FROM leave_app WHERE student_id = ? ORDER BY leave_id DESC LIMIT 10";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
            	leaves.add(new LeaveApplication(resultSet.getDate("start_date").toString()+" - "+resultSet.getDate("end_date"), 
            			resultSet.getString("status")));
            }
    		
        	return leaves;
    	} catch (SQLException e) {
            return null;
        }
    }
    
    public static boolean createLeaveApp(LocalDate startDate, LocalDate endDate, String comment, int id) {
    	try {
    		setConnection();
    		String query = "INSERT INTO leave_app (student_id, start_date, end_date, comment, status) VALUES (?, ?, ?, ?, 'PENDING')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(startDate));
            statement.setDate(3, Date.valueOf(endDate));
            statement.setString(4, comment);
            
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected>0) {
            	return true;
            }
            
            return false;
    	} catch (SQLException e) {
    		return false;
        }
    }
    
    public static boolean createFeedback(String comment, int id) {
    	try {
    		setConnection();
    		String query = "INSERT INTO feedback (student_id, comment) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2,comment);
            
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected>0) {
            	return true;
            }
            
            return false;
    	} catch (SQLException e) {
    		return false;
        }
    }

	public static Object[] requestFood(String type, List<String> selectedMeals, int id) {
		try {
    		setConnection();
    		
    		int rowsAffected = 0;
    		List<Integer> request_ids = new ArrayList<Integer>();
    		String query = "INSERT INTO food_request (type, item, status, date, student_id) VALUES (?, ?, 'PENDING', ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, type);
            statement.setDate(3, Date.valueOf(LocalDate.now()));
            statement.setInt(4, id);
            
            for(String s: selectedMeals) {
            	statement.setString(2, s);
            	
            	rowsAffected = statement.executeUpdate();
            	System.out.print(rowsAffected);
                if(rowsAffected==0) {
                	return new Object[]{false, 0};
                }
                ResultSet genKeys = statement.getGeneratedKeys();
                if(genKeys.next()) {
                	request_ids.add(genKeys.getInt(1));
                }
                else {
                	return new Object[]{false, 0};
                }
            }
            
            return new Object[]{true, request_ids};
    	} catch (SQLException e) {
    		return new Object[]{false, 0};
        }
	}
	
	public static List<ManagerFPReq> getFoodRequests(int messId, int mealType){
		try {
			setConnection();
			List<ManagerFPReq> foodRequests = new ArrayList<>();
        	
    		String query = "SELECT s.name AS sname, s.student_id as sid, fr.request_id as fid, fr.item AS fitem, fr.status AS fstatus "
    				+ "FROM student s JOIN food_request fr ON s.student_id = fr.student_id WHERE s.mess_id = ? AND fr.type = ? LIMIT 10";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, messId);
            statement.setString(2, ManagerFPReq.mealTypes.get(mealType));
            
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
            	ManagerFPReq.ReqStatus status = resultSet.getString("fstatus").equals("PENDING")?ManagerFPReq.ReqStatus.PENDING:ManagerFPReq.ReqStatus.DELIVERED;
            	foodRequests.add(new ManagerFPReq(resultSet.getInt("fid"), resultSet.getString("sname"), resultSet.getInt("sid"), 
            			resultSet.getString("fitem"), mealType, status));
            }
            
            return foodRequests;
			
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static boolean updateFoodRequestStatus(int requestId) {
	    try {
	        setConnection();
	        String query = "UPDATE food_request SET status = 'DELIVERED' WHERE request_id = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, requestId);
	        
	        int rowsAffected = statement.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        return false;
	    }
	}
	
	public static boolean createLeftovers (String time, int plates, int messId) {
		try {
		    setConnection();
		    String query = "INSERT INTO leftover (date, time, plates, mess_id) VALUES (?, ?, ?, ?)";
		    PreparedStatement statement = connection.prepareStatement(query);
		    
		    // Set the values for the parameters
		    statement.setDate(1, Date.valueOf(LocalDate.now())); 
		    statement.setString(2, time); 
		    statement.setInt(3, plates); 
		    statement.setInt(4, messId); 
		    
		    // Execute the statement
		    int rowsAffected = statement.executeUpdate();
		    
		    // Check if the insertion was successful
		    if (rowsAffected > 0) {
		        return true;
		    } 
		    return false;
		} catch (SQLException e) {
		    return false; // Handle the SQL exception
		}

	}
	
	public static int getTotalLeftover () {
		try {
			setConnection();
			int totalLeftover = 0;
			
			String query = "SELECT SUM(plates) AS total FROM leftover WHERE date = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalLeftover = resultSet.getInt("total");
            }
            return totalLeftover;
		} catch (SQLException e) {
		    return 0; // Handle the SQL exception
		}
	}
	
	public static List<AdminLeave> getAllLeaves(int length){
		try {
			setConnection();
			List<AdminLeave> leaves = new ArrayList<>();
			
			String query = "SELECT leave_app.*, student.name, student.mess_id FROM leave_app JOIN student ON "
					+ "leave_app.student_id = student.student_id ORDER BY leave_id DESC LIMIT ?";
            PreparedStatement statement = connection.prepareStatement(query);
            if(length==0) {
            	statement.setInt(1, 10);
            }
            else {
            	statement.setInt(1, 50);
            }
			
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
            	leaves.add(new AdminLeave(resultSet.getInt("leave_app.leave_id"), resultSet.getString("student.name"), resultSet.getInt("leave_app.student_id"),
            			resultSet.getInt("student.mess_id"), resultSet.getDate("leave_app.start_date").toString()+" - "+resultSet.getDate("leave_app.end_date").toString(),
            			resultSet.getString("leave_app.comment")));
            }
            
			return leaves;			
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static boolean approveLeave(int lid) {
		try {
			setConnection();
			
			String query = "UPDATE leave_app SET status = 'APPROVED' WHERE leave_id = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, lid);
	        
	        int rowsAffected = statement.executeUpdate();
	        return rowsAffected > 0;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static List<AdminFB> getFeedback(int length){
		try {
			setConnection();
			List<AdminFB> feedback = new ArrayList<>();
			
			String query = "SELECT feedback.*, student.name, student.mess_id FROM feedback JOIN student ON "
					+ "feedback.student_id = student.student_id ORDER BY feedback_id DESC LIMIT ?";
			PreparedStatement statement = connection.prepareStatement(query);
			if(length==0) {
            	statement.setInt(1, 10);
            }
            else {
            	statement.setInt(1, 50);
            }
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				feedback.add(new AdminFB(resultSet.getString("student.name"), resultSet.getInt("feedback.student_id"), resultSet.getInt("student.mess_id"),
						resultSet.getString("feedback.comment")));
			}
			
			return feedback;
		} catch (SQLException e) {
			return null;
		}
	}
}