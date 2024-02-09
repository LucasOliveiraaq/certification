package com.lucaspo.certification.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucaspo.certification.modules.students.dto.VerifyHasCertificationDTO;
import com.lucaspo.certification.modules.students.userCases.VerifyIfHasCertificationUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {
	
	@Autowired
	private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

	@PostMapping("/verifyIfHasCertification")
	public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO) {
		var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
		if(result) {
			return "Usuario já fez a prova";
		} 
		return "Usuario pode fazer a prova";
	}
}
