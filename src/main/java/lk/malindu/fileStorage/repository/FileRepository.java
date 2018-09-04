package lk.malindu.fileStorage.repository;

import lk.malindu.fileStorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Integer> {
}
