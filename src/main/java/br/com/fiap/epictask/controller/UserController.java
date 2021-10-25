package br.com.fiap.epictask.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.epictask.exception.UserNotFoundException;
import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping
	public ModelAndView index() {
		return service.listUser();
	}
	
	@GetMapping("/ranking")
	public ModelAndView ranking() {
		return service.listOrderByPoints();
	}
	
	@RequestMapping("signup")
	public String create(User user) {
		return "user_form";
	}
	
	@PostMapping
	public String save(@Valid User user, BindingResult result, RedirectAttributes redirect) {		
		if(!service.saveUser(user, result, redirect)) {
			return "user_form";
		}
		return "redirect:user";
	}
	
	@GetMapping("{id}")
	public ModelAndView edit(@PathVariable Long id) throws UserNotFoundException {
		return service.edit(id);
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) throws UserNotFoundException {
		service.deleteUser(id);
		return "redirect:/user";
	}
}
