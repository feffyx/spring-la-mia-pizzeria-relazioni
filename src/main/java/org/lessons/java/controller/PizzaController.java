package org.lessons.java.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.lessons.java.model.Pizza;
import org.lessons.java.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

	private @Autowired PizzaRepository repository;
	
	@GetMapping
	public String index(Model model, @RequestParam(name="keyword", required=false) String keyword) {
		List<Pizza> result;
		if(keyword != null && !keyword.isEmpty()) {
			result = repository.findByNameContainingIgnoreCaseOrderByUpdatedAt(keyword);
			model.addAttribute("keyword", keyword);
		} else {
			result = repository.findAll(Sort.by("updatedAt"));
		}
		model.addAttribute("list", result);
		return "/pizzas/index";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") Integer id , Model model) {
		model.addAttribute("pizza", repository.findById(id).get());
		return "/pizzas/show";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pizza", new Pizza());
		return "/pizzas/create";
	}
	
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("pizza") Pizza formBook, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if(bindingResult.hasErrors()) {
			return "/pizzas/create";
		}

		formBook.setUpdatedAt(LocalDateTime.now());
		
		repository.save(formBook);
		
		redirectAttributes.addFlashAttribute("successMessage", "Pizza created!");
		
		return "redirect:/pizzas";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("pizza", repository.findById(id).get());
		return "/pizzas/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("pizza") Pizza formBook, 
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) {
			return "/pizzas/edit";
		}
		
		formBook.setUpdatedAt(LocalDateTime.now());
		
		repository.save(formBook);
		
		redirectAttributes.addFlashAttribute("successMessage", "Pizza edited!");
		
		return "redirect:/pizzas";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		
		repository.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Pizza deleted!");
		return "redirect:/pizzas";
	}
}
