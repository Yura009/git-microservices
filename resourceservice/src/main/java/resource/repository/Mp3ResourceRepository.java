package resource.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import resource.entity.Mp3Resource;

public interface Mp3ResourceRepository extends JpaRepository<Mp3Resource, Long> {}
