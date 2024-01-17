package org.lessons.java.repository;

import java.util.List;

import org.lessons.java.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

	public List<Pizza> findByNameContainingIgnoreCaseOrderByUpdatedAt(String keyword);

	public List<Pizza> findByName(String name);
}
