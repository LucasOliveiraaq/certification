package com.lucaspo.certification.modules.students.userCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucaspo.certification.modules.questions.dto.AlternativesResultDTO;
import com.lucaspo.certification.modules.questions.entities.QuestionEntity;
import com.lucaspo.certification.modules.questions.repositories.QuestionRepository;
import com.lucaspo.certification.modules.students.dto.StudentCertificationAnswerDTO;
import com.lucaspo.certification.modules.students.dto.VerifyHasCertificationDTO;
import com.lucaspo.certification.modules.students.entities.AnswersCertificationsEntity;
import com.lucaspo.certification.modules.students.entities.CertificationStudentEntity;
import com.lucaspo.certification.modules.students.entities.StudentEntity;
import com.lucaspo.certification.modules.students.repositories.CertificationStudentRepository;
import com.lucaspo.certification.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersUseCase {

	@Autowired
	private StudentRepository studentRepository; 
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private CertificationStudentRepository certificationStudentRepository;
	
	@Autowired
	private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;
	
	public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception{		
		
		var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));
		
		if(hasCertification) {
			throw new Exception("Você já tirou sua certificação!");
		}
		
		List<QuestionEntity> questionEntities = questionRepository.findByTechnology(dto.getTechnology());
		List<AnswersCertificationsEntity> answersCertificationsEntities = new ArrayList<>();
		AtomicInteger count = new AtomicInteger(0);
		dto.getQuestionAnswer()
		.stream().forEach(questionAnswer -> {
			var question = questionEntities.stream().filter(questionFilter -> questionFilter.getId().equals(questionAnswer.getQuestionID()))
								.findFirst().get();
			var findCorrectAlternative =  question.getAlternativesEntities().stream().filter(alternative -> alternative.isCorrect())
								.findFirst().get();;
			
			if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
				questionAnswer.setCorrect(true);
				count.incrementAndGet();
			} else {
				questionAnswer.setCorrect(false);
			}
			
			var answers = AnswersCertificationsEntity.builder()
					.answerId(questionAnswer.getAlternativeID())
					.questionId(questionAnswer.getQuestionID())
					.isCorrect(questionAnswer.isCorrect()).build();
			
			answersCertificationsEntities.add(answers);
		});
		var student = studentRepository.findByEmail(dto.getEmail());
		UUID studentID;
		if(student.isEmpty()) {
			var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
			studentCreated = studentRepository.save(studentCreated);
			studentID = studentCreated.getId();
		} else {
			studentID = student.get().getId();
		}
		
		
		CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
																.technology(dto.getTechnology())
																.studentId(studentID)
																.grade(count.get())
																.build();
		
		var certificationStudentCreated =  certificationStudentRepository.save(certificationStudentEntity);
		
		answersCertificationsEntities.stream().forEach(answerCertification -> {
			answerCertification.setCertificationId(certificationStudentEntity.getId());
			answerCertification.setCertificationStudentEntity(certificationStudentEntity);
		});
		
		certificationStudentEntity.setAnswersCertificationsEntities(answersCertificationsEntities);
		
		certificationStudentRepository.save(certificationStudentEntity);
		return certificationStudentCreated;
	}
}
