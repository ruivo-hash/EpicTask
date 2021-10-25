package br.com.fiap.epictask.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.epictask.exception.TaskNotFoundException;
import br.com.fiap.epictask.exception.UserNotFoundException;
import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private MessageSource messages;
	
	public ModelAndView listUser(){
		List<User> users = repository.findAll();
		ModelAndView modelAndView = new ModelAndView("users");
		modelAndView.addObject("users", users);
		return modelAndView;
	}
	
	public ModelAndView listOrderByPoints() {
		List<User> users = repository.listOrderByPoints();
		ModelAndView modelAndView = new ModelAndView("ranking");
		modelAndView.addObject("users", users);
		return modelAndView;
	}

	public boolean saveUser(User user, BindingResult result, RedirectAttributes redirect) {
		if(result.hasErrors()) return false;
		user.setPassword(AuthenticationService.getPasswordEncoder().encode(user.getPassword()));
		if(user.getId() == null) {
			repository.save(user);
			redirect.addFlashAttribute("message", messages.getMessage("user.new.sucess", null, LocaleContextHolder.getLocale()));
		}else {
			repository.save(user);
			redirect.addFlashAttribute("message", messages.getMessage("user.edit.sucess", null, LocaleContextHolder.getLocale()));
		}
		return true;
	}

	public ModelAndView edit(Long id) throws UserNotFoundException {
		Optional<User> optional = repository.findById(id);
		if(optional.isEmpty())
			throw new UserNotFoundException("usuário não encontrado");
		
		User user = optional.get();
		
		ModelAndView modelAndView = new ModelAndView("user_form");
		modelAndView.addObject("user", user);
		return modelAndView;
	}
	
	public void deleteUser(Long id) throws UserNotFoundException {
		Optional<User> optional = repository.findById(id);
		if(optional.isEmpty())
			throw new UserNotFoundException("usuário não encontrado");
		
		repository.deleteById(id);
	}
}
