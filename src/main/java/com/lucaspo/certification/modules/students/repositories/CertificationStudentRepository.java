package com.lucaspo.certification.modules.students.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lucaspo.certification.modules.students.entities.CertificationStudentEntity;

public interface CertificationStudentRepository extends JpaRepository<CertificationStudentEntity, UUID>{

	@Query("SELECT c FROM Certifications c INNER JOIN c.studentEntity std WHERE std.email = :email AND c.technology = :technology")
	List<CertificationStudentEntity> findByStudentEmailAndTechnology(String email, String technology);
}