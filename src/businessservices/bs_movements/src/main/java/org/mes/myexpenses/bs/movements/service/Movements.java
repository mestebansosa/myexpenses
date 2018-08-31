package org.mes.myexpenses.bs.movements.service;

import java.util.List;

import org.mes.myexpenses.bs.movements.domains.CounterMovements;
import org.mes.myexpenses.bs.movements.domains.Movement;
import org.mes.myexpenses.bs.movements.domains.MovementId;

public interface Movements {
	
	public Movement findById(MovementId movementId);
	
	public List<Movement> findAll();
	
	public CounterMovements count();
	
	public boolean existsById(MovementId movementId);
	
	public Movement save(Movement movement);

	public void delete(Movement movement);
	
	public void deleteById(MovementId movementId);
}
