package com.lanaVitor.Reservas.com.services.validation;

import com.lanaVitor.Reservas.com.controllers.exceptions.FieldMessage;
import com.lanaVitor.Reservas.com.dtos.UserDTO;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserDTO> {

	@Autowired
	private UserRepository repository;

	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		if (!domainEmailValidator.isValid(dto.getEmail())) {
			list.add(new FieldMessage("email", "Email possui um formato inválido ou o domínio não existe"));
		}

		// Verifica se o e-mail já existe na base de dados
		UserDetails user = repository.findByEmail(dto.getEmail());
		if (user != null) {
			list.add(new FieldMessage("email", "Email já existe"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
