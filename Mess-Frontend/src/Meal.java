import java.time.LocalDate;
import java.util.HashMap;

public class Meal {
	public LocalDate date = LocalDate.now();
	public MealType meal;
	public User student;
	
	public Meal(LocalDate date, MealType meal, User user) {
		this.date = date;
		this.meal = meal;
		this.student = user;
	}
	
	public enum MealType {
		BREAKFAST,
		LUNCH,
		DINNER
	}
	
	@SuppressWarnings("serial")
	public static HashMap<MealType, String> mealTime = new HashMap<MealType, String>(){{
		put(MealType.BREAKFAST, "7.30am - 9.30am");
		put(MealType.LUNCH, "12pm - 2pm");
		put(MealType.DINNER, "7.30pm - 9.30pm");
	}};
}
