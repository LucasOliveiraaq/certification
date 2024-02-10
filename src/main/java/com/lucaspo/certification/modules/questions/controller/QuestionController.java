package com.lucaspo.certification.modules.questions.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucaspo.certification.modules.questions.dto.AlternativesResultDTO;
import com.lucaspo.certification.modules.questions.dto.QuestionResultDTO;
import com.lucaspo.certification.modules.questions.entities.AlternativesEntity;
import com.lucaspo.certification.modules.questions.entities.QuestionEntity;
import com.lucaspo.certification.modules.questions.repositories.QuestionRepository;

import jakarta.persistence.Id;

@RestController
@RequestMapping("/questions")
public class QuestionController {

	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/technology/{technology}")
	public List<QuestionResultDTO> findByTechnology(@PathVariable String technology){
		var result = this.questionRepository.findByTechnology(technology);
		var toMap = result.stream().map(question -> mapQuestionToDTO(question)).collect(Collectors.toList());
		return toMap;
	}
	
	static QuestionResultDTO mapQuestionToDTO(QuestionEntity question) {
		var questionResultDTO = QuestionResultDTO.builder()
				.id(question.getId())
				.technology(question.getTechnology())
				.description(question.getDescription()).build();
		List<AlternativesResultDTO> alternativesResultDTOs = question.getAlternativesEntities()
				.stream().map(alternative -> mapAlternativeDTO(alternative)).collect(Collectors.toList());
		questionResultDTO.setAlternatives(alternativesResultDTOs);
		return questionResultDTO;
	}
	
	static AlternativesResultDTO mapAlternativeDTO(AlternativesEntity alternativesEntity) {
		return AlternativesResultDTO.builder()
				.id(alternativesEntity.getId())
				.description(alternativesEntity.getDescription()).build();
	}
}
