
package com.kshem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kshem.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // 1. Find skill by name
    Optional<Skill> findBySkillNameIgnoreCase(String skillName);

    // 2. Check whether skill already exists
    boolean existsBySkillNameIgnoreCase(String skillName);

    // 3. Search skills by name
    List<Skill> findBySkillNameContainingIgnoreCase(String skillName);

    // 4. Search skills with pagination
    Page<Skill> findBySkillNameContainingIgnoreCase(
            String skillName, Pageable pageable);

    // 5. Find skills by category
    List<Skill> findByCategoryIgnoreCase(String category);

    // 6. Find skills by category with pagination
    Page<Skill> findByCategoryIgnoreCase(
            String category, Pageable pageable);

    // 7. Search skills by name or category
    @Query("""
            SELECT s FROM Skill s
            WHERE LOWER(s.skillName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Skill> searchSkills(
            @Param("keyword") String keyword,
            Pageable pageable);

    // 8. Find skills associated with a student
    @Query("""
            SELECT s FROM Skill s
            JOIN s.students st
            WHERE st.id = :studentId
            """)
    List<Skill> findSkillsByStudentId(
            @Param("studentId") Long studentId);

    // 9. Count students having a particular skill
    @Query("""
            SELECT COUNT(st) FROM Student st
            JOIN st.skills s
            WHERE s.id = :skillId
            """)
    long countStudentsBySkillId(
            @Param("skillId") Long skillId);

}
