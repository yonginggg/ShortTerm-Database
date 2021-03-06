package kitchen.model;

public class BeanIngredientsInformation {
	public static BeanIngredientsInformation currentIngredients = null;
	public static final String[] tblIngredientsTitle = { "食材编号", "食材名称", "食材价格", "数量", "食材描述", "单位"};
	public String getCell(int col) {
		if (col == 0)
			return String.valueOf(getIngredients_number());
		else if (col == 1)
			return getIngredients_name();
		else if (col == 2)
			return String.valueOf(getIngredients_price());
		else if(col == 3)
			return String.valueOf(getIngredients_quantity());
		else if(col == 4)
			return getIngredients_description();
		else if(col == 5)
			return getIngredients_specification();
		else
			return "";
	}
	
	private int ingredients_number;
	private String ingredients_name;
	private double ingredients_price;
	private double ingredients_quantity;
	private String ingredients_description;
	private String ingredients_specification;
	private int category_number;
	
	public int getIngredients_number() {
		return ingredients_number;
	}
	public void setIngredients_number(int ingredients_number) {
		this.ingredients_number = ingredients_number;
	}
	public String getIngredients_name() {
		return ingredients_name;
	}
	public void setIngredients_name(String ingredients_name) {
		this.ingredients_name = ingredients_name;
	}
	public double getIngredients_price() {
		return ingredients_price;
	}
	public void setIngredients_price(double ingredients_price) {
		this.ingredients_price = ingredients_price;
	}
	public double getIngredients_quantity() {
		return ingredients_quantity;
	}
	public void setIngredients_quantity(double ingredients_quantity) {
		this.ingredients_quantity = ingredients_quantity;
	}
	public String getIngredients_description() {
		return ingredients_description;
	}
	public void setIngredients_description(String ingredients_description) {
		this.ingredients_description = ingredients_description;
	}
	public String getIngredients_specification() {
		return ingredients_specification;
	}
	public void setIngredients_specification(String ingredients_specification) {
		this.ingredients_specification = ingredients_specification;
	}
	public int getCategory_number() {
		return category_number;
	}
	public void setCategory_number(int category_number) {
		this.category_number = category_number;
	}


}
