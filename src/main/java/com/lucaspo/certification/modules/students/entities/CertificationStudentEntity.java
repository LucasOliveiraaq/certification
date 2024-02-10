package com.lucaspo.certification.modules.students.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Certifications")
public class CertificationStudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(length = 100)
	private String technology;
	@Column(length = 100)
	private int grade;
	
	@Column(name = "student_id")
	private UUID studentId;
	
	@ManyToOne
	@JoinColumn(name = "student_id", insertable = false, updatable = false)
	@JsonBackReference
	private StudentEntity studentEntity;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "answer_certification_id", insertable = false, updatable = false)
	@JsonManagedReference
	private List<AnswersCertificationsEntity> answersCertificationsEntities;
}
