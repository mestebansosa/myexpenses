package org.mes.myexpenses.bs.movements.service;

import java.util.List;

import org.mes.myexpenses.bs.movements.domains.Category;
import org.mes.myexpenses.bs.movements.domains.CategoryId;
import org.mes.myexpenses.bs.movements.domains.CounterCategories;

public interface Categories {
	
	public Category findById(CategoryId categoryId);
	
	public List<Category> findAll();
	
	public CounterCategories count();
	
	public boolean existsById(CategoryId categoryId);
	
	public Category save(Category category);

	public void delete(Category category);
	
	public void deleteById(CategoryId categoryId);

}